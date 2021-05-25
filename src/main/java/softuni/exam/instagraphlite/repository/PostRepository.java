package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entities.Post;

import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Set<Post> findAllByUser_Id(int id);
}
