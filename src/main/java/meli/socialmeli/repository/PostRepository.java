package meli.socialmeli.repository;

import meli.socialmeli.model.Post;
import meli.socialmeli.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findBySellerIn(Collection<User> sellers);

}
