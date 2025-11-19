package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.FragmentDTO;
import javasharks.excusas_challenge.model.Fragment;
import javasharks.excusas_challenge.model.FragmentType;
import javasharks.excusas_challenge.repository.FragmentRepository;
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
class FragmentServiceTest {

    @Mock
    private FragmentRepository fragmentRepository;

    @InjectMocks
    private FragmentService fragmentService;

    private Fragment fragment;

    @BeforeEach
    void setUp() {
        fragment = new Fragment(1L, FragmentType.CONTEXTO, "Texto de ejemplo", "dev");
    }

    @Test
    @DisplayName("findAll should return a list of FragmentDTOs")
    void findAll_ShouldReturnListOfFragmentDTOs() {
        when(fragmentRepository.findAll()).thenReturn(List.of(fragment));

        List<FragmentDTO> result = fragmentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Texto de ejemplo", result.get(0).texto());
        verify(fragmentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findByTipo should return a list of FragmentDTOs for a specific type")
    void findByTipo_ShouldReturnListOfFragmentDTOs() {
        when(fragmentRepository.findByTipo(FragmentType.CONTEXTO)).thenReturn(List.of(fragment));

        List<FragmentDTO> result = fragmentService.findByTipo(FragmentType.CONTEXTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(FragmentType.CONTEXTO, result.get(0).tipo());
        verify(fragmentRepository, times(1)).findByTipo(FragmentType.CONTEXTO);
    }

    @Test
    @DisplayName("findById should return FragmentDTO when ID exists")
    void findById_ShouldReturnFragmentDTO_WhenIdExists() {
        when(fragmentRepository.findById(1L)).thenReturn(Optional.of(fragment));

        Optional<FragmentDTO> result = fragmentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Texto de ejemplo", result.get().texto());
        verify(fragmentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("save should persist and return the saved FragmentDTO")
    void save_ShouldPersistAndReturnSavedFragmentDTO() {
        when(fragmentRepository.save(fragment)).thenReturn(fragment);

        FragmentDTO result = fragmentService.save(fragment);

        assertNotNull(result);
        assertEquals("Texto de ejemplo", result.texto());
        verify(fragmentRepository, times(1)).save(fragment);
    }

    @Test
    @DisplayName("delete should remove the Fragment by ID")
    void delete_ShouldRemoveFragmentById() {
        doNothing().when(fragmentRepository).deleteById(1L);

        fragmentService.delete(1L);

        verify(fragmentRepository, times(1)).deleteById(1L);
    }
}