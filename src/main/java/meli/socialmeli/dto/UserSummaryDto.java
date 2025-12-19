package meli.socialmeli.dto;

public class UserSummaryDto {
    private Integer userId;
    private String userName;

    public UserSummaryDto(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
