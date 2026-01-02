package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.BeneficiosResponseDTO;
import br.tec.jessebezerra.app.dto.EspecificacaoRequestDTO;
import br.tec.jessebezerra.app.dto.EspecificacaoResponseDTO;
import br.tec.jessebezerra.app.dto.QuestionarioDTO;
import br.tec.jessebezerra.app.dto.TarefaRequestDTO;
import br.tec.jessebezerra.app.dto.TarefaResponseDTO;
import br.tec.jessebezerra.app.model.enums.Tipo;
import br.tec.jessebezerra.app.service.QuestionarioService;
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
    private final QuestionarioService questionarioService;

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

    @GetMapping("/tarefa-pai/{tarefaPaiId}")
    public ResponseEntity<List<TarefaResponseDTO>> findByTarefaPaiId(@PathVariable Long tarefaPaiId) {
        List<TarefaResponseDTO> tarefas = tarefaService.findByTarefaPaiId(tarefaPaiId);
        return ResponseEntity.ok(tarefas);
    }

    @PostMapping("/especificacao")
    public ResponseEntity<EspecificacaoResponseDTO> gerarEspecificacao(@RequestBody EspecificacaoRequestDTO request) {
        EspecificacaoResponseDTO response = tarefaService.gerarEspecificacao(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/questionario")
    public ResponseEntity<QuestionarioDTO> gerarQuestionario(@PathVariable Long id) {
        try {
            QuestionarioDTO questionario = questionarioService.gerarQuestionarioPorTarefa(id);
            return ResponseEntity.ok(questionario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/beneficios")
    public ResponseEntity<BeneficiosResponseDTO> gerarBeneficios(@PathVariable Long id) {
        try {
            BeneficiosResponseDTO beneficios = tarefaService.gerarBeneficios(id);
            return ResponseEntity.ok(beneficios);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BeneficiosResponseDTO("Erro ao gerar benefícios", e.getMessage()));
        }
    }
}
