package meli.socialmeli.controller;

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

    @PostMapping("/publish")
    public ResponseEntity<Void> publish(@Valid @RequestBody NewPostRequestDto dto) {
        postService.createPost(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followed/{userId}/list")
    public ResponseEntity<FollowedPostsResponseDto> getFollowedPosts(
            @PathVariable Integer userId,
            @RequestParam (required = false) String order) {
        FollowedPostsResponseDto response = postService.getFollowedPosts(userId, order);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/promo-pub")
    public ResponseEntity<Void> publishPromo(@Valid @RequestBody NewPromoPostRequestDto dto) {
        postService.createPromoPost(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/promo-pub/count")
    public ResponseEntity<PromoProductsCountDto> getPromoProductsCount(
            @RequestParam("user_id") Integer userId) {

        PromoProductsCountDto dto = postService.getPromoProductsCount(userId);
        return ResponseEntity.ok(dto);
    }

}
