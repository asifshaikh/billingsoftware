package in.mohammed.billingsoftware.service.impl;

import in.mohammed.billingsoftware.entity.CategoryEntity;
import in.mohammed.billingsoftware.io.CategoryRequest;
import in.mohammed.billingsoftware.io.CategoryResponse;
import in.mohammed.billingsoftware.repository.CategoryRepository;
import in.mohammed.billingsoftware.repository.ItemRepository;
import in.mohammed.billingsoftware.service.CategoryService;
import in.mohammed.billingsoftware.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
   private final CategoryRepository categoryRepository;
   private final FileUploadService fileUploadService;
   private final ItemRepository itemRepository;

    @Override
    public CategoryResponse add(CategoryRequest categoryRequest , MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file); // Upload the file and get the URL

         CategoryEntity newCategory =convertToEntity(categoryRequest); // Convert request to entity
        newCategory.setImgUrl(imgUrl); // Set the image URL
         newCategory=categoryRepository.save(newCategory); // saving the entity to the databse
          return convertToResponse(newCategory); // Convert entity to response
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
        // Delete the image from S3
        fileUploadService.deleteFile(existingCategory.getImgUrl());
         categoryRepository.delete(existingCategory);
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
        Integer itemsCount = itemRepository.countByCategoryId(newCategory.getId());
        return CategoryResponse.builder()
                .categoryId(newCategory.getCategoryId())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl(newCategory.getImgUrl())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .items(itemsCount)
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
