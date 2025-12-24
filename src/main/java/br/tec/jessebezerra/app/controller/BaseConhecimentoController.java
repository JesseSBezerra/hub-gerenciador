package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.BaseConhecimentoRequestDTO;
import br.tec.jessebezerra.app.dto.BaseConhecimentoResponseDTO;
import br.tec.jessebezerra.app.service.BaseConhecimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base-conhecimento")
@RequiredArgsConstructor
public class BaseConhecimentoController {

    private final BaseConhecimentoService baseConhecimentoService;

    @PostMapping
    public ResponseEntity<BaseConhecimentoResponseDTO> create(@RequestBody BaseConhecimentoRequestDTO dto) {
        BaseConhecimentoResponseDTO saved = baseConhecimentoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<BaseConhecimentoResponseDTO>> findAll() {
        List<BaseConhecimentoResponseDTO> list = baseConhecimentoService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseConhecimentoResponseDTO> findById(@PathVariable Long id) {
        try {
            BaseConhecimentoResponseDTO dto = baseConhecimentoService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseConhecimentoResponseDTO> update(@PathVariable Long id, @RequestBody BaseConhecimentoRequestDTO dto) {
        try {
            BaseConhecimentoResponseDTO updated = baseConhecimentoService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        baseConhecimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
