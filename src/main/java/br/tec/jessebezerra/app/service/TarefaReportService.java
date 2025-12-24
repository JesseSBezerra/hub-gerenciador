package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.HistoriaGroupDTO;
import br.tec.jessebezerra.app.dto.TarefaReportDTO;
import br.tec.jessebezerra.app.dto.TarefaReportItemDTO;
import br.tec.jessebezerra.app.dto.UsuarioTarefaResponseDTO;
import br.tec.jessebezerra.app.model.entity.Sprint;
import br.tec.jessebezerra.app.model.entity.SprintItem;
import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.repository.SprintItemRepository;
import br.tec.jessebezerra.app.repository.SprintRepository;
import br.tec.jessebezerra.app.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TarefaReportService {

    private final SprintRepository sprintRepository;
    private final SprintItemRepository sprintItemRepository;
    private final TarefaRepository tarefaRepository;
    private final UsuarioTarefaService usuarioTarefaService;

    public TarefaReportDTO gerarRelatorio(Long sprintId) {
        log.info("Gerando relatório para sprint ID: {}", sprintId);
        
        Sprint sprint = sprintRepository.findById(sprintId)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + sprintId));
        
        log.info("Sprint encontrada: {}", sprint.getNome());

        List<SprintItem> sprintItems = sprintItemRepository.findBySprintId(sprintId);
        log.info("Total de SprintItems encontrados: {}", sprintItems.size());
        
        // As tarefas da sprint são as histórias (tarefas pai)
        // Precisamos buscar as subtarefas de cada uma
        List<HistoriaGroupDTO> historias = new ArrayList<>();
        
        for (SprintItem sprintItem : sprintItems) {
            Tarefa historia = sprintItem.getTarefa();
            
            log.info("Processando historia: {} (ID: {})", historia.getNome(), historia.getId());
            
            HistoriaGroupDTO historiaGroup = new HistoriaGroupDTO();
            historiaGroup.setHistoriaNome(historia.getNome());
            
            // Buscar subtarefas desta historia
            List<Tarefa> subtarefasList = tarefaRepository.findByTarefaPaiId(historia.getId());
            log.info("  Total de subtarefas encontradas para historia ID {}: {}", historia.getId(), subtarefasList.size());
            
            if (subtarefasList.isEmpty()) {
                log.warn("  ATENCAO: Historia '{}' (ID: {}) nao tem subtarefas!", historia.getNome(), historia.getId());
            }
            
            List<TarefaReportItemDTO> subtarefas = new ArrayList<>();
            
            for (Tarefa subtarefa : subtarefasList) {
                log.info("    -> Subtarefa ID: {}, Nome: {}, TarefaPaiId: {}", 
                    subtarefa.getId(), subtarefa.getNome(), subtarefa.getTarefaPai() != null ? subtarefa.getTarefaPai().getId() : "NULL");
                TarefaReportItemDTO item = new TarefaReportItemDTO();
                item.setNomeTarefa(subtarefa.getNome());
                item.setDataCriacao(subtarefa.getDataCriacao());
                item.setDataEstimada(subtarefa.getDataEstimada());
                
                List<UsuarioTarefaResponseDTO> vinculos = usuarioTarefaService.listarPorTarefa(subtarefa.getId());
                List<String> responsaveis = vinculos.stream()
                        .map(UsuarioTarefaResponseDTO::getUsuarioNome)
                        .collect(Collectors.toList());
                item.setResponsaveis(responsaveis);
                
                log.info("    Subtarefa: {}, Responsáveis: {}", subtarefa.getNome(), responsaveis);
                
                calcularSemanas(item);
                
                subtarefas.add(item);
            }
            
            historiaGroup.setSubtarefas(subtarefas);
            historias.add(historiaGroup);
        }
        
        TarefaReportDTO report = new TarefaReportDTO();
        report.setHistorias(historias);
        
        log.info("Relatório gerado com {} histórias", historias.size());
        
        return report;
    }
    
    private void calcularSemanas(TarefaReportItemDTO item) {
        LocalDate dataCriacao = item.getDataCriacao();
        LocalDate dataEstimada = item.getDataEstimada();
        LocalDate hoje = LocalDate.now();
        
        if (dataCriacao == null || dataEstimada == null) {
            return;
        }
        
        long diasTotal = ChronoUnit.DAYS.between(dataCriacao, dataEstimada);
        long diasDecorridos = ChronoUnit.DAYS.between(dataCriacao, hoje);
        
        if (diasTotal <= 0) {
            return;
        }
        
        double progressoPorcentagem = (double) diasDecorridos / diasTotal;
        
        item.setSemana1(progressoPorcentagem >= 0.00);
        item.setSemana2(progressoPorcentagem >= 0.25);
        item.setSemana3(progressoPorcentagem >= 0.50);
        item.setSemana4(progressoPorcentagem >= 0.75);
    }
}
