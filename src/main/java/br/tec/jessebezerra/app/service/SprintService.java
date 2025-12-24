package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.SprintRequestDTO;
import br.tec.jessebezerra.app.dto.SprintResponseDTO;
import br.tec.jessebezerra.app.model.entity.Sprint;
import br.tec.jessebezerra.app.repository.SprintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintService {

    private final SprintRepository sprintRepository;

    public SprintResponseDTO save(SprintRequestDTO dto) {
        Sprint sprint = new Sprint();
        sprint.setNome(dto.getNome());
        sprint.setDescricao(dto.getDescricao());
        
        Sprint savedSprint = sprintRepository.save(sprint);
        return toResponseDTO(savedSprint);
    }

    public List<SprintResponseDTO> findAll() {
        return sprintRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<SprintResponseDTO> findById(Long id) {
        return sprintRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public SprintResponseDTO update(Long id, SprintRequestDTO dto) {
        return sprintRepository.findById(id)
                .map(existingSprint -> {
                    existingSprint.setNome(dto.getNome());
                    existingSprint.setDescricao(dto.getDescricao());
                    Sprint updatedSprint = sprintRepository.save(existingSprint);
                    return toResponseDTO(updatedSprint);
                })
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));
    }

    public void delete(Long id) {
        sprintRepository.deleteById(id);
    }
    
    private SprintResponseDTO toResponseDTO(Sprint sprint) {
        SprintResponseDTO dto = new SprintResponseDTO();
        dto.setId(sprint.getId());
        dto.setNome(sprint.getNome());
        dto.setDescricao(sprint.getDescricao());
        return dto;
    }
}
