package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.HistoriaDescritivaGroupDTO;
import br.tec.jessebezerra.app.dto.TarefaDescritivaItemDTO;
import br.tec.jessebezerra.app.dto.TarefaDescritivaReportDTO;
import br.tec.jessebezerra.app.dto.TarefaQuestionarioResponseDTO;
import br.tec.jessebezerra.app.model.entity.Sprint;
import br.tec.jessebezerra.app.model.entity.SprintItem;
import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.repository.SprintItemRepository;
import br.tec.jessebezerra.app.repository.SprintRepository;
import br.tec.jessebezerra.app.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TarefaDescritivaReportService {

    private final SprintRepository sprintRepository;
    private final SprintItemRepository sprintItemRepository;
    private final TarefaRepository tarefaRepository;
    private final TarefaQuestionarioService tarefaQuestionarioService;

    public TarefaDescritivaReportDTO gerarRelatorioDescritivo(Long sprintId) {
        log.info("Gerando relatório descritivo para sprint ID: {}", sprintId);
        
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        
        log.info("Sprint encontrada: {}", sprint.getNome());

        List<SprintItem> sprintItems = sprintItemRepository.findBySprintId(sprintId);
        log.info("Total de SprintItems encontrados: {}", sprintItems.size());
        
        List<HistoriaDescritivaGroupDTO> historias = new ArrayList<>();
        
        for (SprintItem sprintItem : sprintItems) {
            Tarefa historia = sprintItem.getTarefa();
            
            log.info("Processando historia: {} (ID: {})", historia.getNome(), historia.getId());
            
            HistoriaDescritivaGroupDTO historiaGroup = new HistoriaDescritivaGroupDTO();
            historiaGroup.setHistoriaNome(historia.getNome());
            
            List<Tarefa> subtarefasList = tarefaRepository.findByTarefaPaiId(historia.getId());
            log.info("  Total de subtarefas encontradas: {}", subtarefasList.size());
            
            List<TarefaDescritivaItemDTO> subtarefas = new ArrayList<>();
            
            for (Tarefa subtarefa : subtarefasList) {
                TarefaDescritivaItemDTO item = new TarefaDescritivaItemDTO();
                item.setNomeTarefa(subtarefa.getNome());
                item.setTituloSugerido(subtarefa.getTituloSugerido());
                item.setTarefaSugerida(subtarefa.getTarefaSugerida());
                item.setBeneficioProduto(subtarefa.getBeneficioProduto());
                item.setBeneficioAplicacao(subtarefa.getBeneficioAplicacao());
                
                List<TarefaQuestionarioResponseDTO> questionarios = tarefaQuestionarioService.findByTarefaId(subtarefa.getId());
                if (!questionarios.isEmpty()) {
                    item.setQuestionarios(questionarios);
                    log.info("    Subtarefa: {} - {} questionários encontrados", subtarefa.getNome(), questionarios.size());
                } else {
                    log.info("    Subtarefa: {}", subtarefa.getNome());
                }
                
                subtarefas.add(item);
            }
            
            historiaGroup.setSubtarefas(subtarefas);
            historias.add(historiaGroup);
        }
        
        TarefaDescritivaReportDTO report = new TarefaDescritivaReportDTO();
        report.setHistorias(historias);
        
        log.info("Relatório descritivo gerado com {} histórias", historias.size());
        
        return report;
    }
}
