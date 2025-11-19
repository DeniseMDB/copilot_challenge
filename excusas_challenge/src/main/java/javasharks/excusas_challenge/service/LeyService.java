package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.LeyDTO;
import javasharks.excusas_challenge.model.Ley;
import javasharks.excusas_challenge.repository.LeyRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeyService {

    private final LeyRepository leyRepository;

    public LeyService(LeyRepository leyRepository) {
        this.leyRepository = leyRepository;
    }

    public List<LeyDTO> findAll() {
    return leyRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<LeyDTO> findById(Long id) {
        return leyRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public LeyDTO save(Ley ley) {
        Ley saved = leyRepository.save(ley);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        leyRepository.deleteById(id);
    }

    // Convierte una Ley a su DTO completo
    private LeyDTO toDto(Ley ley) {
        if (ley == null) return null;
        return new LeyDTO(
            ley.getId(),
            ley.getFuente(),
            ley.getTexto(),
            ley.getRole()
        );
    }
}
