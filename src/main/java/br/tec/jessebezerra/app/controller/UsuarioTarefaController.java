package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.UsuarioTarefaRequestDTO;
import br.tec.jessebezerra.app.dto.UsuarioTarefaResponseDTO;
import br.tec.jessebezerra.app.service.UsuarioTarefaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario-tarefa")
@RequiredArgsConstructor
public class UsuarioTarefaController {

    private final UsuarioTarefaService usuarioTarefaService;

    @PostMapping("/vincular")
    public ResponseEntity<UsuarioTarefaResponseDTO> vincular(@RequestBody UsuarioTarefaRequestDTO dto) {
        try {
            UsuarioTarefaResponseDTO vinculo = usuarioTarefaService.vincular(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(vinculo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{vinculoId}")
    public ResponseEntity<Void> desvincular(@PathVariable Long vinculoId) {
        usuarioTarefaService.desvincular(vinculoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<UsuarioTarefaResponseDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<UsuarioTarefaResponseDTO> vinculos = usuarioTarefaService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(vinculos);
    }

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<UsuarioTarefaResponseDTO>> listarPorTarefa(@PathVariable Long tarefaId) {
        List<UsuarioTarefaResponseDTO> vinculos = usuarioTarefaService.listarPorTarefa(tarefaId);
        return ResponseEntity.ok(vinculos);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioTarefaResponseDTO>> listarTodos() {
        List<UsuarioTarefaResponseDTO> vinculos = usuarioTarefaService.listarTodos();
        return ResponseEntity.ok(vinculos);
    }
}
