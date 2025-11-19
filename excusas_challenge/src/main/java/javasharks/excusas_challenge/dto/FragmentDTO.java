package javasharks.excusas_challenge.dto;

import javasharks.excusas_challenge.model.FragmentType;

// DTO completo para Fragment
public record FragmentDTO(Long id, FragmentType tipo, String texto) {
}
