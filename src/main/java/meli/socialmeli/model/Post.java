package meli.socialmeli.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer category;

    private Double price;

    public Post(){}

    public Post(User seller,
                LocalDate date,
                Product product,
                Integer category,
                Double price) {
        this.seller = seller;
        this.date = date;
        this.product = product;
        this.category = category;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }
}
