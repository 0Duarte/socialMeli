package meli.socialmeli.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import meli.socialmeli.dto.response.FollowedPostsResponseDto;
import meli.socialmeli.dto.request.NewPostRequestDto;
import meli.socialmeli.dto.request.NewPromoPostRequestDto;
import meli.socialmeli.dto.response.PromoProductsCountDto;
import meli.socialmeli.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class PostController
{
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "[US0005] Create a new post",
            description = "Create a new post")
    @PostMapping("/publish")
    public ResponseEntity<Void> publish(@Valid @RequestBody NewPostRequestDto dto) {
        postService.createPost(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "[US0006] Get posts from followed users",
            description = "Get posts from users that the given user follows, with optional ordering")
    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<FollowedPostsResponseDto> getFollowedPosts(
            @PathVariable Integer userId,
            @RequestParam (required = false) String order) {
        FollowedPostsResponseDto response = postService.getFollowedPosts(userId, order);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "[US0010] Create a new promotional post",
            description = "Create a new promotional post")
    @PostMapping("/promo-pub")
    public ResponseEntity<Void> publishPromo(@Valid @RequestBody NewPromoPostRequestDto dto) {
        postService.createPromoPost(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "[US0011] Get count of promotional products for a user",
            description = "Get the count of promotional products associated with a specific user")
    @GetMapping("/promo-pub/count")
    public ResponseEntity<PromoProductsCountDto> getPromoProductsCount(
            @RequestParam("user_id") Integer userId) {

        PromoProductsCountDto dto = postService.getPromoProductsCount(userId);
        return ResponseEntity.ok(dto);
    }

}
