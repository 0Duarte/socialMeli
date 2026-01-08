package meli.socialmeli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import meli.socialmeli.dto.request.NewPostRequestDto;
import meli.socialmeli.dto.request.NewPromoPostRequestDto;
import meli.socialmeli.dto.request.ProductRequestDto;
import meli.socialmeli.model.Post;
import meli.socialmeli.model.Product;
import meli.socialmeli.model.User;
import meli.socialmeli.repository.PostRepository;
import meli.socialmeli.repository.ProductRepository;
import meli.socialmeli.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PostIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("US-0005: Should create a new post successfully")
    @Transactional
    void us0005_shouldCreateNewPostSuccessfully() throws Exception {
        User seller = userRepository.save(new User(null, "sellerPost", true));

        ProductRequestDto productDto = new ProductRequestDto();
        productDto.setProduct_id(1);
        productDto.setProduct_name("Gamer Chair");
        productDto.setType("Gamer");
        productDto.setBrand("Racer");
        productDto.setColor("Red Black");
        productDto.setNotes("Special Edition");

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        NewPostRequestDto requestDto = new NewPostRequestDto();
        requestDto.setUser_id(seller.getId());
        requestDto.setDate(today);
        requestDto.setProduct(productDto);
        requestDto.setCategory(100);
        requestDto.setPrice(1500.50);

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/products/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        var posts = postRepository.findAll();
        assertThat(posts).hasSize(1);

        Post post = posts.get(0);
        assertThat(post.getSeller().getId()).isEqualTo(seller.getId());
        assertThat(post.getCategory()).isEqualTo(100);
        assertThat(post.getPrice()).isEqualTo(1500.50);
        assertThat(post.getProduct().getName()).isEqualTo("Gamer Chair");
    }

    @Test
    @DisplayName("US-0006: Should list recent posts from followed sellers ordered by date desc")
    @Transactional
    void us0006_shouldListRecentPostsFromFollowedSellersOrderedByDateDesc() throws Exception {
        User follower = userRepository.save(new User(null, "follower", false));

        User seller1 = userRepository.save(new User(null, "seller1", true));
        User seller2 = userRepository.save(new User(null, "seller2", true));
        User notFollowedSeller = userRepository.save(new User(null, "sellerX", true));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), seller1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), seller2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Product prod1 = productRepository.save(new Product(1, "Prod1", "Type1", "Brand1", "Red", "Notes1"));
        Product prod2 = productRepository.save(new Product(2, "Prod2", "Type2", "Brand2", "Blue", "Notes2"));
        Product prod3 = productRepository.save(new Product(3, "Prod3", "Type3", "Brand3", "Green", "Notes3"));

        LocalDate today = LocalDate.now();

        Post mostRecentPostSeller1 =
                postRepository.save(new Post(seller1, today.minusDays(1), prod1, 10, 100.0, false, 0.0));

        Post oldestPostSeller2 =
                postRepository.save(new Post(seller2, today.minusDays(5), prod2, 20, 200.0, false, 0.0));

        postRepository.save(new Post(seller1, today.minusDays(20), prod3, 30, 300.0, false, 0.0));

        postRepository.save(new Post(notFollowedSeller, today.minusDays(1), prod1, 40, 400.0, false, 0.0));

        mockMvc.perform(get("/products/followed/{userId}/list", follower.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.user_id").value(follower.getId()))

                .andExpect(jsonPath("$.posts.length()").value(2))

                .andExpect(jsonPath("$.posts[0].post_id").value(mostRecentPostSeller1.getId()))
                .andExpect(jsonPath("$.posts[1].post_id").value(oldestPostSeller2.getId()));
    }

    @Test
    @DisplayName("US-0009: Should order posts by date asc")
    @Transactional
    void us0009_shouldOrderPostsByDateAsc() throws Exception {
        User follower = userRepository.save(new User(null, "followerDateAsc", false));

        User seller1 = userRepository.save(new User(null, "sellerDate1", true));
        User seller2 = userRepository.save(new User(null, "sellerDate2", true));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), seller1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), seller2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Product prod1 = productRepository.save(new Product(10, "ProdA", "TypeA", "BrandA", "Red", "NotesA"));
        Product prod2 = productRepository.save(new Product(20, "ProdB", "TypeB", "BrandB", "Blue", "NotesB"));
        Product prod3 = productRepository.save(new Product(30, "ProdC", "TypeC", "BrandC", "Green", "NotesC"));

        LocalDate today = LocalDate.now();

        Post oldest =
                postRepository.save(new Post(seller1, today.minusDays(10), prod1, 10, 100.0, false, 0.0));
        Post middle =
                postRepository.save(new Post(seller2, today.minusDays(5), prod2, 20, 200.0, false, 0.0));
        Post mostRecent =
                postRepository.save(new Post(seller1, today.minusDays(1), prod3, 30, 300.0, false, 0.0));

        mockMvc.perform(get("/products/followed/{userId}/list", follower.getId())
                        .param("order", "date_asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(follower.getId()))
                .andExpect(jsonPath("$.posts.length()").value(3))

                .andExpect(jsonPath("$.posts[0].post_id").value(oldest.getId()))
                .andExpect(jsonPath("$.posts[1].post_id").value(middle.getId()))
                .andExpect(jsonPath("$.posts[2].post_id").value(mostRecent.getId()));
    }

    @Test
    @DisplayName("US-0010: Should create a new promotional post successfully")
    @Transactional
    void us0010_shouldCreateNewPromotionalPostSuccessfully() throws Exception {
        User seller = userRepository.save(new User(null, "sellerPromo", true));

        ProductRequestDto productDto = new ProductRequestDto();
        productDto.setProduct_id(10);
        productDto.setProduct_name("Mouse Gamer");
        productDto.setType("Peripheral");
        productDto.setBrand("Racer");
        productDto.setColor("Black");
        productDto.setNotes("With RGB");

        LocalDate today = LocalDate.now();

        NewPromoPostRequestDto requestDto = new NewPromoPostRequestDto();
        requestDto.setUser_id(seller.getId());
        requestDto.setDate(today);
        requestDto.setProduct(productDto);
        requestDto.setCategory(58);
        requestDto.setPrice(250.0);
        requestDto.setHas_promo(true);
        requestDto.setDiscount(0.15);

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/products/promo-pub")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());

        var posts = postRepository.findAll();
        assertThat(posts).hasSize(1);

        Post post = posts.get(0);
        assertThat(post.getSeller().getId()).isEqualTo(seller.getId());
        assertThat(post.getHasPromo()).isTrue();
        assertThat(post.getDiscount()).isEqualTo(0.15);
        assertThat(post.getProduct().getName()).isEqualTo("Mouse Gamer");
    }

    @Test
    @DisplayName("US-0011: Should return the count of promotional posts for a seller")
    @Transactional
    void us0011_shouldReturnCountOfPromotionalPostsForSeller() throws Exception {
        User seller = userRepository.save(new User(null, "sellerPromo", true));

        Product prod1 = productRepository.save(new Product(100, "PromoProd1", "Type1", "Brand1", "Red", "Notes1"));
        Product prod2 = productRepository.save(new Product(200, "PromoProd2", "Type2", "Brand2", "Blue", "Notes2"));
        Product prod3 = productRepository.save(new Product(300, "NonPromoProd", "Type3", "Brand3", "Green", "Notes3"));

        LocalDate today = LocalDate.now();

        Post promo1 = postRepository.save(new Post(seller, today.minusDays(1), prod1, 10, 100.0, true, 0.10));
        Post promo2 = postRepository.save(new Post(seller, today.minusDays(2), prod2, 20, 200.0, true, 0.20));

        postRepository.save(new Post(seller, today.minusDays(3), prod3, 30, 300.0, false, 0.0));

        mockMvc.perform(get("/products/promo-pub/count")
                        .param("user_id", seller.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(seller.getId()))
                .andExpect(jsonPath("$.user_name").value("sellerPromo"))
                .andExpect(jsonPath("$.promo_products_count").value(2));
    }
}
