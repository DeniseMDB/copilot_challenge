package javasharks.excusas_challenge.repository;

import javasharks.excusas_challenge.model.Ley;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeyRepository extends JpaRepository<Ley, Long> {
	java.util.List<Ley> findByRole(String role);
}
