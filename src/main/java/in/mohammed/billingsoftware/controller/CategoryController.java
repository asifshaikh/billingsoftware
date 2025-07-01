package in.mohammed.billingsoftware.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.mohammed.billingsoftware.io.CategoryRequest;
import in.mohammed.billingsoftware.io.CategoryResponse;
import in.mohammed.billingsoftware.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategory(@RequestPart("category") String categoryString,
                                        @RequestPart("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest request = null;
        try {
            request = objectMapper.readValue(categoryString, CategoryRequest.class);
            return categoryService.add(request, file);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Exception while parsing json", e
            );
        }

    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> fetchCategories() {
        return categoryService.read();
    }
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable String categoryId) {
        try{
            categoryService.delete(categoryId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage()
            );
        }
    }
}
