package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.DetalhesProjetoResponseDTO;
import br.tec.jessebezerra.app.dto.ProjetoRequestDTO;
import br.tec.jessebezerra.app.dto.ProjetoResponseDTO;
import br.tec.jessebezerra.app.model.entity.Projeto;
import br.tec.jessebezerra.app.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoResponseDTO save(ProjetoRequestDTO dto) {
        Projeto projeto = new Projeto();
        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());
        
        Projeto saved = projetoRepository.save(projeto);
        return toResponseDTO(saved);
    }

    public List<ProjetoResponseDTO> findAll() {
        return projetoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProjetoResponseDTO findById(Long id) {
        return projetoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Projeto not found with id: " + id));
    }

    public ProjetoResponseDTO update(Long id, ProjetoRequestDTO dto) {
        return projetoRepository.findById(id)
                .map(existing -> {
                    existing.setNome(dto.getNome());
                    existing.setDescricao(dto.getDescricao());
                    Projeto updated = projetoRepository.save(existing);
                    return toResponseDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("Projeto not found with id: " + id));
    }

    public void delete(Long id) {
        projetoRepository.deleteById(id);
    }
    
    private ProjetoResponseDTO toResponseDTO(Projeto projeto) {
        ProjetoResponseDTO dto = new ProjetoResponseDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setDescricao(projeto.getDescricao());
        
        if (projeto.getDetalhes() != null) {
            List<DetalhesProjetoResponseDTO> detalhesDTO = projeto.getDetalhes().stream()
                    .map(detalhe -> new DetalhesProjetoResponseDTO(
                            detalhe.getId(),
                            detalhe.getProjeto().getId(),
                            detalhe.getDescricao()
                    ))
                    .collect(Collectors.toList());
            dto.setDetalhes(detalhesDTO);
        }
        
        return dto;
    }
}
