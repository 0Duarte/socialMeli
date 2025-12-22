package meli.socialmeli.dto;

import java.util.List;

public class FollowedPostsResponseDto {
    private Integer user_id;
    private List<FollowedPostDto> posts;

    public FollowedPostsResponseDto(Integer user_id, List<FollowedPostDto> posts) {
        this.user_id = user_id;
        this.posts = posts;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public List<FollowedPostDto> getPosts() {
        return posts;
    }
}
