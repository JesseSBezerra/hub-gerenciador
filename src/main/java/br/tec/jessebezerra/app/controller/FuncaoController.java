package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.FuncaoRequestDTO;
import br.tec.jessebezerra.app.dto.FuncaoResponseDTO;
import br.tec.jessebezerra.app.service.FuncaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcoes")
@RequiredArgsConstructor
public class FuncaoController {

    private final FuncaoService funcaoService;

    @PostMapping
    public ResponseEntity<FuncaoResponseDTO> create(@RequestBody FuncaoRequestDTO dto) {
        FuncaoResponseDTO savedFuncao = funcaoService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFuncao);
    }

    @GetMapping
    public ResponseEntity<List<FuncaoResponseDTO>> findAll() {
        List<FuncaoResponseDTO> funcoes = funcaoService.findAll();
        return ResponseEntity.ok(funcoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncaoResponseDTO> findById(@PathVariable Long id) {
        try {
            FuncaoResponseDTO funcao = funcaoService.findById(id);
            return ResponseEntity.ok(funcao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/aplicacao/{aplicacaoId}")
    public ResponseEntity<List<FuncaoResponseDTO>> findByAplicacaoId(@PathVariable Long aplicacaoId) {
        List<FuncaoResponseDTO> funcoes = funcaoService.findByAplicacaoId(aplicacaoId);
        return ResponseEntity.ok(funcoes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncaoResponseDTO> update(@PathVariable Long id, @RequestBody FuncaoRequestDTO dto) {
        try {
            FuncaoResponseDTO updatedFuncao = funcaoService.update(id, dto);
            return ResponseEntity.ok(updatedFuncao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        funcaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
