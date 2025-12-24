package br.tec.jessebezerra.app.controller;

import br.tec.jessebezerra.app.dto.FuncaoResponseDTO;
import br.tec.jessebezerra.app.integration.openai.OpenAIService;
import br.tec.jessebezerra.app.integration.openai.dto.OpenAIResponse;
import br.tec.jessebezerra.app.integration.openai.dto.QuestaoRequest;
import br.tec.jessebezerra.app.model.entity.Aplicacao;
import br.tec.jessebezerra.app.service.AplicacaoService;
import br.tec.jessebezerra.app.service.FuncaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/openai")
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;
    private final AplicacaoService aplicacaoService;
    private final FuncaoService funcaoService;


    @PostMapping("/questao")
    public ResponseEntity<?> processarQuestao(@RequestBody QuestaoRequest request) {
        Aplicacao aplicacao = aplicacaoService.findById(request.getAplicacaoId())
                .orElseThrow(() -> new RuntimeException("Aplicacao not found with id: " + request.getAplicacaoId()));

        List<FuncaoResponseDTO> funcoes = funcaoService.findByAplicacaoId(request.getAplicacaoId());

        if (funcoes.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhuma função encontrada para a aplicação informada");
        }

        OpenAIResponse response = openAIService.processQuestaoComFuncoes(funcoes, request.getQuestao());
        
        return ResponseEntity.ok(response);
    }
}
