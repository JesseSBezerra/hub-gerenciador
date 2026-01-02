package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.BaseConhecimentoResponseDTO;
import br.tec.jessebezerra.app.dto.FuncaoResponseDTO;
import br.tec.jessebezerra.app.dto.QuestionarioDTO;
import br.tec.jessebezerra.app.integration.openai.OpenAIService;
import br.tec.jessebezerra.app.model.entity.Aplicacao;
import br.tec.jessebezerra.app.model.entity.BaseConhecimento;
import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionarioService {

    private final TarefaRepository tarefaRepository;
    private final FuncaoService funcaoService;
    private final OpenAIService openAIService;

    public QuestionarioDTO gerarQuestionarioPorTarefa(Long tarefaId) {
        log.info("Gerando questionário para tarefa ID: {}", tarefaId);
        
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa not found with id: " + tarefaId));
        
        String nomeTarefa = tarefa.getNome();
        String descricaoTarefa = tarefa.getDescricao();
        String tituloSugerido = tarefa.getTituloSugerido();
        String tarefaSugerida = tarefa.getTarefaSugerida();
        
        String aplicacaoNome = null;
        String aplicacaoRepo = null;
        List<FuncaoResponseDTO> funcoes = null;
        
        if (tarefa.getAplicacao() != null) {
            Aplicacao aplicacao = tarefa.getAplicacao();
            aplicacaoNome = aplicacao.getNome();
            aplicacaoRepo = aplicacao.getRepo();
            funcoes = funcaoService.findByAplicacaoId(aplicacao.getId());
            log.info("Aplicação encontrada: {} com {} funções", aplicacaoNome, funcoes.size());
        }
        
        List<BaseConhecimentoResponseDTO> baseConhecimentos = null;
        if (tarefa.getBaseConhecimentos() != null && !tarefa.getBaseConhecimentos().isEmpty()) {
            baseConhecimentos = tarefa.getBaseConhecimentos().stream()
                    .map(bc -> new BaseConhecimentoResponseDTO(bc.getId(), bc.getNome(), bc.getDescricao(), bc.getAtivo()))
                    .collect(Collectors.toList());
            log.info("Base de conhecimento encontrada: {} itens", baseConhecimentos.size());
        }
        
        QuestionarioDTO questionario = openAIService.gerarQuestionarioPorTarefa(
            nomeTarefa, 
            descricaoTarefa, 
            tituloSugerido, 
            tarefaSugerida, 
            aplicacaoNome, 
            aplicacaoRepo, 
            funcoes, 
            baseConhecimentos
        );
        
        log.info("Questionário gerado com sucesso para tarefa ID: {}", tarefaId);
        
        return questionario;
    }
}
