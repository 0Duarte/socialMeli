package meli.socialmeli.dto.response;

import java.util.List;

public class UserFollowersListDto {
    private Integer userId;
    private String userName;
    private Boolean isSeller;
    private List<UserSummaryDto> followers;

    public UserFollowersListDto(Integer userId, String userName, Boolean isSeller, List<UserSummaryDto> followers) {
        this.userId = userId;
        this.userName = userName;
        this.isSeller = isSeller;
        this.followers = followers;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Boolean getIsSeller() {return isSeller;}

    public List<UserSummaryDto> getFollowers() {
        return followers;
    }
}
