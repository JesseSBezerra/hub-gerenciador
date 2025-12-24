package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.TarefaRequestDTO;
import br.tec.jessebezerra.app.dto.TarefaResponseDTO;
import br.tec.jessebezerra.app.model.enums.Tipo;
import br.tec.jessebezerra.app.service.TarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> create(@RequestBody TarefaRequestDTO dto) {
        TarefaResponseDTO savedTarefa = tarefaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTarefa);
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> findAll(@RequestParam(required = false) Tipo tipo) {
        List<TarefaResponseDTO> tarefas = tarefaService.findAll(tipo);
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> findById(@PathVariable Long id) {
        try {
            TarefaResponseDTO tarefa = tarefaService.findById(id);
            return ResponseEntity.ok(tarefa);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> update(@PathVariable Long id, @RequestBody TarefaRequestDTO dto) {
        try {
            TarefaResponseDTO updatedTarefa = tarefaService.update(id, dto);
            return ResponseEntity.ok(updatedTarefa);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tarefaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
