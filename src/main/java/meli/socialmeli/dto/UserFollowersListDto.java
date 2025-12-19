package meli.socialmeli.dto;

import java.util.List;

public class UserFollowersListDto {
    private Integer userId;
    private String userName;
    private List<UserSummaryDto> followers;

    public UserFollowersListDto(Integer userId, String userName, List<UserSummaryDto> followers) {
        this.userId = userId;
        this.userName = userName;
        this.followers = followers;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<UserSummaryDto> getFollowers() {
        return followers;
    }
}
