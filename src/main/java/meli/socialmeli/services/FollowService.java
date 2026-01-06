package meli.socialmeli.services;

import jakarta.transaction.Transactional;
import meli.socialmeli.dto.response.FollowersCountDto;
import meli.socialmeli.dto.response.UserFollowersListDto;
import meli.socialmeli.dto.response.UserFollowingListDto;
import meli.socialmeli.dto.response.UserSummaryDto;
import meli.socialmeli.model.User;
import meli.socialmeli.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {
    private final UserRepository userRepository;

    public FollowService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void follow(Integer userId, Integer userIdToFollow){
        if (userId == null || userIdToFollow == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User IDs cannot be null.");
        }

        if (userId.equals(userIdToFollow)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user cannot follow themselves.");
        }

        User follower = userRepository.findById(userId).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow user not found."));

        User seller = userRepository.findById(userIdToFollow).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to follow not found."));

        if (!seller.getIs_seller()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user to follow is not a seller.");
        }

        if (!follower.getFollowings().contains(seller)) {
            follower.getFollowings().add(seller);
            userRepository.save(follower);
        }
    }

    @Transactional
    public void unfollow(Integer userId, Integer userIdToUnfollow){
        if(userId.equals(userIdToUnfollow)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user cannot unfollow themselves.");
        }

        User follower = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Follow user not found."));

        User seller = userRepository.findById(userIdToUnfollow).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User to unfollow not found."));

        if (follower.getFollowings().contains(seller)) {
            follower.getFollowings().remove(seller);
            userRepository.save(follower);
        }
    }

    public FollowersCountDto getFollowersCount(Integer userId){
        User seller = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        int followersCount = seller.getFollowers().size();

        return new FollowersCountDto(userId, seller.getUserName(), followersCount);

    }

    public UserFollowersListDto getFollowersList(Integer userId, String order){
        User seller = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        List<UserSummaryDto> followersDtos = seller.getFollowers().stream().map(follower ->
                new UserSummaryDto(
                        follower.getId(),
                        follower.getUserName()))
                .collect(Collectors.toList());

        sortByName(followersDtos, order);

        return new UserFollowersListDto(
                userId,
                seller.getUserName(),
                followersDtos
        );
    }

    public UserFollowingListDto getFollowedList(Integer userId, String order){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        List<UserSummaryDto> followingDtos = user.getFollowings().stream().map(following ->
                new UserSummaryDto(
                        following.getId(),
                        following.getUserName()))
                .collect(Collectors.toList());

        sortByName(followingDtos, order);

        return new UserFollowingListDto(
                userId,
                user.getUserName(),
                followingDtos
        );
    }

    private void sortByName(List<UserSummaryDto> list, String order){
        if(order == null || order.isBlank()){
            return;
        }

        switch(order){
            case "name_asc":
                list.sort(Comparator.comparing(UserSummaryDto::getUserName));
                break;
            case "name_desc":
                list.sort(Comparator.comparing(UserSummaryDto::getUserName).reversed());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order, use 'name_asc' or 'name_desc'.");
        }
    }
}
