package meli.socialmeli.dto;

public class ProductRequestDto {
    private Integer product_id;
    private String product_name;
    private String type;
    private String brand;
    private String color;
    private String notes; // opcional

    public ProductRequestDto() {
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getNotes() {
        return notes;
    }
}
