package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.DetalhesProjetoRequestDTO;
import br.tec.jessebezerra.app.dto.DetalhesProjetoResponseDTO;
import br.tec.jessebezerra.app.service.DetalhesProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalhes-projeto")
@RequiredArgsConstructor
public class DetalhesProjetoController {

    private final DetalhesProjetoService detalhesProjetoService;

    @PostMapping
    public ResponseEntity<DetalhesProjetoResponseDTO> create(@RequestBody DetalhesProjetoRequestDTO dto) {
        DetalhesProjetoResponseDTO saved = detalhesProjetoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<DetalhesProjetoResponseDTO>> getAll() {
        List<DetalhesProjetoResponseDTO> detalhes = detalhesProjetoService.findAll();
        return ResponseEntity.ok(detalhes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesProjetoResponseDTO> getById(@PathVariable Long id) {
        DetalhesProjetoResponseDTO detalhe = detalhesProjetoService.findById(id);
        return ResponseEntity.ok(detalhe);
    }

    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<DetalhesProjetoResponseDTO>> getByProjetoId(@PathVariable Long projetoId) {
        List<DetalhesProjetoResponseDTO> detalhes = detalhesProjetoService.findByProjetoId(projetoId);
        return ResponseEntity.ok(detalhes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalhesProjetoResponseDTO> update(@PathVariable Long id, @RequestBody DetalhesProjetoRequestDTO dto) {
        DetalhesProjetoResponseDTO updated = detalhesProjetoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        detalhesProjetoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
