
package com.customs.bidding_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "auto_bids",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_auto_bid_auction_bidder",
            columnNames = {"auction_id", "bidder_id"}
        )
    }
)
public class AutoBid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "auction_id", nullable = false)
    private UUID auctionId;

    @Column(name = "bidder_id", nullable = false)
    private UUID bidderId;

    @Column(name = "maximum_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal maximumAmount;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public AutoBid() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(UUID auctionId) {
        this.auctionId = auctionId;
    }

    public UUID getBidderId() {
        return bidderId;
    }

    public void setBidderId(UUID bidderId) {
        this.bidderId = bidderId;
    }

    public BigDecimal getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(BigDecimal maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
