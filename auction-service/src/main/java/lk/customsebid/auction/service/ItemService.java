package lk.customsebid.auction.service;

import lk.customsebid.auction.dto.ItemRequest;
import lk.customsebid.auction.dto.ItemResponse;
import lk.customsebid.auction.entity.Item;
import lk.customsebid.auction.enums.ItemStatus;
import lk.customsebid.auction.repository.ItemRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public Optional<ItemResponse> getItemById(UUID id) {

        return itemRepository.findById(id)
                .map(this::convertToResponse);
    }

    // Update an existing item
    public Optional<ItemResponse> updateItem(UUID id, ItemRequest request) {

        return itemRepository.findById(id).map(existingItem -> {

            existingItem.setTitle(request.getTitle());
            existingItem.setDescription(request.getDescription());
            existingItem.setCategory(request.getCategory());
            existingItem.setCondition(request.getCondition());
            existingItem.setStartingPrice(request.getStartingPrice());

            Item savedItem = itemRepository.save(existingItem);

            return convertToResponse(savedItem);
        });
    }

    // Deactivate an item instead of permanently deleting it
    public Optional<ItemResponse> deleteItem(UUID id) {

        return itemRepository.findById(id).map(existingItem -> {

            existingItem.setStatus(ItemStatus.INACTIVE);

            Item savedItem = itemRepository.save(existingItem);

            return convertToResponse(savedItem);
        });
    }
}
