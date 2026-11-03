
package com.customs.bidding_service.service;

import com.customs.bidding_service.dto.AutoBidRequest;
import com.customs.bidding_service.entity.AutoBid;
import com.customs.bidding_service.repository.AutoBidRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AutoBidService {

    private final AutoBidRepository autoBidRepository;

    public AutoBidService(AutoBidRepository autoBidRepository) {
        this.autoBidRepository = autoBidRepository;
    }

    @Transactional
    public AutoBid createOrUpdateAutoBid(AutoBidRequest request) {

        if (request.auctionId() == null
                || request.bidderId() == null
                || request.maximumAmount() == null
                || request.maximumAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Auction ID, bidder ID and a positive maximum amount are required."
            );
        }

        AutoBid autoBid = autoBidRepository
                .findByAuctionIdAndBidderId(
                        request.auctionId(),
                        request.bidderId()
                )
                .orElseGet(AutoBid::new);

        autoBid.setAuctionId(request.auctionId());
        autoBid.setBidderId(request.bidderId());
        autoBid.setMaximumAmount(request.maximumAmount());
        autoBid.setActive(true);

        return autoBidRepository.save(autoBid);
    }
}
