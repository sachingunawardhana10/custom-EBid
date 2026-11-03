
package com.customs.bidding_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AutoBidRequest(
        UUID auctionId,
        UUID bidderId,
        BigDecimal maximumAmount
) {
}
