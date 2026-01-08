package meli.socialmeli.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class NewPostRequestDto {

    @NotNull(message = "user_id must not be null")
    @Positive
    private Integer user_id;

    @NotNull(message = "date must not be null")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @NotNull(message = "product must not be null")
    @Valid
    private ProductRequestDto product;

    @NotNull(message = "category must not be null")
    @Positive
    private Integer category;

    @NotNull(message = "price must not be null")
    @Positive
    @DecimalMax(value = "10000000.0")
    private Double price;

    public NewPostRequestDto() {
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setProduct(ProductRequestDto product) {
        this.product = product;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public LocalDate getDate() {
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
