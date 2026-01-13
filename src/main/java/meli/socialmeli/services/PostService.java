package meli.socialmeli.services;

import meli.socialmeli.dto.request.NewPostRequestDto;
import meli.socialmeli.dto.request.NewPromoPostRequestDto;
import meli.socialmeli.dto.request.ProductRequestDto;
import meli.socialmeli.dto.response.FollowedPostDto;
import meli.socialmeli.dto.response.FollowedPostsResponseDto;
import meli.socialmeli.dto.response.ProductResponseDto;
import meli.socialmeli.dto.response.PromoProductsCountDto;
import meli.socialmeli.model.Post;
import meli.socialmeli.model.Product;
import meli.socialmeli.model.User;
import meli.socialmeli.repository.PostRepository;
import meli.socialmeli.repository.ProductRepository;
import meli.socialmeli.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ProductRepository productRepository;

    public PostService(UserRepository userRepository,
                       PostRepository postRepository,
                       ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.productRepository = productRepository;
    }

    public void createPost(NewPostRequestDto dto) {

        User seller = userRepository.findById(dto.getUser_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        if (!seller.getIs_seller()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only sellers can create posts");
        }

        LocalDate date = dto.getDate();

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
                dto.getPrice(),
                false,
                0.0
        );

        postRepository.save(post);
    }

    public FollowedPostsResponseDto getFollowedPosts(Integer userId, String order) {

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found"));

        Set<User> followings = user.getFollowings();
        if (followings.isEmpty()) {
            return new FollowedPostsResponseDto(userId, List.of());
        }

        List<Post> posts = postRepository.findBySellerIn(followings);

        LocalDate today = LocalDate.now();
        LocalDate twoWeeksAgo = today.minusDays(14);

        List<Post> recentPosts = posts.stream()
                .filter(post -> {
                    LocalDate postDate = post.getDate();
                    return (postDate != null &&
                            !postDate.isBefore(twoWeeksAgo) &&
                            !postDate.isAfter(today));
                })
                .collect(Collectors.toList());

        sortPostsByDate(recentPosts, order);

        List<FollowedPostDto> postDtos = recentPosts.stream()
                .map(post -> {
                    Product product = post.getProduct();
                    ProductResponseDto productDto = new ProductResponseDto(
                            product.getId(),
                            product.getName(),
                            product.getType(),
                            product.getBrand(),
                            product.getColor(),
                            product.getNotes()
                    );

                    return new FollowedPostDto(
                            post.getSeller().getId(),
                            post.getId(),
                            post.getDate(),
                            productDto,
                            post.getCategory(),
                            post.getPrice()
                    );
                })
                .collect(Collectors.toList());

        return new FollowedPostsResponseDto(userId, postDtos);
    }

    public void createPromoPost(NewPromoPostRequestDto dto) {

        User seller = userRepository.findById(dto.getUser_id()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        if (!seller.getIs_seller()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only sellers can create promo posts");
        }
        LocalDate date = dto.getDate();

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
                dto.getPrice(),
                dto.getHas_promo(),
                dto.getDiscount()
        );

        postRepository.save(post);
    }

    public PromoProductsCountDto getPromoProductsCount(Integer userId) {

        User seller = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        int count = postRepository.findBySellerAndHasPromoTrue(seller).size();

        return new PromoProductsCountDto(
                seller.getId(),
                seller.getUserName(),
                count
        );
    }

    private void sortPostsByDate(List<Post> posts, String order) {
        if (order == null || order.isBlank()) {
            posts.sort(Comparator.comparing(Post::getDate).reversed());
            return;
        }

        switch (order) {
            case "date_asc":
                posts.sort(Comparator.comparing(Post::getDate));
                break;
            case "date_desc":
                posts.sort(Comparator.comparing(Post::getDate).reversed());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order, use 'date_asc' or 'date_desc'.");
        }
    }
}
