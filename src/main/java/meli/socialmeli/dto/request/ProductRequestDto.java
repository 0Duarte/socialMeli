package meli.socialmeli.dto.request;

import jakarta.validation.constraints.*;

public class ProductRequestDto {
    @NotNull(message = "product_id must not be null")
    @Positive
    private Integer product_id;

    @NotBlank(message = "product_name must not be blank")
    @Size(max = 40 , message = "product_name must not exceed 40 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N} ]+$", message = "product_name does not allow special characters")
    private String product_name;

    @NotBlank(message = "type must not be blank")
    @Size(max = 15 , message = "type must not exceed 15 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s]+$", message = "type does not allow special characters")
    private String type;

    @NotBlank(message = "brand must not be blank")
    @Size(max = 25 , message = "brand must not exceed 25 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s]+$", message = "brand does not allow special characters")
    private String brand;

    @NotBlank(message = "color must not be blank")
    @Size(max = 15 , message = "color must not exceed 15 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\s]+$", message = "color does not allow special characters")
    private String color;

    @Size(max = 80 , message = "notes must not exceed 80 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N} ]+$", message = "notes does not allow special characters")
    private String notes;

    public ProductRequestDto() {
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
