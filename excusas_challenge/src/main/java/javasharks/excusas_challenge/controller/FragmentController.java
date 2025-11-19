package javasharks.excusas_challenge.controller;

import javasharks.excusas_challenge.dto.FragmentDTO;
import javasharks.excusas_challenge.model.Fragment;
import javasharks.excusas_challenge.model.FragmentType;
import javasharks.excusas_challenge.service.FragmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FragmentController {

    private final FragmentService fragmentService;

    public FragmentController(FragmentService fragmentService) {
        this.fragmentService = fragmentService;
    }

    @PostMapping("/fragments")
    public ResponseEntity<FragmentDTO> create(@RequestBody FragmentDTO dto) {
        Fragment f = new Fragment();
        f.setTexto(dto.texto());
        FragmentDTO saved = fragmentService.save(f);
        return ResponseEntity.created(URI.create("/fragments/")).body(saved);
    }

    @GetMapping("/fragments")
    public ResponseEntity<List<FragmentDTO>> list(@RequestParam(name = "tipo", required = false) String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return ResponseEntity.ok(fragmentService.findAll());
        }
        FragmentType ft = FragmentType.valueOf(tipo.toUpperCase());
        return ResponseEntity.ok(fragmentService.findByTipo(ft));
    }

    @GetMapping("/fragments/{id}")
    public ResponseEntity<FragmentDTO> get(@PathVariable Long id) {
        return fragmentService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/fragments/{id}")
    public ResponseEntity<FragmentDTO> update(@PathVariable Long id, @RequestBody FragmentDTO dto) {
        // simple upsert: set id and save
        Fragment f = new Fragment();
        f.setId(id);
        f.setTexto(dto.texto());
        FragmentDTO saved = fragmentService.save(f);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/fragments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fragmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
