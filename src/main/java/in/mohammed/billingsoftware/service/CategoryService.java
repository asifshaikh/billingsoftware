package in.mohammed.billingsoftware.service;

import in.mohammed.billingsoftware.io.CategoryRequest;
import in.mohammed.billingsoftware.io.CategoryResponse;

public interface CategoryService {
     CategoryResponse add(CategoryRequest categoryRequest);
}
