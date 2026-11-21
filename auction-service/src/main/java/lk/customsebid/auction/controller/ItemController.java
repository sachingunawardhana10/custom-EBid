package lk.customsebid.auction.controller;

import lk.customsebid.auction.dto.ItemRequest;
import lk.customsebid.auction.dto.ItemResponse;
import lk.customsebid.auction.service.ItemService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Create a new item
    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @Valid @RequestBody ItemRequest request) {

        ItemResponse createdItem = itemService.createItem(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdItem);
    }

    // Retrieve all items
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {

        List<ItemResponse> items = itemService.getAllItems();

        return ResponseEntity.ok(items);
    }

    // Retrieve an item by UUID
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(
            @PathVariable UUID id) {

        ItemResponse item = itemService.getItemById(id);

        return ResponseEntity.ok(item);
    }

    // Update an existing item
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable UUID id,
            @Valid @RequestBody ItemRequest request) {

        ItemResponse updatedItem = itemService.updateItem(id, request);

        return ResponseEntity.ok(updatedItem);
    }

    // Deactivate an item
    @DeleteMapping("/{id}")
    public ResponseEntity<ItemResponse> deleteItem(
            @PathVariable UUID id) {

        ItemResponse deletedItem = itemService.deleteItem(id);

        return ResponseEntity.ok(deletedItem);
    }
}
