package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entities.Picture;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
    Optional<Picture> findByPath(String path);

    Set<Picture> findAllBySizeIsGreaterThanOrderBySizeAsc(double size);
}
