
package com.customs.bidding_service.controller;

import com.customs.bidding_service.dto.AutoBidRequest;
import com.customs.bidding_service.entity.AutoBid;
import com.customs.bidding_service.service.AutoBidService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auto-bids")
public class AutoBidController {

    private final AutoBidService autoBidService;

    public AutoBidController(AutoBidService autoBidService) {
        this.autoBidService = autoBidService;
    }

    @PostMapping
    public ResponseEntity<?> createAutoBid(
            @RequestBody AutoBidRequest request) {

        try {
            AutoBid savedAutoBid =
                    autoBidService.createOrUpdateAutoBid(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Automatic bid configuration saved successfully. ID: "
                            + savedAutoBid.getId());

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}
