package javasharks.excusas_challenge.service;

import javasharks.excusas_challenge.dto.RolDTO;
import javasharks.excusas_challenge.model.Rol;
import javasharks.excusas_challenge.repository.RolRepository;
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
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "dev");
    }

    @Test
    @DisplayName("findAll should return a list of RolDTOs")
    void findAll_ShouldReturnListOfRolDTOs() {
        when(rolRepository.findAll()).thenReturn(List.of(rol));

        List<RolDTO> result = rolService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("dev", result.get(0).nombre());
        verify(rolRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById should return RolDTO when ID exists")
    void findById_ShouldReturnRolDTO_WhenIdExists() {
        when(rolRepository.findById(1L)).thenReturn(Optional.of(rol));

        Optional<RolDTO> result = rolService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("dev", result.get().nombre());
        verify(rolRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById should return empty Optional when ID does not exist")
    void findById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {
        when(rolRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<RolDTO> result = rolService.findById(2L);

        assertFalse(result.isPresent());
        verify(rolRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("findByNombre should return RolDTO when name exists")
    void findByNombre_ShouldReturnRolDTO_WhenNameExists() {
        when(rolRepository.findByNombre("dev")).thenReturn(Optional.of(rol));

        Optional<RolDTO> result = rolService.findByNombre("dev");

        assertTrue(result.isPresent());
        assertEquals("dev", result.get().nombre());
        verify(rolRepository, times(1)).findByNombre("dev");
    }

    @Test
    @DisplayName("findByNombre should return empty Optional when name does not exist")
    void findByNombre_ShouldReturnEmptyOptional_WhenNameDoesNotExist() {
        when(rolRepository.findByNombre("qa")).thenReturn(Optional.empty());

        Optional<RolDTO> result = rolService.findByNombre("qa");

        assertFalse(result.isPresent());
        verify(rolRepository, times(1)).findByNombre("qa");
    }

    @Test
    @DisplayName("save should persist and return the saved RolDTO")
    void save_ShouldPersistAndReturnSavedRolDTO() {
        when(rolRepository.save(rol)).thenReturn(rol);

        RolDTO result = rolService.save(rol);

        assertNotNull(result);
        assertEquals("dev", result.nombre());
        verify(rolRepository, times(1)).save(rol);
    }

    @Test
    @DisplayName("delete should remove the Rol by ID")
    void delete_ShouldRemoveRolById() {
        doNothing().when(rolRepository).deleteById(1L);

        rolService.delete(1L);

        verify(rolRepository, times(1)).deleteById(1L);
    }
}