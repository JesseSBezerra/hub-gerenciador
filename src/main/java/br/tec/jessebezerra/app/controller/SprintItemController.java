package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.SprintItemRequestDTO;
import br.tec.jessebezerra.app.dto.SprintItemResponseDTO;
import br.tec.jessebezerra.app.service.SprintItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprint-items")
@RequiredArgsConstructor
public class SprintItemController {

    private final SprintItemService sprintItemService;

    @PostMapping
    public ResponseEntity<SprintItemResponseDTO> addTarefaToSprint(@RequestBody SprintItemRequestDTO dto) {
        try {
            SprintItemResponseDTO sprintItem = sprintItemService.addTarefaToSprint(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(sprintItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTarefaFromSprint(@PathVariable Long id) {
        sprintItemService.removeTarefaFromSprint(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<List<SprintItemResponseDTO>> findBySprint(@PathVariable Long sprintId) {
        List<SprintItemResponseDTO> items = sprintItemService.findBySprint(sprintId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<SprintItemResponseDTO>> findByTarefa(@PathVariable Long tarefaId) {
        List<SprintItemResponseDTO> items = sprintItemService.findByTarefa(tarefaId);
        return ResponseEntity.ok(items);
    }

    @GetMapping
    public ResponseEntity<List<SprintItemResponseDTO>> findAll() {
        List<SprintItemResponseDTO> items = sprintItemService.findAll();
        return ResponseEntity.ok(items);
    }
}
