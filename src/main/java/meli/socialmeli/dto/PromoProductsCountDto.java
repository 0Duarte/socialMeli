package meli.socialmeli.dto;

public class PromoProductsCountDto {
    private Integer user_id;
    private String user_name;
    private Integer promo_products_count;

    public PromoProductsCountDto(Integer user_id, String user_name, Integer promo_products_count) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.promo_products_count = promo_products_count;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public Integer getPromo_products_count() {
        return promo_products_count;
    }
}
