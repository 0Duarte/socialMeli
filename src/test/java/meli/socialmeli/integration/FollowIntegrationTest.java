package meli.socialmeli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FollowIntegrationTest {

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
    @DisplayName("US-0001: Should allow follow between existing users")
    @Transactional
    void us0001_shouldAllowFollowBetweenExistingUsers() throws Exception {
        User follower = userRepository.save(new User(null, "follower", false));
        User seller   = userRepository.save(new User(null, "seller", true));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User followerReloaded = userRepository.findById(follower.getId()).orElseThrow();

        assertThat(followerReloaded.getFollowings())
                .extracting(User::getId)
                .contains(seller.getId());
    }

    @Test
    @DisplayName("US-0002: Should return the number of followers for a seller")
    @Transactional
    void us0002_shouldReturnNumberOfFollowersForSeller() throws Exception {
        User seller    = userRepository.save(new User(null, "seller", true));
        User follower1 = userRepository.save(new User(null, "follower1", false));
        User follower2 = userRepository.save(new User(null, "follower2", false));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower1.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower2.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{userId}/followers/count", seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(seller.getId()))
                .andExpect(jsonPath("$.userName").value("seller"))
                .andExpect(jsonPath("$.followersCount").value(2));
    }

    @Test
    @DisplayName("US-0003: Who follow me - List followers of a seller ordered by name asc")
    @Transactional
    void us0003_shouldListFollowersOfSellerOrderedByNameAsc() throws Exception {
        User seller    = userRepository.save(new User(null, "seller", true));
        User ana       = userRepository.save(new User(null, "Ana", false));
        User carlos    = userRepository.save(new User(null, "Carlos", false));
        User bruno     = userRepository.save(new User(null, "Bruno", false));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        carlos.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        ana.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        bruno.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{userId}/followers/list", seller.getId())
                        .param("order", "name_asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(seller.getId()))
                .andExpect(jsonPath("$.userName").value("seller"))
                .andExpect(jsonPath("$.followers.length()").value(3))
                .andExpect(jsonPath("$.followers[0].userId").value(ana.getId()))
                .andExpect(jsonPath("$.followers[0].userName").value("Ana"))
                .andExpect(jsonPath("$.followers[1].userId").value(bruno.getId()))
                .andExpect(jsonPath("$.followers[1].userName").value("Bruno"))
                .andExpect(jsonPath("$.followers[2].userId").value(carlos.getId()))
                .andExpect(jsonPath("$.followers[2].userName").value("Carlos"));
    }

    @Test
    @DisplayName("US-0004: Should list sellers followed by a user ordered by name asc")
    @Transactional
    void us0004_shouldListSellersFollowedByUserOrderedByNameAsc() throws Exception {
        User follower = userRepository.save(new User(null, "follower", false));
        User sellerA  = userRepository.save(new User(null, "SellerA", true));
        User sellerC  = userRepository.save(new User(null, "SellerC", true));
        User sellerB  = userRepository.save(new User(null, "SellerB", true));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), sellerC.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), sellerA.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), sellerB.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{userId}/followed/list", follower.getId())
                        .param("order", "name_asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.userId").value(follower.getId()))
                .andExpect(jsonPath("$.userName").value("follower"))

                .andExpect(jsonPath("$.followed.length()").value(3))

                .andExpect(jsonPath("$.followed[0].userId").value(sellerA.getId()))
                .andExpect(jsonPath("$.followed[0].userName").value("SellerA"))
                .andExpect(jsonPath("$.followed[1].userId").value(sellerB.getId()))
                .andExpect(jsonPath("$.followed[1].userName").value("SellerB"))
                .andExpect(jsonPath("$.followed[2].userId").value(sellerC.getId()))
                .andExpect(jsonPath("$.followed[2].userName").value("SellerC"));
    }

    @Test
    @DisplayName("US-0007: Should allow unfollow between existing users")
    @Transactional
    void us0007_shouldAllowUnfollowBetweenExistingUsers() throws Exception {
        User follower = userRepository.save(new User(null, "followerUnf", false));
        User seller   = userRepository.save(new User(null, "sellerUnf", true));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}",
                        follower.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User followerBefore = userRepository.findById(follower.getId()).orElseThrow();
        assertThat(followerBefore.getFollowings())
                .extracting(User::getId)
                .contains(seller.getId());

        mockMvc.perform(post("/users/{userId}/unfollow/{userIdToFollow}",
                        follower.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        User followerAfter = userRepository.findById(follower.getId()).orElseThrow();
        assertThat(followerAfter.getFollowings())
                .extracting(User::getId)
                .doesNotContain(seller.getId());
    }

    @Test
    @DisplayName("US-0008: Should order followers by name asc and desc")
    @Transactional
    void us0008_shouldOrderFollowersByNameAscAndDesc() throws Exception {
        User seller = userRepository.save(new User(null, "sellerOrder", true));
        User zeca  = userRepository.save(new User(null, "Zeca", false));
        User ana   = userRepository.save(new User(null, "Ana", false));
        User bruno = userRepository.save(new User(null, "Bruno", false));

        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", zeca.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", ana.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/users/{userId}/follow/{userIdToFollow}", bruno.getId(), seller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{userId}/followers/list", seller.getId())
                        .param("order", "name_asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followers.length()").value(3))
                .andExpect(jsonPath("$.followers[0].userName").value("Ana"))
                .andExpect(jsonPath("$.followers[1].userName").value("Bruno"))
                .andExpect(jsonPath("$.followers[2].userName").value("Zeca"));

        mockMvc.perform(get("/users/{userId}/followers/list", seller.getId())
                        .param("order", "name_desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followers.length()").value(3))
                .andExpect(jsonPath("$.followers[0].userName").value("Zeca"))
                .andExpect(jsonPath("$.followers[1].userName").value("Bruno"))
                .andExpect(jsonPath("$.followers[2].userName").value("Ana"));
    }


}

