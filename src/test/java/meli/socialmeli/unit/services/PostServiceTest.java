package meli.socialmeli.unit.services;

import meli.socialmeli.dto.response.FollowedPostDto;
import meli.socialmeli.dto.response.FollowedPostsResponseDto;
import meli.socialmeli.model.Post;
import meli.socialmeli.model.Product;
import meli.socialmeli.model.User;
import meli.socialmeli.repository.PostRepository;
import meli.socialmeli.repository.UserRepository;
import meli.socialmeli.services.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void getFollowedPosts_shouldThrowException_whenOrderByDateIsInvalid() {//05
        User user = new User(1, "user", false);
        User seller = new User(2, "seller", true);
        user.getFollowings().add(seller);

        Post post = new Post();
        post.setSeller(seller);
        post.setDate(LocalDate.now());

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findBySellerIn(Set.of(seller))).thenReturn(List.of(post));

        assertThrows(IllegalArgumentException.class,
                () -> postService.getFollowedPosts(1, "data_invalida"));
    }

    @Test
    void getFollowedPosts_shouldSortByDateAsc_whenOrderIsDateAsc() {//05 06
        // arrange
        User user = new User(1, "user", false);
        User seller = new User(2, "seller", true);
        user.getFollowings().add(seller);

        LocalDate today = LocalDate.now();
        LocalDate d1 = today.minusDays(1);
        LocalDate d2 = today.minusDays(2);

        Product product = new Product(10, "Prod", "Type", "Brand", "Color", null);

        Post postMaisRecente = new Post(seller, d1, product, 100, 10.0, false, null);
        Post postMaisAntigo  = new Post(seller, d2, product, 100, 10.0, false, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findBySellerIn(Set.of(seller)))
                .thenReturn(List.of(postMaisRecente, postMaisAntigo));

        FollowedPostsResponseDto response = postService.getFollowedPosts(1, "date_asc");

        List<LocalDate> datas = response.getPosts().stream()
                .map(FollowedPostDto::getDate)
                .toList();

        assertEquals(
                List.of(
                        d2,
                        d1
                ),
                datas
        );
    }

    @Test
    void getFollowedPosts_shouldSortByDateDesc_whenOrderIsDateDesc() {//06
        User user = new User(1, "user", false);
        User seller = new User(2, "seller", true);
        user.getFollowings().add(seller);

        LocalDate today = LocalDate.now();
        Product product = new Product(1, "P", "T", "B", "C", "N");

        Post p1 = new Post(seller, today.minusDays(2), product, 100, 10.0, false, null);
        Post p2 = new Post(seller, today.minusDays(1), product, 100, 10.0, false, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findBySellerIn(Set.of(seller))).thenReturn(List.of(p1, p2));

        FollowedPostsResponseDto response = postService.getFollowedPosts(1, "date_desc");

        List<LocalDate> dates = response.getPosts().stream()
                .map(FollowedPostDto::getDate)
                .toList();

        assertEquals(
                List.of(
                        today.minusDays(1),
                        today.minusDays(2)
                ),
                dates
        );
    }

    @Test
    void getFollowedPosts_shouldReturnOnlyPostsFromLastTwoWeeks() { // T-0008
        User user = new User(1, "user", false);
        User seller = new User(2, "seller", true);
        user.getFollowings().add(seller);

        LocalDate today = LocalDate.now();
        LocalDate dentro1 = today.minusDays(1);
        LocalDate dentro2 = today.minusDays(10);
        LocalDate fora   = today.minusDays(20);

        Product product = new Product(10, "Prod", "Type", "Brand", "Color", null);

        Post postDentro1 = new Post(seller, dentro1, product, 100, 10.0, false, null);
        Post postDentro2 = new Post(seller, dentro2, product, 100, 20.0, false, null);
        Post postFora    = new Post(seller, fora,    product, 100, 30.0, false, null);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findBySellerIn(Set.of(seller)))
                .thenReturn(List.of(postDentro1, postDentro2, postFora));

        FollowedPostsResponseDto response = postService.getFollowedPosts(1, null);

        List<LocalDate> datas = response.getPosts().stream()
                .map(FollowedPostDto::getDate)
                .toList();

        List<LocalDate> expectedDates = List.of(
            dentro1,
            dentro2
        );

        assertEquals(2, datas.size());
        assertTrue(datas.containsAll(expectedDates));
    }

}