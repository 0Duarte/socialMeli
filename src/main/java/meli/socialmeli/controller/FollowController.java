package meli.socialmeli.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import meli.socialmeli.dto.response.FollowersCountDto;
import meli.socialmeli.dto.response.UserFollowersListDto;
import meli.socialmeli.dto.response.UserFollowingListDto;
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

    @Operation(
            summary = "[US0001] Follow seller",
            description = "Allows a user to follow seller.")
    @PostMapping("/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<Void> follow(
            @PathVariable Integer userId,
            @PathVariable Integer userIdToFollow) {

        followService.follow(userId, userIdToFollow);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "[US0002] Number of followers",
            description = "Get the number of users that follow some seller.")
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<FollowersCountDto> getFollowersCount(@PathVariable Integer userId) {
        FollowersCountDto dto = followService.getFollowersCount(userId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "[US0003] List Who follow me?",
            description = "List all users that follow a seller (Who follow me?)")
    @GetMapping("/{userId}/followers/list")
    public ResponseEntity<UserFollowersListDto> getFollowersList(
            @PathVariable Integer userId,
            @RequestParam(required = false) String order) {
        UserFollowersListDto dto = followService.getFollowersList(userId, order);
        return ResponseEntity.ok(dto);
    }


    @Operation(
            summary = "[US0004] List Who am i following?",
            description = "List all sellers followed by a user (Who am i following?)")
    @GetMapping("/{userId}/followed/list")
    public ResponseEntity<UserFollowingListDto> getFollowingList(
            @PathVariable Integer userId,
            @RequestParam(required = false) String order) {
        UserFollowingListDto dto = followService.getFollowedList(userId, order);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "[US0007] Unfollow a seller",
            description = "Unfollow a seller")
    @PostMapping("/{userId}/unfollow/{userIdToFollow}")
    public ResponseEntity<Void> unfollow(
            @PathVariable Integer userId,
            @PathVariable Integer userIdToFollow) {

        followService.unfollow(userId, userIdToFollow);
        return ResponseEntity.ok().build();
    }
}
