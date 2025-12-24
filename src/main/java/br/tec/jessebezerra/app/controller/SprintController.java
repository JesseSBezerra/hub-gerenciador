package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.SprintRequestDTO;
import br.tec.jessebezerra.app.dto.SprintResponseDTO;
import br.tec.jessebezerra.app.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;

    @PostMapping
    public ResponseEntity<SprintResponseDTO> create(@RequestBody SprintRequestDTO dto) {
        SprintResponseDTO savedSprint = sprintService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSprint);
    }

    @GetMapping
    public ResponseEntity<List<SprintResponseDTO>> findAll() {
        List<SprintResponseDTO> sprints = sprintService.findAll();
        return ResponseEntity.ok(sprints);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SprintResponseDTO> findById(@PathVariable Long id) {
        return sprintService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SprintResponseDTO> update(@PathVariable Long id, @RequestBody SprintRequestDTO dto) {
        try {
            SprintResponseDTO updatedSprint = sprintService.update(id, dto);
            return ResponseEntity.ok(updatedSprint);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sprintService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
