package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.ProjetoRequestDTO;
import br.tec.jessebezerra.app.dto.ProjetoResponseDTO;
import br.tec.jessebezerra.app.service.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> create(@RequestBody ProjetoRequestDTO dto) {
        ProjetoResponseDTO saved = projetoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ProjetoResponseDTO>> getAll() {
        List<ProjetoResponseDTO> projetos = projetoService.findAll();
        return ResponseEntity.ok(projetos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> getById(@PathVariable Long id) {
        ProjetoResponseDTO projeto = projetoService.findById(id);
        return ResponseEntity.ok(projeto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> update(@PathVariable Long id, @RequestBody ProjetoRequestDTO dto) {
        ProjetoResponseDTO updated = projetoService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projetoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
