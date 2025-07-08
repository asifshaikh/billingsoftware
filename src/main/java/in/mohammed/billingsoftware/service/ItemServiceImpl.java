package in.mohammed.billingsoftware.service;

import in.mohammed.billingsoftware.entity.CategoryEntity;
import in.mohammed.billingsoftware.entity.ItemEntity;
import in.mohammed.billingsoftware.io.ItemRequest;
import in.mohammed.billingsoftware.io.ItemResponse;
import in.mohammed.billingsoftware.repository.CategoryRepository;
import in.mohammed.billingsoftware.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final FileUploadService fileUploadService;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public ItemResponse add(ItemRequest request, MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file); // Upload the file and get the URL
        ItemEntity newItem = convertToEntity(request);
        CategoryEntity existingCategory = categoryRepository.findByCategoryId(request.getCategoryId()).orElseThrow(()->new RuntimeException("Category not found"+ request.getCategoryId())); // Find the category by ID and throw an exception if not found
        newItem.setCategory(existingCategory); // Set the category for the new item
        newItem.setImgUrl(imgUrl); // Set the image URL for the new item
         newItem = itemRepository.save(newItem); // Save the new item to the database
        return convertToResponse(newItem); // Convert the saved item entity to a response object and return it

    }

    private ItemResponse convertToResponse(ItemEntity newItem) {
        return ItemResponse.builder()
                .itemId(newItem.getItemId())
                .name(newItem.getName())
                .price(newItem.getPrice())
                .description(newItem.getDescription())
                .imgUrl(newItem.getImgUrl())
                .categoryId(newItem.getCategory().getCategoryId())
                .categoryName(newItem.getCategory().getName())
                .createdAt(newItem.getCreatedAt())
                .updatedAt(newItem.getUpdatedAt())
                .build();
    }

    private ItemEntity convertToEntity(ItemRequest request) {
        return ItemEntity.builder()
                .itemId(UUID.randomUUID().toString())
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .build();
    }

    @Override
    public List<ItemResponse> fetchItems() {
        List<ItemEntity> items = itemRepository.findAll(); // Fetch all items from the repository
        return items.stream() // Stream through the list of items
                .map(this::convertToResponse) // Convert each ItemEntity to ItemResponse
                .collect(Collectors.toList()); // Collect the results into a list
    }

    @Override
    public void deleteItem(String itemId) {
        ItemEntity item = itemRepository.findByItemId(itemId) // Find the item by its ID
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + itemId)); // Throw an exception if not found
        boolean isFileDelete =fileUploadService.deleteFile(item.getImgUrl()); // Delete the associated image file
        if(isFileDelete){
            itemRepository.delete(item); // If the file deletion was successful, delete the item from the repository
        } else {
            throw new RuntimeException("Failed to delete file for item with ID: " + itemId); // Throw an exception if file deletion failed
        }

    }
}
