package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.SprintItemRequestDTO;
import br.tec.jessebezerra.app.dto.SprintItemResponseDTO;
import br.tec.jessebezerra.app.model.entity.Sprint;
import br.tec.jessebezerra.app.model.entity.SprintItem;
import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.repository.SprintItemRepository;
import br.tec.jessebezerra.app.repository.SprintRepository;
import br.tec.jessebezerra.app.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintItemService {

    private final SprintItemRepository sprintItemRepository;
    private final SprintRepository sprintRepository;
    private final TarefaRepository tarefaRepository;

    public SprintItemResponseDTO addTarefaToSprint(SprintItemRequestDTO dto) {
        Sprint sprint = sprintRepository.findById(dto.getSprintId())
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + dto.getSprintId()));
        
        Tarefa tarefa = tarefaRepository.findById(dto.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa not found with id: " + dto.getTarefaId()));

        SprintItem sprintItem = new SprintItem();
        sprintItem.setSprint(sprint);
        sprintItem.setTarefa(tarefa);

        SprintItem saved = sprintItemRepository.save(sprintItem);
        return toResponseDTO(saved);
    }

    public void removeTarefaFromSprint(Long sprintItemId) {
        sprintItemRepository.deleteById(sprintItemId);
    }

    public List<SprintItemResponseDTO> findBySprint(Long sprintId) {
        return sprintItemRepository.findBySprintId(sprintId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SprintItemResponseDTO> findByTarefa(Long tarefaId) {
        return sprintItemRepository.findByTarefaId(tarefaId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SprintItemResponseDTO> findAll() {
        return sprintItemRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    private SprintItemResponseDTO toResponseDTO(SprintItem sprintItem) {
        SprintItemResponseDTO dto = new SprintItemResponseDTO();
        dto.setId(sprintItem.getId());
        dto.setSprintId(sprintItem.getSprint().getId());
        dto.setSprintNome(sprintItem.getSprint().getNome());
        dto.setTarefaId(sprintItem.getTarefa().getId());
        dto.setTarefaNome(sprintItem.getTarefa().getNome());
        return dto;
    }
}
