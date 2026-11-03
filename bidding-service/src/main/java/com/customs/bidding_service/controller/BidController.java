
package com.customs.bidding_service.controller;

import com.customs.bidding_service.dto.BidRequest;
import com.customs.bidding_service.entity.Bid;
import com.customs.bidding_service.entity.BidStatus;
import com.customs.bidding_service.repository.BidRepository;

import jakarta.persistence.EntityManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidRepository bidRepository;
    private final EntityManager entityManager;

    public BidController(
            BidRepository bidRepository,
            EntityManager entityManager) {
        this.bidRepository = bidRepository;
        this.entityManager = entityManager;
    }

    // POST: Place a new bid with idempotency protection
    @PostMapping
    @Transactional
    public ResponseEntity<?> placeBid(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody BidRequest request) {

        // Validate required fields and positive amount
        if (request.auctionId() == null
                || request.bidderId() == null
                || request.amount() == null
                || request.amount().compareTo(BigDecimal.ZERO) <= 0
                || idempotencyKey.isBlank()
                || idempotencyKey.length() > 100) {

            return ResponseEntity.badRequest()
                    .body("Valid auction ID, bidder ID, positive amount and Idempotency-Key are required.");
        }

        // Lock this auction until the transaction finishes
        entityManager.createNativeQuery(
                "SELECT pg_advisory_xact_lock(hashtext(?1))"
        )
        .setParameter(1, request.auctionId().toString())
        .getResultList();

        // Check whether this request was already processed
        Optional<Bid> existingBid =
                bidRepository.findByAuctionIdAndIdempotencyKey(
                        request.auctionId(),
                        idempotencyKey
                );

        if (existingBid.isPresent()) {
            Bid previousBid = existingBid.get();

            // Prevent using the same key for a different request
            if (!previousBid.getBidderId().equals(request.bidderId())
                    || previousBid.getAmount().compareTo(request.amount()) != 0) {
                return ResponseEntity.badRequest()
                        .body("This Idempotency-Key has already been used for a different bid.");
            }

            // Return the original bid instead of creating a duplicate
            return ResponseEntity.ok(previousBid);
        }

        // Find the highest valid bid for this auction
        Optional<Bid> highestBid =
                bidRepository.findTopByAuctionIdAndStatusOrderByAmountDesc(
                        request.auctionId(),
                        BidStatus.VALID
                );

        // New bid must be higher than the current highest bid
        if (highestBid.isPresent()
                && request.amount().compareTo(
                        highestBid.get().getAmount()) <= 0) {

            return ResponseEntity.badRequest().body(
                    "Bid must be higher than the current highest bid: "
                            + highestBid.get().getAmount()
            );
        }

        // Create and save the bid
        Bid bid = new Bid();
        bid.setAuctionId(request.auctionId());
        bid.setBidderId(request.bidderId());
        bid.setAmount(request.amount());
        bid.setIdempotencyKey(idempotencyKey);

        Bid savedBid = bidRepository.save(bid);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedBid);
    }

    // GET: Retrieve all bids
    @GetMapping
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    // GET: Retrieve bids by auction ID
    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<Bid>> getBidsByAuction(
            @PathVariable UUID auctionId) {

        List<Bid> bids =
                bidRepository.findByAuctionId(auctionId);

        return ResponseEntity.ok(bids);
    }
}
