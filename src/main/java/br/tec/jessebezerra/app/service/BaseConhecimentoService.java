package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.BaseConhecimentoRequestDTO;
import br.tec.jessebezerra.app.dto.BaseConhecimentoResponseDTO;
import br.tec.jessebezerra.app.model.entity.BaseConhecimento;
import br.tec.jessebezerra.app.repository.BaseConhecimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaseConhecimentoService {

    private final BaseConhecimentoRepository baseConhecimentoRepository;

    public BaseConhecimentoResponseDTO save(BaseConhecimentoRequestDTO dto) {
        BaseConhecimento baseConhecimento = new BaseConhecimento();
        baseConhecimento.setNome(dto.getNome());
        baseConhecimento.setDescricao(dto.getDescricao());
        baseConhecimento.setAtivo(dto.getAtivo());
        
        BaseConhecimento saved = baseConhecimentoRepository.save(baseConhecimento);
        return toResponseDTO(saved);
    }

    public List<BaseConhecimentoResponseDTO> findAll() {
        return baseConhecimentoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public BaseConhecimentoResponseDTO findById(Long id) {
        return baseConhecimentoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("BaseConhecimento not found with id: " + id));
    }

    public BaseConhecimentoResponseDTO update(Long id, BaseConhecimentoRequestDTO dto) {
        return baseConhecimentoRepository.findById(id)
                .map(existing -> {
                    existing.setNome(dto.getNome());
                    existing.setDescricao(dto.getDescricao());
                    existing.setAtivo(dto.getAtivo());
                    BaseConhecimento updated = baseConhecimentoRepository.save(existing);
                    return toResponseDTO(updated);
                })
                .orElseThrow(() -> new RuntimeException("BaseConhecimento not found with id: " + id));
    }

    public void delete(Long id) {
        baseConhecimentoRepository.deleteById(id);
    }
    
    private BaseConhecimentoResponseDTO toResponseDTO(BaseConhecimento entity) {
        BaseConhecimentoResponseDTO dto = new BaseConhecimentoResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setAtivo(entity.getAtivo());
        return dto;
    }
}
