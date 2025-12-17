package meli.socialmeli.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // user_id

    @Column(name = "user_name", length = 15, nullable = false, unique = true)
    @NotBlank
    @Size(max = 15)
    private String userName;

    @ManyToMany
    @JoinTable(
            name = "user_follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "seller_id")
    )
    private Set<User> followings = new HashSet<>();

    @ManyToMany(mappedBy = "followings")
    private Set<User> followers = new HashSet<>();

    public User() {}

    public User(Integer id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public Set<User> getFollowings() {
        return followings;
    }

    public void setFollowings(Set<User> followings) {
        this.followings = followings;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
