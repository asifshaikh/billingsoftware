package in.mohammed.billingsoftware.service.impl;

import in.mohammed.billingsoftware.entity.CategoryEntity;
import in.mohammed.billingsoftware.io.CategoryRequest;
import in.mohammed.billingsoftware.io.CategoryResponse;
import in.mohammed.billingsoftware.repository.CategoryRepository;
import in.mohammed.billingsoftware.service.CategoryService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
   private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse add(CategoryRequest categoryRequest) {
         CategoryEntity newCategory =convertToEntity(categoryRequest);
         newCategory=categoryRepository.save(newCategory);
          return convertToResponse(newCategory);
    }

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll().stream().map(
                categoryEntity -> convertToResponse(categoryEntity)
        ).collect(Collectors.toList());
    }

    @Override
    public void delete(String categoryId) {
       CategoryEntity existingCategory = categoryRepository.findByCategoryId(categoryId).orElseThrow(()->new RuntimeException(
                "Category with ID: " + categoryId + " not found"
        ));
         categoryRepository.delete(existingCategory);
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl(newCategory.getImgUrl())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .build();
    }

    private CategoryEntity convertToEntity(CategoryRequest categoryRequest) {
        return CategoryEntity.builder()
                .categoryId(UUID.randomUUID().toString())
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .bgColor(categoryRequest.getBgColor())
                .build();
    }
}
