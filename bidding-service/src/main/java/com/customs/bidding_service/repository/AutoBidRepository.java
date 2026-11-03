
package com.customs.bidding_service.repository;

import com.customs.bidding_service.entity.AutoBid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutoBidRepository extends JpaRepository<AutoBid, UUID> {

    List<AutoBid> findByAuctionIdAndActiveTrue(UUID auctionId);

    Optional<AutoBid> findByAuctionIdAndBidderId(
            UUID auctionId,
            UUID bidderId
    );
}
