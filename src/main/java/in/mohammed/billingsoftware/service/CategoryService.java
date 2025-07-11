package in.mohammed.billingsoftware.service;

import in.mohammed.billingsoftware.io.CategoryRequest;
import in.mohammed.billingsoftware.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
     CategoryResponse add(CategoryRequest categoryRequest , MultipartFile file);
     List<CategoryResponse>read();
     void delete(String categoryId);
}
