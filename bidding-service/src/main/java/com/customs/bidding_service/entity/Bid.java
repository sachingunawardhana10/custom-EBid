

package com.customs.bidding_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "bids",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_bid_auction_idempotency",
            columnNames = {"auction_id", "idempotency_key"}
        )
    }
)
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "auction_id", nullable = false)
    private UUID auctionId;

    @Column(name = "bidder_id", nullable = false)
    private UUID bidderId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "bid_type", nullable = false)
    private BidType bidType = BidType.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BidStatus status = BidStatus.VALID;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    public Bid() {
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BidType getBidType() {
        return bidType;
    }

    public void setBidType(BidType bidType) {
        this.bidType = bidType;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }
}
