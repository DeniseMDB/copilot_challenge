package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.MemeDTO;
import javasharks.excusas_challenge.model.Meme;
import javasharks.excusas_challenge.repository.MemeRepository;
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
class MemeServiceTest {

    @Mock
    private MemeRepository memeRepository;

    @InjectMocks
    private MemeService memeService;

    private Meme meme;

    @BeforeEach
    void setUp() {
        meme = new Meme(1L, "Meme divertido", "https://example.com/meme", "dev");
    }

    @Test
    @DisplayName("findAll should return a list of MemeDTOs")
    void findAll_ShouldReturnListOfMemeDTOs() {
        when(memeRepository.findAll()).thenReturn(List.of(meme));

        List<MemeDTO> result = memeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Meme divertido", result.get(0).nombre());
        verify(memeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById should return MemeDTO when ID exists")
    void findById_ShouldReturnMemeDTO_WhenIdExists() {
        when(memeRepository.findById(1L)).thenReturn(Optional.of(meme));

        Optional<MemeDTO> result = memeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Meme divertido", result.get().nombre());
        verify(memeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save should persist and return the saved MemeDTO")
    void save_ShouldPersistAndReturnSavedMemeDTO() {
        when(memeRepository.save(meme)).thenReturn(meme);

        MemeDTO result = memeService.save(meme);

        assertNotNull(result);
        assertEquals("Meme divertido", result.nombre());
        verify(memeRepository, times(1)).save(meme);
    }

    @Test
    @DisplayName("delete should remove the Meme by ID")
    void delete_ShouldRemoveMemeById() {
        doNothing().when(memeRepository).deleteById(1L);

        memeService.delete(1L);

        verify(memeRepository, times(1)).deleteById(1L);
    }
}