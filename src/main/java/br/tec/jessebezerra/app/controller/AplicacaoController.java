package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.model.entity.Aplicacao;
import br.tec.jessebezerra.app.service.AplicacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aplicacoes")
@RequiredArgsConstructor
public class AplicacaoController {

    private final AplicacaoService aplicacaoService;

    @PostMapping
    public ResponseEntity<Aplicacao> create(@RequestBody Aplicacao aplicacao) {
        Aplicacao savedAplicacao = aplicacaoService.save(aplicacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAplicacao);
    }

    @GetMapping
    public ResponseEntity<List<Aplicacao>> findAll() {
        List<Aplicacao> aplicacoes = aplicacaoService.findAll();
        return ResponseEntity.ok(aplicacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aplicacao> findById(@PathVariable Long id) {
        return aplicacaoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aplicacao> update(@PathVariable Long id, @RequestBody Aplicacao aplicacao) {
        try {
            Aplicacao updatedAplicacao = aplicacaoService.update(id, aplicacao);
            return ResponseEntity.ok(updatedAplicacao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        aplicacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
