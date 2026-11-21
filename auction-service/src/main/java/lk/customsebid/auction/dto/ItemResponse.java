package lk.customsebid.auction.dto;

import lk.customsebid.auction.enums.ItemStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ItemResponse {

    private UUID id;
    private String title;
    private String description;
    private String category;
    private String condition;
    private BigDecimal startingPrice;
    private ItemStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public ItemResponse() {
    }

    public ItemResponse(
            UUID id,
            String title,
            String description,
            String category,
            String condition,
            BigDecimal startingPrice,
            ItemStatus status,
            Instant createdAt,
            Instant updatedAt) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.condition = condition;
        this.startingPrice = startingPrice;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
