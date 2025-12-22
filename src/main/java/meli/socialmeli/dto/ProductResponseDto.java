package meli.socialmeli.dto;

public class ProductResponseDto {
    private Integer product_id;
    private String product_name;
    private String type;
    private String brand;
    private String color;
    private String notes;

    public ProductResponseDto(Integer product_id,
                              String product_name,
                              String type,
                              String brand,
                              String color,
                              String notes) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.type = type;
        this.brand = brand;
        this.color = color;
        this.notes = notes;
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
