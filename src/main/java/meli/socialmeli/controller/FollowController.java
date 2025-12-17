package meli.socialmeli.controller;

import meli.socialmeli.services.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class FollowController {
    private final FollowService followService;

    public FollowController (FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<Void> follow(
            @PathVariable Integer userId,
            @PathVariable Integer userIdToFollow) {

        followService.follow(userId, userIdToFollow);
        return ResponseEntity.ok().build();
    }
}
