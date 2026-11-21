package lk.customsebid.auction.service;

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

    // Create a new item
    public Item createItem(Item item) {
        item.setId(null);
        item.setStatus(ItemStatus.AVAILABLE);

        return itemRepository.save(item);
    }

    // Retrieve all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Retrieve an item by UUID
    public Optional<Item> getItemById(UUID id) {
        return itemRepository.findById(id);
    }

    // Update an existing item
    public Optional<Item> updateItem(UUID id, Item updatedItem) {

        return itemRepository.findById(id).map(existingItem -> {

            existingItem.setTitle(updatedItem.getTitle());
            existingItem.setDescription(updatedItem.getDescription());
            existingItem.setCategory(updatedItem.getCategory());
            existingItem.setCondition(updatedItem.getCondition());
            existingItem.setStartingPrice(updatedItem.getStartingPrice());

            return itemRepository.save(existingItem);
        });
    }

    // Deactivate an item instead of permanently deleting it
    public Optional<Item> deleteItem(UUID id) {

        return itemRepository.findById(id).map(existingItem -> {

            existingItem.setStatus(ItemStatus.INACTIVE);

            return itemRepository.save(existingItem);
        });
    }
}
