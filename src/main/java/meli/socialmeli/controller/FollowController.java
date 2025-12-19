package meli.socialmeli.controller;

import meli.socialmeli.dto.FollowersCountDto;
import meli.socialmeli.dto.UserFollowersListDto;
import meli.socialmeli.services.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<FollowersCountDto> getFollowersCount(@PathVariable Integer userId) {
        FollowersCountDto dto = followService.getFollowersCount(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<UserFollowersListDto> getFollowersList(@PathVariable Integer userId){
        UserFollowersListDto dto = followService.getFollowersList(userId);
        return ResponseEntity.ok(dto);
    }
}
