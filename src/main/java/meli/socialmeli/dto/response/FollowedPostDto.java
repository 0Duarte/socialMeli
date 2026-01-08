package meli.socialmeli.dto.response;

import java.time.LocalDate;

public class FollowedPostDto {

    private Integer user_id;
    private Integer post_id;
    private LocalDate date;
    private ProductResponseDto product;
    private Integer category;
    private Double price;

    public FollowedPostDto(Integer user_id,
                           Integer post_id,
                           LocalDate date,
                           ProductResponseDto product,
                           Integer category,
                           Double price) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.date = date;
        this.product = product;
        this.category = category;
        this.price = price;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public LocalDate getDate() {return date;}

    public ProductResponseDto getProduct() {
        return product;
    }

    public Integer getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

}
