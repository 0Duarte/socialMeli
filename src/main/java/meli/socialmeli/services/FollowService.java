package meli.socialmeli.services;

import jakarta.transaction.Transactional;
import meli.socialmeli.model.User;
import meli.socialmeli.repository.UserRepository;
import org.springframework.stereotype.Service;

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
                new IllegalArgumentException("Usuário seguidor não encontrado."));

        User seller = userRepository.findById(userIdToFollow).orElseThrow(() ->
                new IllegalArgumentException("Usuário a ser seguido não encontrado."));

        if (!follower.getFollowings().contains(seller)) {
            follower.getFollowings().add(seller);
            userRepository.save(follower);
        }
    }
}
