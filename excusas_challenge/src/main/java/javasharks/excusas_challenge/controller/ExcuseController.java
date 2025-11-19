package javasharks.excusas_challenge.controller;

import javasharks.excusas_challenge.dto.SimpleExcuseDTO;
import javasharks.excusas_challenge.dto.SimpleExcuseUltraDTO;
import javasharks.excusas_challenge.service.ExcuseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excuses")
public class ExcuseController {

    private final ExcuseService excuseService;

    public ExcuseController(ExcuseService excuseService) {
        this.excuseService = excuseService;
    }

    @GetMapping("/random")
    public ResponseEntity<SimpleExcuseDTO> random(@RequestParam(name = "seed", required = false) Long seed) {
        return ResponseEntity.ok(excuseService.generateRandom(seed));
    }

    @GetMapping("/roles/{rol}")
    public ResponseEntity<javasharks.excusas_challenge.dto.SimpleExcuseWithMemeDTO> byRole(@PathVariable("rol") String rol, @RequestParam(name = "seed", required = false) Long seed) {
        // Return an excusa textual + meme for the given role (no ley)
        return ResponseEntity.ok(excuseService.generateRoleWithMeme(rol));
    }

    @GetMapping("/daily")
    public ResponseEntity<SimpleExcuseDTO> daily() {
        return ResponseEntity.ok(excuseService.generateDaily());
    }

    @GetMapping("/ultra")
    public ResponseEntity<SimpleExcuseUltraDTO> ultra() {
        return ResponseEntity.ok(excuseService.generateUltra());
    }
}
