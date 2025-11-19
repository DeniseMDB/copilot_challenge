package javasharks.excusas_challenge.dto;

// DTO para respuesta role-specific: excusa textual + meme (string)
public record SimpleExcuseWithMemeDTO(
    ExcusaTextDTO excusa,
    String meme
) {}
