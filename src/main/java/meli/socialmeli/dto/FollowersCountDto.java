package meli.socialmeli.dto;

public class FollowersCountDto {
    private Integer userId;
    private String userName;
    private Integer followersCount;

    public FollowersCountDto(Integer userId, String userName, Integer followersCount) {
        this.userId = userId;
        this.userName = userName;
        this.followersCount = followersCount;
    }

    public Integer getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public Integer getFollowersCount() {
        return followersCount;
    }
}
