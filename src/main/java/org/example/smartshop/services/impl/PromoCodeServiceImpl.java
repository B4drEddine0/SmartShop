package org.example.smartshop.services.impl;

import org.example.smartshop.services.PromoCodeService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

    private static final Set<String> VALID_PROMO_CODES = Set.of(
            "PROMO-2024", "PROMO-NOEL", "PROMO-SALE", "PROMO-VIP1"
    );

    private static final Double PROMO_DISCOUNT_PERCENTAGE = 0.05;

    @Override
    public boolean isValidPromoCode(String code) {
        return code != null && VALID_PROMO_CODES.contains(code);
    }

    @Override
    public Double getPromoDiscount() {
        return PROMO_DISCOUNT_PERCENTAGE;
    }
}
