package org.example.smartshop.services;

public interface PromoCodeService {
    boolean isValidPromoCode(String code);
    Double getPromoDiscount();
}
