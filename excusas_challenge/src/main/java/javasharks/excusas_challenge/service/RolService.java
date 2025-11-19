package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.RolDTO;
import javasharks.excusas_challenge.model.Rol;
import javasharks.excusas_challenge.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<RolDTO> findAll() {
    return rolRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<RolDTO> findById(Long id) {
        return rolRepository.findById(id).map(this::toDto);
    }

    public Optional<RolDTO> findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre).map(this::toDto);
    }

    @Transactional
    public RolDTO save(Rol rol) {
        Rol saved = rolRepository.save(rol);
        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        rolRepository.deleteById(id);
    }

    // Convierte un Rol a su DTO completo
    private RolDTO toDto(Rol rol) {
        if (rol == null) return null;
        return new RolDTO(
            rol.getId(),
            rol.getNombre()
        );
    }
}
