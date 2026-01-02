package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.TarefaQuestionarioRequestDTO;
import br.tec.jessebezerra.app.dto.TarefaQuestionarioResponseDTO;
import br.tec.jessebezerra.app.service.TarefaQuestionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefa-questionarios")
@RequiredArgsConstructor
public class TarefaQuestionarioController {

    private final TarefaQuestionarioService tarefaQuestionarioService;

    @PostMapping
    public ResponseEntity<TarefaQuestionarioResponseDTO> create(@RequestBody TarefaQuestionarioRequestDTO dto) {
        TarefaQuestionarioResponseDTO saved = tarefaQuestionarioService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<TarefaQuestionarioResponseDTO>> getAll() {
        List<TarefaQuestionarioResponseDTO> questionarios = tarefaQuestionarioService.findAll();
        return ResponseEntity.ok(questionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaQuestionarioResponseDTO> getById(@PathVariable Long id) {
        TarefaQuestionarioResponseDTO questionario = tarefaQuestionarioService.findById(id);
        return ResponseEntity.ok(questionario);
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<TarefaQuestionarioResponseDTO>> getByTarefaId(@PathVariable Long tarefaId) {
        List<TarefaQuestionarioResponseDTO> questionarios = tarefaQuestionarioService.findByTarefaId(tarefaId);
        return ResponseEntity.ok(questionarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaQuestionarioResponseDTO> update(@PathVariable Long id, @RequestBody TarefaQuestionarioRequestDTO dto) {
        TarefaQuestionarioResponseDTO updated = tarefaQuestionarioService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tarefaQuestionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
