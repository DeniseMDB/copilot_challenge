package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.FragmentDTO;
import javasharks.excusas_challenge.model.Fragment;
import javasharks.excusas_challenge.model.FragmentType;
import javasharks.excusas_challenge.repository.FragmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FragmentService {

    private final FragmentRepository fragmentRepository;

    public FragmentService(FragmentRepository fragmentRepository) {
        this.fragmentRepository = fragmentRepository;
    }

    public List<FragmentDTO> findAll() {
        return fragmentRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<FragmentDTO> findByTipo(FragmentType tipo) {
        return fragmentRepository.findByTipo(tipo)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<FragmentDTO> findById(Long id) {
        return fragmentRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public FragmentDTO save(Fragment fragment) {
        Fragment saved = fragmentRepository.save(fragment);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        fragmentRepository.deleteById(id);
    }

    // Convierte un Fragment a su DTO completo
    private FragmentDTO toDto(Fragment fragment) {
        if (fragment == null) return null;
        return new FragmentDTO(
            fragment.getId(),
            fragment.getTipo(),
            fragment.getTexto()
        );
    }
}
