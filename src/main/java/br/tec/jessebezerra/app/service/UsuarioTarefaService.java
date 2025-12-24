package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.UsuarioTarefaRequestDTO;
import br.tec.jessebezerra.app.dto.UsuarioTarefaResponseDTO;
import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.model.entity.User;
import br.tec.jessebezerra.app.model.entity.UsuarioTarefa;
import br.tec.jessebezerra.app.repository.TarefaRepository;
import br.tec.jessebezerra.app.repository.UserRepository;
import br.tec.jessebezerra.app.repository.UsuarioTarefaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioTarefaService {

    private final UsuarioTarefaRepository usuarioTarefaRepository;
    private final UserRepository userRepository;
    private final TarefaRepository tarefaRepository;

    public UsuarioTarefaResponseDTO vincular(UsuarioTarefaRequestDTO dto) {
        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario not found with id: " + dto.getUsuarioId()));
        
        Tarefa tarefa = tarefaRepository.findById(dto.getTarefaId())
                .orElseThrow(() -> new RuntimeException("Tarefa not found with id: " + dto.getTarefaId()));

        UsuarioTarefa usuarioTarefa = new UsuarioTarefa();
        usuarioTarefa.setUsuario(usuario);
        usuarioTarefa.setTarefa(tarefa);
        usuarioTarefa.setDataVinculo(dto.getDataVinculo() != null ? dto.getDataVinculo() : LocalDate.now());

        UsuarioTarefa saved = usuarioTarefaRepository.save(usuarioTarefa);
        return toResponseDTO(saved);
    }

    public void desvincular(Long vinculoId) {
        usuarioTarefaRepository.deleteById(vinculoId);
    }

    public List<UsuarioTarefaResponseDTO> listarPorUsuario(Long usuarioId) {
        return usuarioTarefaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioTarefaResponseDTO> listarPorTarefa(Long tarefaId) {
        return usuarioTarefaRepository.findByTarefaId(tarefaId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioTarefaResponseDTO> listarTodos() {
        return usuarioTarefaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    private UsuarioTarefaResponseDTO toResponseDTO(UsuarioTarefa usuarioTarefa) {
        UsuarioTarefaResponseDTO dto = new UsuarioTarefaResponseDTO();
        dto.setId(usuarioTarefa.getId());
        dto.setUsuarioId(usuarioTarefa.getUsuario().getId());
        dto.setUsuarioNome(usuarioTarefa.getUsuario().getNome());
        dto.setTarefaId(usuarioTarefa.getTarefa().getId());
        dto.setTarefaNome(usuarioTarefa.getTarefa().getNome());
        dto.setDataVinculo(usuarioTarefa.getDataVinculo());
        return dto;
    }
}
