package in.mohammed.billingsoftware.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.mohammed.billingsoftware.io.ItemRequest;
import in.mohammed.billingsoftware.io.ItemResponse;
import in.mohammed.billingsoftware.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/items")
    public ItemResponse addItem(@RequestPart("item")String request,
                                @RequestPart("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        ItemRequest itemRequest = null;
        try {
            // Convert the JSON string to ItemRequest object
             itemRequest = objectMapper.readValue(request, ItemRequest.class);
            // Call the service to add the item
            return itemService.add(itemRequest, file);
        } catch (Exception e) {
            throw new RuntimeException("Error processing item request: " + e.getMessage(), e);
        }
    }

    @GetMapping("/items")
    public List<ItemResponse> readItems() {
        return itemService.fetchItems();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/admin/items/{itemId}")
    public void removeItem(@PathVariable String itemId){
        try {
            itemService.deleteItem(itemId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting item: " + e.getMessage(), e);
        }
    }
}
