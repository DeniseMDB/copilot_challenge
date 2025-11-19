package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.MemeDTO;
import javasharks.excusas_challenge.model.Meme;
import javasharks.excusas_challenge.repository.MemeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemeService {

    private final MemeRepository memeRepository;

    public MemeService(MemeRepository memeRepository) {
        this.memeRepository = memeRepository;
    }

    public List<MemeDTO> findAll() {
    return memeRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<MemeDTO> findById(Long id) {
        return memeRepository.findById(id).map(this::toDto);
    }

    @Transactional
    public MemeDTO save(Meme meme) {
        Meme saved = memeRepository.save(meme);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        memeRepository.deleteById(id);
    }

    // Convierte un Meme a su DTO completo
    private MemeDTO toDto(Meme meme) {
        if (meme == null) return null;
        return new MemeDTO(
            meme.getId(),
            meme.getNombre(),
            meme.getReferencia(),
            meme.getRole()
        );
    }
}
