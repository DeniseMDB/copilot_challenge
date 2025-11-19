package javasharks.excusas_challenge.dto;

public record ExcuseDTO(
        FragmentDTO contexto,
        FragmentDTO causa,
        FragmentDTO consecuencia,
        FragmentDTO recomendacion,
        MemeDTO meme,
        LeyDTO ley
) {
}
