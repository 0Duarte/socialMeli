package meli.socialmeli.services;

import meli.socialmeli.dto.NewPostRequestDto;
import meli.socialmeli.dto.ProductRequestDto;
import meli.socialmeli.model.Post;
import meli.socialmeli.model.Product;
import meli.socialmeli.model.User;
import meli.socialmeli.repository.PostRepository;
import meli.socialmeli.repository.ProductRepository;
import meli.socialmeli.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ProductRepository productRepository;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu");

    public PostService(UserRepository userRepository,
                       PostRepository postRepository,
                       ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.productRepository = productRepository;
    }

    public void createPost(NewPostRequestDto dto) {

        User seller = userRepository.findById(dto.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalDate date = LocalDate.parse(dto.getDate(), FORMATTER);

        ProductRequestDto p = dto.getProduct();

        Product product = new Product(
                p.getProduct_id(),
                p.getProduct_name(),
                p.getType(),
                p.getBrand(),
                p.getColor(),
                p.getNotes()
        );

        productRepository.save(product);

        Post post = new Post(
                seller,
                date,
                product,
                dto.getCategory(),
                dto.getPrice()
        );

        postRepository.save(post);
    }
}
