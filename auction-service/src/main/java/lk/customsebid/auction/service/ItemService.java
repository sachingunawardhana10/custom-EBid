package lk.customsebid.auction.service;

import lk.customsebid.auction.dto.ItemRequest;
import lk.customsebid.auction.dto.ItemResponse;
import lk.customsebid.auction.entity.Item;
import lk.customsebid.auction.enums.ItemStatus;
import lk.customsebid.auction.exception.ItemOperationException;
import lk.customsebid.auction.exception.ItemNotFoundException;
import lk.customsebid.auction.repository.ItemRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Convert Item entity into ItemResponse DTO
    private ItemResponse convertToResponse(Item item) {

        return new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getCategory(),
                item.getCondition(),
                item.getStartingPrice(),
                item.getStatus(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    // Create a new item
    public ItemResponse createItem(ItemRequest request) {

        Item item = new Item();

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setCondition(request.getCondition());
        item.setStartingPrice(request.getStartingPrice());
        item.setStatus(ItemStatus.AVAILABLE);

        Item savedItem = itemRepository.save(item);

        return convertToResponse(savedItem);
    }

    // Retrieve all items
    public List<ItemResponse> getAllItems() {

        return itemRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Retrieve an item by UUID
    public ItemResponse getItemById(UUID id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item not found with ID: " + id
                ));

        return convertToResponse(item);
    }

    // Update an existing item
    public ItemResponse updateItem(UUID id, ItemRequest request) {

        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item not found with ID: " + id
                ));

        // Only AVAILABLE items can be updated
        if (existingItem.getStatus() != ItemStatus.AVAILABLE) {
            throw new ItemOperationException(
                    "Only AVAILABLE items can be updated. Current status: "
                            + existingItem.getStatus()
            );
        }

        existingItem.setTitle(request.getTitle());
        existingItem.setDescription(request.getDescription());
        existingItem.setCategory(request.getCategory());
        existingItem.setCondition(request.getCondition());
        existingItem.setStartingPrice(request.getStartingPrice());

        Item savedItem = itemRepository.save(existingItem);

        return convertToResponse(savedItem);
    }

    // Deactivate an item instead of permanently deleting it
    public ItemResponse deleteItem(UUID id) {

        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item not found with ID: " + id
                ));

        // Only AVAILABLE items can be deactivated
        if (existingItem.getStatus() != ItemStatus.AVAILABLE) {
            throw new ItemOperationException(
                    "Only AVAILABLE items can be deactivated. Current status: "
                            + existingItem.getStatus()
            );
        }

        existingItem.setStatus(ItemStatus.INACTIVE);

        Item savedItem = itemRepository.save(existingItem);

        return convertToResponse(savedItem);
    }
}
