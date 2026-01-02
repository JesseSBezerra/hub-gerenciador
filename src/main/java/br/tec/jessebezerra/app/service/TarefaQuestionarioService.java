package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.TarefaQuestionarioRequestDTO;
import br.tec.jessebezerra.app.dto.TarefaQuestionarioResponseDTO;
import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.model.entity.TarefaQuestionario;
import br.tec.jessebezerra.app.repository.TarefaQuestionarioRepository;
import br.tec.jessebezerra.app.repository.TarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TarefaQuestionarioService {

    private final TarefaQuestionarioRepository tarefaQuestionarioRepository;
    private final TarefaRepository tarefaRepository;

    public TarefaQuestionarioResponseDTO save(TarefaQuestionarioRequestDTO dto) {
        Tarefa tarefa = tarefaRepository.findById(dto.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa not found with id: " + dto.getTarefaId()));
        
        TarefaQuestionario tarefaQuestionario = new TarefaQuestionario();
        tarefaQuestionario.setTarefa(tarefa);
        tarefaQuestionario.setPergunta(dto.getPergunta());
        tarefaQuestionario.setResposta(dto.getResposta());
        
        TarefaQuestionario saved = tarefaQuestionarioRepository.save(tarefaQuestionario);
        return toResponseDTO(saved);
    }

    public List<TarefaQuestionarioResponseDTO> findAll() {
        return tarefaQuestionarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TarefaQuestionarioResponseDTO findById(Long id) {
        return tarefaQuestionarioRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("TarefaQuestionario not found with id: " + id));
    }

    public List<TarefaQuestionarioResponseDTO> findByTarefaId(Long tarefaId) {
        return tarefaQuestionarioRepository.findByTarefaId(tarefaId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TarefaQuestionarioResponseDTO update(Long id, TarefaQuestionarioRequestDTO dto) {
        Tarefa tarefa = tarefaRepository.findById(dto.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa not found with id: " + dto.getTarefaId()));
        
        return tarefaQuestionarioRepository.findById(id)
                .map(existing -> {
                    existing.setTarefa(tarefa);
                    existing.setPergunta(dto.getPergunta());
                    existing.setResposta(dto.getResposta());
                    TarefaQuestionario updated = tarefaQuestionarioRepository.save(existing);
                    return toResponseDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("TarefaQuestionario not found with id: " + id));
    }

    public void delete(Long id) {
        tarefaQuestionarioRepository.deleteById(id);
    }
    
    private TarefaQuestionarioResponseDTO toResponseDTO(TarefaQuestionario entity) {
        TarefaQuestionarioResponseDTO dto = new TarefaQuestionarioResponseDTO();
        dto.setId(entity.getId());
        dto.setTarefaId(entity.getTarefa().getId());
        dto.setPergunta(entity.getPergunta());
        dto.setResposta(entity.getResposta());
        return dto;
    }
}
