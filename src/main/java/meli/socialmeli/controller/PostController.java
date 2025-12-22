package meli.socialmeli.controller;

import meli.socialmeli.dto.NewPostRequestDto;
import meli.socialmeli.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
