package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.FuncaoRequestDTO;
import br.tec.jessebezerra.app.dto.FuncaoResponseDTO;
import br.tec.jessebezerra.app.model.entity.Aplicacao;
import br.tec.jessebezerra.app.model.entity.Funcao;
import br.tec.jessebezerra.app.repository.AplicacaoRepository;
import br.tec.jessebezerra.app.repository.FuncaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncaoService {

    private final FuncaoRepository funcaoRepository;
    private final AplicacaoRepository aplicacaoRepository;

    public FuncaoResponseDTO save(FuncaoRequestDTO dto) {
        Aplicacao aplicacao = aplicacaoRepository.findById(dto.getAplicacaoId())
                .orElseThrow(() -> new RuntimeException("Aplicacao not found with id: " + dto.getAplicacaoId()));
        
        Funcao funcao = new Funcao();
        funcao.setNome(dto.getNome());
        funcao.setDescricao(dto.getDescricao());
        funcao.setAtivo(dto.getAtivo());
        funcao.setAplicacao(aplicacao);
        
        Funcao savedFuncao = funcaoRepository.save(funcao);
        return toResponseDTO(savedFuncao);
    }

    public List<FuncaoResponseDTO> findAll() {
        return funcaoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FuncaoResponseDTO findById(Long id) {
        return funcaoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Funcao not found with id: " + id));
    }

    public List<FuncaoResponseDTO> findByAplicacaoId(Long aplicacaoId) {
        return funcaoRepository.findByAplicacaoId(aplicacaoId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FuncaoResponseDTO update(Long id, FuncaoRequestDTO dto) {
        Aplicacao aplicacao = aplicacaoRepository.findById(dto.getAplicacaoId())
                .orElseThrow(() -> new RuntimeException("Aplicacao not found with id: " + dto.getAplicacaoId()));
        
        return funcaoRepository.findById(id)
                .map(existingFuncao -> {
                    existingFuncao.setNome(dto.getNome());
                    existingFuncao.setDescricao(dto.getDescricao());
                    existingFuncao.setAtivo(dto.getAtivo());
                    existingFuncao.setAplicacao(aplicacao);
                    Funcao updatedFuncao = funcaoRepository.save(existingFuncao);
                    return toResponseDTO(updatedFuncao);
                })
                .orElseThrow(() -> new RuntimeException("Funcao not found with id: " + id));
    }

    public void delete(Long id) {
        funcaoRepository.deleteById(id);
    }
    
    private FuncaoResponseDTO toResponseDTO(Funcao funcao) {
        FuncaoResponseDTO dto = new FuncaoResponseDTO();
        dto.setId(funcao.getId());
        dto.setNome(funcao.getNome());
        dto.setDescricao(funcao.getDescricao());
        dto.setAtivo(funcao.getAtivo());
        dto.setAplicacaoId(funcao.getAplicacao().getId());
        dto.setAplicacaoNome(funcao.getAplicacao().getNome());
        return dto;
    }
}
