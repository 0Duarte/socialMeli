package meli.socialmeli.unit.services;

import meli.socialmeli.dto.response.FollowersCountDto;
import meli.socialmeli.dto.response.UserFollowersListDto;
import meli.socialmeli.dto.response.UserSummaryDto;
import meli.socialmeli.model.User;
import meli.socialmeli.repository.UserRepository;
import meli.socialmeli.services.FollowService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    @Test
    void follow_shouldAddFollowing_whenUsersExist() {//01
        User user = new User(1, "user1", false);
        User seller = new User(2, "seller", true);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(seller));

        followService.follow(1, 2);

        assertTrue(user.getFollowings().contains(seller));
        verify(userRepository).save(user);
    }

    @Test
    void follow_shouldThrowException_whenSellerDoesNotExist() {//01
        User follower = new User(1, "user1", false);

        when(userRepository.findById(1)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> followService.follow(1, 2));
    }

    @Test
    void unfollow_shouldThrowException_whenSellerDoesNotExist() {//02
        User user = new User(1, "user1", false);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> followService.unfollow(1, 2));
    }

    @Test
    void unfollow_shouldRemoveFollowing_whenUsersExist() {//02
        User user = new User(1, "user1", false);
        User seller = new User(2, "seller", true);

        user.getFollowings().add(seller);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.findById(2)).thenReturn(Optional.of(seller));

        followService.unfollow(1, 2);

        assertFalse(user.getFollowings().contains(seller));
        verify(userRepository).save(user);
    }

    @Test
    void getFollowersList_shouldThrowException_whenOrderIsInvalid() {//03
        User seller = new User(1, "seller", true);
        when(userRepository.findById(1)).thenReturn(Optional.of(seller));

        assertThrows(ResponseStatusException.class,
                () -> followService.getFollowersList(1, "qualquer_coisa"));
    }

    @Test
    void getFollowersList_shouldSortByNameAsc_whenOrderIsNameAsc() {//03 04
        User seller = new User(1, "seller", true);
        User a = new User(2, "Ana", false);
        User b = new User(3, "Bea", false);

        seller.getFollowers().add(b);
        seller.getFollowers().add(a);

        when(userRepository.findById(1)).thenReturn(Optional.of(seller));

        UserFollowersListDto dto = followService.getFollowersList(1, "name_asc");

        List<String> names = dto.getFollowers().stream()
                .map(UserSummaryDto::getUserName)
                .toList();

        assertEquals(List.of("Ana", "Bea"), names);
    }

    @Test
    void getFollowersList_shouldSortByNameDesc_whenOrderIsNameDesc() {//04
        User seller = new User(1, "seller", true);
        User a = new User(2, "Ana", false);
        User b = new User(3, "Bea", false);

        seller.getFollowers().add(a);
        seller.getFollowers().add(b);

        when(userRepository.findById(1)).thenReturn(Optional.of(seller));

        UserFollowersListDto dto = followService.getFollowersList(1, "name_desc");

        List<String> names = dto.getFollowers().stream()
                .map(UserSummaryDto::getUserName)
                .toList();

        assertEquals(List.of("Bea", "Ana"), names);
    }

    @Test
    void getFollowersCount_shouldReturnCorrectFollowersCount() { //07
        User seller = new User(2, "seller", true);
        User follower1 = new User(1, "user1", false);
        User follower2 = new User(3, "user2", false);

        seller.getFollowers().add(follower1);
        seller.getFollowers().add(follower2);

        when(userRepository.findById(2)).thenReturn(Optional.of(seller));

        FollowersCountDto dto = followService.getFollowersCount(2);

        assertEquals(2, dto.getFollowersCount());
        assertEquals(2, dto.getUserId());
        assertEquals("seller", dto.getUserName());
    }
}