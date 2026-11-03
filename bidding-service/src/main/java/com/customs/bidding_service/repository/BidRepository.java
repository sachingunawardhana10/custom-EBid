
package com.customs.bidding_service.repository;

import com.customs.bidding_service.entity.Bid;
import com.customs.bidding_service.entity.BidStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {

    List<Bid> findByAuctionId(UUID auctionId);

    // Find the highest valid bid for an auction
    Optional<Bid> findTopByAuctionIdAndStatusOrderByAmountDesc(
            UUID auctionId,
            BidStatus status
    );

    // Find an existing bid using its idempotency key
    Optional<Bid> findByAuctionIdAndIdempotencyKey(
            UUID auctionId,
            String idempotencyKey
    );
}
