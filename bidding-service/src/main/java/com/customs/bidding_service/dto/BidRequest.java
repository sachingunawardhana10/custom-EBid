
package com.customs.bidding_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BidRequest(
        UUID auctionId,
        UUID bidderId,
        BigDecimal amount
) {
}
