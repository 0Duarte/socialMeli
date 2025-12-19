package meli.socialmeli.dto;

import java.util.List;

public class UserFollowingListDto {
    private Integer userId;
    private String userName;
    private List<UserSummaryDto> followed;

    public UserFollowingListDto(Integer userId, String userName, List<UserSummaryDto> followed) {
        this.userId = userId;
        this.userName = userName;
        this.followed = followed;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<UserSummaryDto> getFollowed() {
        return followed;
    }
}
