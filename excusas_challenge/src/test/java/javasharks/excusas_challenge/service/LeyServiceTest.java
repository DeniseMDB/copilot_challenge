package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.LeyDTO;
import javasharks.excusas_challenge.model.Ley;
import javasharks.excusas_challenge.repository.LeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeyServiceTest {

    @Mock
    private LeyRepository leyRepository;

    @InjectMocks
    private LeyService leyService;

    private Ley ley;

    @BeforeEach
    void setUp() {
        ley = new Ley(1L, "Fuente de ejemplo", "Texto de ejemplo", "dev");
    }

    @Test
    @DisplayName("findAll should return a list of LeyDTOs")
    void findAll_ShouldReturnListOfLeyDTOs() {
        when(leyRepository.findAll()).thenReturn(List.of(ley));

        List<LeyDTO> result = leyService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Texto de ejemplo", result.get(0).texto());
        verify(leyRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById should return LeyDTO when ID exists")
    void findById_ShouldReturnLeyDTO_WhenIdExists() {
        when(leyRepository.findById(1L)).thenReturn(Optional.of(ley));

        Optional<LeyDTO> result = leyService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Texto de ejemplo", result.get().texto());
        verify(leyRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save should persist and return the saved LeyDTO")
    void save_ShouldPersistAndReturnSavedLeyDTO() {
        when(leyRepository.save(ley)).thenReturn(ley);

        LeyDTO result = leyService.save(ley);

        assertNotNull(result);
        assertEquals("Texto de ejemplo", result.texto());
        verify(leyRepository, times(1)).save(ley);
    }

    @Test
    @DisplayName("delete should remove the Ley by ID")
    void delete_ShouldRemoveLeyById() {
        doNothing().when(leyRepository).deleteById(1L);

        leyService.delete(1L);

        verify(leyRepository, times(1)).deleteById(1L);
    }
}