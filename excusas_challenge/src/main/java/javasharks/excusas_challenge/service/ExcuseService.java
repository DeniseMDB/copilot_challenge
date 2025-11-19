package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.SimpleExcuseDTO;
import javasharks.excusas_challenge.dto.SimpleExcuseUltraDTO;
import javasharks.excusas_challenge.dto.ExcusaTextDTO;
import javasharks.excusas_challenge.dto.SimpleExcuseWithMemeDTO;
import javasharks.excusas_challenge.model.Fragment;
import javasharks.excusas_challenge.model.FragmentType;
import javasharks.excusas_challenge.model.Meme;
import javasharks.excusas_challenge.model.Ley;
import javasharks.excusas_challenge.repository.FragmentRepository;
import javasharks.excusas_challenge.repository.MemeRepository;
import javasharks.excusas_challenge.repository.LeyRepository;
import javasharks.excusas_challenge.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional(readOnly = true)
public class ExcuseService {

    private final FragmentRepository fragmentRepository;
    private final RolRepository rolRepository;

    // repositorios opcionales: no romperán tests que mockean sólo fragmentRepository
    private MemeRepository memeRepository;
    private LeyRepository leyRepository;

    public ExcuseService(FragmentRepository fragmentRepository, RolRepository rolRepository) {
        this.fragmentRepository = fragmentRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Genera una excusa filtrada por rol y devuelve la excusa textual junto con
     * un meme (texto) asociado al rol. No incluye la ley.
     */
    public SimpleExcuseWithMemeDTO generateRoleWithMeme(String role) {
        SimpleExcuseDTO simpleExcuse = generateRandom(null);
        ExcusaTextDTO excusaText = new ExcusaTextDTO(simpleExcuse.contexto(), simpleExcuse.causa(), simpleExcuse.consecuencia(), simpleExcuse.recomendacion());

        if (role == null || role.isBlank()) {
            return new SimpleExcuseWithMemeDTO(excusaText, null);
        }

        String normalizedRole = role.toLowerCase();
        if (rolRepository.findByNombre(normalizedRole).isEmpty()) {
            return new SimpleExcuseWithMemeDTO(excusaText, null);
        }

        String memeStr = null;
        if (memeRepository != null) {
            List<Meme> memes = memeRepository.findByRole(normalizedRole);
            memeStr = pickRandom(memes, randomFromSeed(null)).map(Meme::getNombre).orElse(null);
        }

        return new SimpleExcuseWithMemeDTO(excusaText, memeStr);
    }

    // setter de inyección opcional para Spring (si están disponibles)
    @Autowired(required = false)
    public void setOptionalRepositories(MemeRepository memeRepository, LeyRepository leyRepository) {
        this.memeRepository = memeRepository;
        this.leyRepository = leyRepository;
    }

    /**
     * Generate a random excuse. If seed is provided the selection is reproducible.
     */
    public SimpleExcuseDTO generateRandom(Long seed) {
        Random rnd = randomFromSeed(seed);
        return buildSimpleExcuse(rnd);
    }

    /**
     * Generate an excuse specific to a role.
     *
     * - Validates role against the set of roles used in the JSON data.
     * - Derives a deterministic seed from role + optional seed to get reproducible but different results per role.
     * - Falls back to generateRandom if role is unknown or null.
     */
    public SimpleExcuseDTO generateByRole(String role, Long seed) {
        if (role == null || role.isBlank()) {
            return generateRandom(seed);
        }
        String normalized = role.toLowerCase();
        // Validate role exists in RolRepository
        boolean exists = rolRepository.findByNombre(normalized).isPresent();
        if (!exists) {
            return generateRandom(seed);
        }
        long derived = deriveSeedForRole(normalized, seed);
        Random rnd = randomFromSeed(derived);
        Fragment contexto = pickRandom(fragmentRepository.findByTipoAndRole(FragmentType.CONTEXTO, normalized), rnd).orElse(null);
        Fragment causa = pickRandom(fragmentRepository.findByTipoAndRole(FragmentType.CAUSA, normalized), rnd).orElse(null);
        Fragment consecuencia = pickRandom(fragmentRepository.findByTipoAndRole(FragmentType.CONSECUENCIA, normalized), rnd).orElse(null);
        Fragment recomendacion = pickRandom(fragmentRepository.findByTipoAndRole(FragmentType.RECOMENDACION, normalized), rnd).orElse(null);
        return new SimpleExcuseDTO(textoOf(contexto), textoOf(causa), textoOf(consecuencia), textoOf(recomendacion));
    }

    /**
     * Generate a daily deterministic excuse (changes every day).
     */
    public SimpleExcuseDTO generateDaily() {
        long daySeed = LocalDate.now().toEpochDay();
        return generateRandom(daySeed);
    }

    /**
     * Ultra mode: devuelve excusa completa con meme y ley (si existen repositorios).
     *
     * - Usa las mismas reglas de selección que generateRandom para los fragments.
     * - Si no hay MemeRepository o LeyRepository disponibles (tests/mocks), devuelve null para esos campos.
     */

    public SimpleExcuseUltraDTO generateUltra() {
        return generateUltra((String) null);
    }

    /**
     * Ultra mode with optional role filter: if role is provided and repositories support it,
     * memes and leyes are selected from role-specific lists.
     */
    public SimpleExcuseUltraDTO generateUltra(String role) {
        Random rnd = randomFromSeed(null);
        // select fragments
        Fragment contexto = pickRandom(fragmentRepository.findByTipo(FragmentType.CONTEXTO), rnd).orElse(null);
        Fragment causa = pickRandom(fragmentRepository.findByTipo(FragmentType.CAUSA), rnd).orElse(null);
        Fragment consecuencia = pickRandom(fragmentRepository.findByTipo(FragmentType.CONSECUENCIA), rnd).orElse(null);
        Fragment recomendacion = pickRandom(fragmentRepository.findByTipo(FragmentType.RECOMENDACION), rnd).orElse(null);

        Meme meme = null;
        Ley ley = null;
        if (memeRepository != null) {
            List<Meme> memes = (role == null || role.isBlank()) ? memeRepository.findAll() : memeRepository.findByRole(role.toLowerCase());
            meme = pickRandom(memes, rnd).orElse(null);
        }
        if (leyRepository != null) {
            List<Ley> leyes = (role == null || role.isBlank()) ? leyRepository.findAll() : leyRepository.findByRole(role.toLowerCase());
            ley = pickRandom(leyes, rnd).orElse(null);
        }

        ExcusaTextDTO excusa = new ExcusaTextDTO(
            textoOf(contexto),
            textoOf(causa),
            textoOf(consecuencia),
            textoOf(recomendacion)
        );
        String memeStr = (meme == null) ? null : meme.getNombre();
        String leyStr = (ley == null) ? null : ley.getFuente();
        return new SimpleExcuseUltraDTO(excusa, memeStr, leyStr);
    }

    // --- helper refactors for modularity ---
    private long deriveSeedForRole(String normalizedRole, Long seed) {
        long base = seed == null ? 0L : seed;
        return base ^ Objects.hash(normalizedRole);
    }

    private Random randomFromSeed(Long seed) {
        return seed == null ? new Random() : new Random(seed);
    }

    private SimpleExcuseDTO buildSimpleExcuse(Random rnd) {
        Fragment contexto = pickRandom(fragmentRepository.findByTipo(FragmentType.CONTEXTO), rnd).orElse(null);
        Fragment causa = pickRandom(fragmentRepository.findByTipo(FragmentType.CAUSA), rnd).orElse(null);
        Fragment consecuencia = pickRandom(fragmentRepository.findByTipo(FragmentType.CONSECUENCIA), rnd).orElse(null);
        Fragment recomendacion = pickRandom(fragmentRepository.findByTipo(FragmentType.RECOMENDACION), rnd).orElse(null);
        return new SimpleExcuseDTO(textoOf(contexto), textoOf(causa), textoOf(consecuencia), textoOf(recomendacion));
    }

    private <T> Optional<T> pickRandom(List<T> list, Random rnd) {
        if (list == null || list.isEmpty()) return Optional.empty();
        int idx = rnd.nextInt(list.size());
        return Optional.of(list.get(idx));
    }

    private String textoOf(Fragment f) {
        return f == null ? null : f.getTexto();
    }

}
