package meli.socialmeli.services;

import jakarta.transaction.Transactional;
import meli.socialmeli.dto.FollowersCountDto;
import meli.socialmeli.dto.UserFollowersListDto;
import meli.socialmeli.dto.UserFollowingListDto;
import meli.socialmeli.dto.UserSummaryDto;
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
        if (userId.equals(userIdToFollow)){
            throw new IllegalArgumentException("Um usuário não pode seguir a si mesmo.");
        }

        User follower = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário seguidor não encontrado."));

        User seller = userRepository.findById(userIdToFollow).orElseThrow(() ->
                new IllegalArgumentException("Usuário a ser seguido não encontrado."));

        if (!follower.getFollowings().contains(seller)) {
            follower.getFollowings().add(seller);
            userRepository.save(follower);
        }
    }

    @Transactional
    public void unfollow(Integer userId, Integer userIdToUnfollow){
        if(userId.equals(userIdToUnfollow)){
            throw new IllegalArgumentException("Um usuário não pode deixar de seguir a si mesmo.");
        }

        User follower = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário seguidor não encontrado."));

        User seller = userRepository.findById(userIdToUnfollow).orElseThrow(() ->
                new IllegalArgumentException("Usuário a ser deixado de seguir não encontrado."));

        if (follower.getFollowings().contains(seller)) {
            follower.getFollowings().remove(seller);
            userRepository.save(follower);
        }
    }

    public FollowersCountDto getFollowersCount(Integer userId){
        User seller = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        int followersCount = seller.getFollowers().size();

        return new FollowersCountDto(userId, seller.getUserName(), followersCount);

    }

    public UserFollowersListDto getFollowersList(Integer userId, String order){
        User seller = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("Usuário a ser consultado não encontrado."));

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
                new IllegalArgumentException("Usuário não encontrado."));

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
                throw new IllegalArgumentException("Ordem inválida. Use 'name_asc' ou 'name_desc'.");
        }
    }
}
