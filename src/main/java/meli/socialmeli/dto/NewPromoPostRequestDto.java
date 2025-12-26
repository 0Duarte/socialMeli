package meli.socialmeli.dto;

public class NewPromoPostRequestDto extends NewPostRequestDto{
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
