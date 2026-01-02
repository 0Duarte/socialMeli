package meli.socialmeli.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class NewPromoPostRequestDto extends NewPostRequestDto {
    @NotNull(message = "has_promo must not be null")
    private Boolean has_promo;
    private Double discount;

    public NewPromoPostRequestDto() {
    }

    public Boolean getHas_promo() {
        return has_promo;
    }

    public Double getDiscount() {
        return discount;
    }
}
