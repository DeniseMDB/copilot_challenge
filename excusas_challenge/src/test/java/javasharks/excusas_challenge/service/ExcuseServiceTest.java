package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.SimpleExcuseDTO;
import javasharks.excusas_challenge.dto.SimpleExcuseUltraDTO;
import javasharks.excusas_challenge.dto.SimpleExcuseWithMemeDTO;
import javasharks.excusas_challenge.model.Fragment;
import javasharks.excusas_challenge.model.FragmentType;
import javasharks.excusas_challenge.model.Ley;
import javasharks.excusas_challenge.model.Meme;
import javasharks.excusas_challenge.repository.FragmentRepository;
import javasharks.excusas_challenge.repository.LeyRepository;
import javasharks.excusas_challenge.repository.MemeRepository;
import javasharks.excusas_challenge.repository.RolRepository;
import javasharks.excusas_challenge.model.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ExcuseServiceTest {

    @Mock
    FragmentRepository fragmentRepository;

    @Mock
    LeyRepository leyRepository;

    @Mock
    RolRepository rolRepository;

    @Mock
    MemeRepository memeRepository;

    @InjectMocks
    ExcuseService excuseService;

    @BeforeEach
    void setUp() {
        // prepare fragments
        Fragment f1 = Fragment.builder().id(1L).tipo(FragmentType.CONTEXTO).texto("Estábamos deployando un hotfix").build();
        Fragment f2 = Fragment.builder().id(2L).tipo(FragmentType.CAUSA).texto("el token de CI/CD venció").build();
        Fragment f3 = Fragment.builder().id(3L).tipo(FragmentType.CONSECUENCIA).texto("tuvimos que hacer rollback").build();
        Fragment f4 = Fragment.builder().id(4L).tipo(FragmentType.RECOMENDACION).texto("automatizar la rotación de secretos").build();

        when(fragmentRepository.findByTipo(FragmentType.CONTEXTO)).thenReturn(List.of(f1));
        when(fragmentRepository.findByTipo(FragmentType.CAUSA)).thenReturn(List.of(f2));
        when(fragmentRepository.findByTipo(FragmentType.CONSECUENCIA)).thenReturn(List.of(f3));
        when(fragmentRepository.findByTipo(FragmentType.RECOMENDACION)).thenReturn(List.of(f4));

        // register roles used in tests so generateByRole validates
        when(rolRepository.findByNombre("dev")).thenReturn(java.util.Optional.of(Rol.builder().id(1L).nombre("dev").build()));
        when(rolRepository.findByNombre("qa")).thenReturn(java.util.Optional.of(Rol.builder().id(2L).nombre("qa").build()));

    // memes and leyes are not used by the simplified service output
    }

    @Test
    @DisplayName("generateUltra uses role-specific memes and leyes when provided")
    void generateUltra_filtersByRoleWhenAvailable() {
        // prepare role-specific meme and ley
        var memeRepo = mock(javasharks.excusas_challenge.repository.MemeRepository.class);
        var leyRepo = mock(javasharks.excusas_challenge.repository.LeyRepository.class);

        var mDev = javasharks.excusas_challenge.model.Meme.builder().id(10L).nombre("meme-dev").referencia("r1").role("dev").build();
        var lDev = javasharks.excusas_challenge.model.Ley.builder().id(20L).fuente("Fuente").texto("texto").role("dev").build();

        when(memeRepo.findByRole("dev")).thenReturn(List.of(mDev));
        when(leyRepo.findByRole("dev")).thenReturn(List.of(lDev));

        // inject optional repos
        excuseService.setOptionalRepositories(memeRepo, leyRepo);

        var ultra = excuseService.generateUltra("dev");
        assertNotNull(ultra);
        assertNotNull(ultra.meme());
        assertNotNull(ultra.ley());
    assertEquals("meme-dev", ultra.meme());
    assertEquals("Fuente", ultra.ley());
    }

    @Test
    @DisplayName("generateRandom with seed should be reproducible")
    void generateRandom_seedIsReproducible() {
        long seed = 12345L;

    SimpleExcuseDTO e1 = excuseService.generateRandom(seed);
    SimpleExcuseDTO e2 = excuseService.generateRandom(seed);

        assertNotNull(e1);
        assertNotNull(e2);
        assertEquals(e1, e2, "Excuse should be identical for the same seed");
    }

    @Test
    @DisplayName("generateByRole should vary by role (different outputs for different roles)")
    void generateByRole_differentRolesProduceDifferentExcuses() {
        long seed = 42L;

    SimpleExcuseDTO dev = excuseService.generateByRole("dev", seed);
    SimpleExcuseDTO qa = excuseService.generateByRole("qa", seed);

        assertNotNull(dev);
        assertNotNull(qa);
        // They may be equal by chance, but with small seeded lists they should differ
        assertNotEquals(dev, qa, "Different roles should produce different excuses (seed derived)");
    }

    @Test
    @DisplayName("generateDaily is deterministic for the same day")
    void generateDaily_isDeterministic() {
    SimpleExcuseDTO a = excuseService.generateDaily();
    SimpleExcuseDTO b = excuseService.generateDaily();
        assertEquals(a, b, "Daily excuse should be deterministic during the same day");
    }

    @Test
    void testGenerateRoleWithMeme_NoRole() {
        SimpleExcuseWithMemeDTO result = excuseService.generateRoleWithMeme(null);
        assertNotNull(result);
        assertNotNull(result.excusa());
        assertNull(result.meme());
    }

    @Test
    void testGenerateRoleWithMeme_InvalidRole() {
        when(rolRepository.findByNombre("invalid"))
                .thenReturn(Optional.empty());

        SimpleExcuseWithMemeDTO result = excuseService.generateRoleWithMeme("invalid");
        assertNotNull(result);
        assertNotNull(result.excusa());
        assertNull(result.meme());
    }

    @Test
    void testGenerateRoleWithMeme_ValidRoleWithMeme() {
        when(rolRepository.findByNombre("valid"))
                .thenReturn(Optional.of(new Rol(1L, "valid")));
        when(memeRepository.findByRole("valid"))
                .thenReturn(Collections.singletonList(new javasharks.excusas_challenge.model.Meme(1L, "Funny Meme", null, "valid")));

        SimpleExcuseWithMemeDTO result = excuseService.generateRoleWithMeme("valid");
        assertNotNull(result);
        assertNotNull(result.excusa());
        assertEquals("Funny Meme", result.meme());
    }

    @Test
    void testGenerateRoleWithMeme_ValidRoleNoMeme() {
        when(rolRepository.findByNombre("valid"))
                .thenReturn(Optional.of(new Rol(1L, "valid")));
        when(memeRepository.findByRole("valid"))
                .thenReturn(Collections.emptyList());

        SimpleExcuseWithMemeDTO result = excuseService.generateRoleWithMeme("valid");
        assertNotNull(result);
        assertNotNull(result.excusa());
        assertNull(result.meme());
    }

    @Test
    void testGenerateUltra_NoRole() {
        when(fragmentRepository.findByTipo(FragmentType.CONTEXTO))
                .thenReturn(List.of(Fragment.builder().id(1L).tipo(FragmentType.CONTEXTO).texto("Contexto").build()));
        when(fragmentRepository.findByTipo(FragmentType.CAUSA))
                .thenReturn(List.of(Fragment.builder().id(2L).tipo(FragmentType.CAUSA).texto("Causa").build()));
        when(fragmentRepository.findByTipo(FragmentType.CONSECUENCIA))
                .thenReturn(List.of(Fragment.builder().id(3L).tipo(FragmentType.CONSECUENCIA).texto("Consecuencia").build()));
        when(fragmentRepository.findByTipo(FragmentType.RECOMENDACION))
                .thenReturn(List.of(Fragment.builder().id(4L).tipo(FragmentType.RECOMENDACION).texto("Recomendación").build()));

        when(memeRepository.findAll())
                .thenReturn(List.of(Meme.builder().id(1L).nombre("Funny Meme").build()));
        when(leyRepository.findAll())
                .thenReturn(List.of(Ley.builder().id(1L).fuente("Ley Fuente").build()));

        SimpleExcuseUltraDTO result = excuseService.generateUltra();

        assertNotNull(result);
        assertNotNull(result.excusa());
        assertEquals("Funny Meme", result.meme());
        assertEquals("Ley Fuente", result.ley());
    }

    @Test
    void testGenerateUltra_WithRole() {
        when(fragmentRepository.findByTipo(FragmentType.CONTEXTO))
                .thenReturn(List.of(Fragment.builder().id(1L).tipo(FragmentType.CONTEXTO).texto("Contexto").build()));
        when(fragmentRepository.findByTipo(FragmentType.CAUSA))
                .thenReturn(List.of(Fragment.builder().id(2L).tipo(FragmentType.CAUSA).texto("Causa").build()));
        when(fragmentRepository.findByTipo(FragmentType.CONSECUENCIA))
                .thenReturn(List.of(Fragment.builder().id(3L).tipo(FragmentType.CONSECUENCIA).texto("Consecuencia").build()));
        when(fragmentRepository.findByTipo(FragmentType.RECOMENDACION))
                .thenReturn(List.of(Fragment.builder().id(4L).tipo(FragmentType.RECOMENDACION).texto("Recomendación").build()));

        when(memeRepository.findByRole("dev"))
                .thenReturn(List.of(Meme.builder().id(1L).nombre("Dev Meme").role("dev").build()));
        when(leyRepository.findByRole("dev"))
                .thenReturn(List.of(Ley.builder().id(1L).fuente("Dev Ley").role("dev").build()));

        SimpleExcuseUltraDTO result = excuseService.generateUltra("dev");

        assertNotNull(result);
        assertNotNull(result.excusa());
        assertEquals("Dev Meme", result.meme());
        assertEquals("Dev Ley", result.ley());
    }

    @Test
    void testGenerateUltra_WithRole_NoMemeOrLey() {
        when(fragmentRepository.findByTipo(FragmentType.CONTEXTO))
                .thenReturn(List.of(Fragment.builder().id(1L).tipo(FragmentType.CONTEXTO).texto("Contexto").build()));
        when(fragmentRepository.findByTipo(FragmentType.CAUSA))
                .thenReturn(List.of(Fragment.builder().id(2L).tipo(FragmentType.CAUSA).texto("Causa").build()));
        when(fragmentRepository.findByTipo(FragmentType.CONSECUENCIA))
                .thenReturn(List.of(Fragment.builder().id(3L).tipo(FragmentType.CONSECUENCIA).texto("Consecuencia").build()));
        when(fragmentRepository.findByTipo(FragmentType.RECOMENDACION))
                .thenReturn(List.of(Fragment.builder().id(4L).tipo(FragmentType.RECOMENDACION).texto("Recomendación").build()));

        when(memeRepository.findByRole("dev"))
                .thenReturn(Collections.emptyList());
        when(leyRepository.findByRole("dev"))
                .thenReturn(Collections.emptyList());

        SimpleExcuseUltraDTO result = excuseService.generateUltra("dev");

        assertNotNull(result);
        assertNotNull(result.excusa());
        assertNull(result.meme());
        assertNull(result.ley());
    }
}
