package javasharks.excusas_challenge.repository;

import javasharks.excusas_challenge.model.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemeRepository extends JpaRepository<Meme, Long> {
	java.util.List<Meme> findByRole(String role);
}
