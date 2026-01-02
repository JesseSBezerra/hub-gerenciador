package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.DetalhesProjetoRequestDTO;
import br.tec.jessebezerra.app.dto.DetalhesProjetoResponseDTO;
import br.tec.jessebezerra.app.model.entity.DetalhesProjeto;
import br.tec.jessebezerra.app.model.entity.Projeto;
import br.tec.jessebezerra.app.repository.DetalhesProjetoRepository;
import br.tec.jessebezerra.app.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalhesProjetoService {

    private final DetalhesProjetoRepository detalhesProjetoRepository;
    private final ProjetoRepository projetoRepository;

    public DetalhesProjetoResponseDTO save(DetalhesProjetoRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> new RuntimeException("Projeto not found with id: " + dto.getProjetoId()));
        
        DetalhesProjeto detalhe = new DetalhesProjeto();
        detalhe.setProjeto(projeto);
        detalhe.setDescricao(dto.getDescricao());
        
        DetalhesProjeto saved = detalhesProjetoRepository.save(detalhe);
        return toResponseDTO(saved);
    }

    public List<DetalhesProjetoResponseDTO> findAll() {
        return detalhesProjetoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DetalhesProjetoResponseDTO findById(Long id) {
        return detalhesProjetoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("DetalhesProjeto not found with id: " + id));
    }

    public List<DetalhesProjetoResponseDTO> findByProjetoId(Long projetoId) {
        return detalhesProjetoRepository.findByProjetoId(projetoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public DetalhesProjetoResponseDTO update(Long id, DetalhesProjetoRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(dto.getProjetoId())
                .orElseThrow(() -> new RuntimeException("Projeto not found with id: " + dto.getProjetoId()));
        
        return detalhesProjetoRepository.findById(id)
                .map(existing -> {
                    existing.setProjeto(projeto);
                    existing.setDescricao(dto.getDescricao());
                    DetalhesProjeto updated = detalhesProjetoRepository.save(existing);
                    return toResponseDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("DetalhesProjeto not found with id: " + id));
    }

    public void delete(Long id) {
        detalhesProjetoRepository.deleteById(id);
    }
    
    private DetalhesProjetoResponseDTO toResponseDTO(DetalhesProjeto detalhe) {
        DetalhesProjetoResponseDTO dto = new DetalhesProjetoResponseDTO();
        dto.setId(detalhe.getId());
        dto.setProjetoId(detalhe.getProjeto().getId());
        dto.setDescricao(detalhe.getDescricao());
        return dto;
    }
}
