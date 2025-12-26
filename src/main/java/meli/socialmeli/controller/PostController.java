package meli.socialmeli.controller;

import meli.socialmeli.dto.FollowedPostsResponseDto;
import meli.socialmeli.dto.NewPostRequestDto;
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
    public ResponseEntity<Void> publish(@RequestBody NewPostRequestDto dto) {
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
}
