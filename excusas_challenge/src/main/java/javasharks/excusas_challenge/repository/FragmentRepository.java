package javasharks.excusas_challenge.repository;

import javasharks.excusas_challenge.model.Fragment;
import javasharks.excusas_challenge.model.FragmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FragmentRepository extends JpaRepository<Fragment, Long> {
    List<Fragment> findByTipo(FragmentType tipo);
    List<Fragment> findByTipoAndRole(FragmentType tipo, String role);
}
