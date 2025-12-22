package meli.socialmeli.dto;

public class NewPostRequestDto {
    private Integer user_id;
    private String date;
    private ProductRequestDto product;
    private Integer category;
    private Double price;

    public NewPostRequestDto() {
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }

    public ProductRequestDto getProduct() {
        return product;
    }

    public Integer getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }
}
