package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.dto.BaseConhecimentoResponseDTO;
import br.tec.jessebezerra.app.dto.FuncaoResponseDTO;
import br.tec.jessebezerra.app.dto.TarefaRequestDTO;
import br.tec.jessebezerra.app.dto.TarefaResponseDTO;
import br.tec.jessebezerra.app.dto.TarefaSugeridaDTO;
import br.tec.jessebezerra.app.integration.openai.OpenAIService;
import br.tec.jessebezerra.app.model.entity.Aplicacao;
import br.tec.jessebezerra.app.model.entity.BaseConhecimento;
import br.tec.jessebezerra.app.model.entity.Tarefa;
import br.tec.jessebezerra.app.model.enums.Tipo;
import br.tec.jessebezerra.app.repository.AplicacaoRepository;
import br.tec.jessebezerra.app.repository.BaseConhecimentoRepository;
import br.tec.jessebezerra.app.repository.TarefaRepository;
import br.tec.jessebezerra.app.service.FuncaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final AplicacaoRepository aplicacaoRepository;
    private final BaseConhecimentoRepository baseConhecimentoRepository;
    private final FuncaoService funcaoService;
    private final OpenAIService openAIService;

    public TarefaResponseDTO save(TarefaRequestDTO dto) {
        Tarefa tarefa = new Tarefa();
        tarefa.setNome(dto.getNome());
        tarefa.setDataCriacao(dto.getDataCriacao());
        tarefa.setComplexidade(dto.getComplexidade());
        tarefa.setDescricao(dto.getDescricao());
        tarefa.setTipo(dto.getTipo());
        tarefa.setStatus(dto.getStatus());
        
        if (dto.getTarefaPaiId() != null) {
            Tarefa tarefaPai = tarefaRepository.findById(dto.getTarefaPaiId())
                    .orElseThrow(() -> new RuntimeException("Tarefa pai not found with id: " + dto.getTarefaPaiId()));
            tarefa.setTarefaPai(tarefaPai);
        }
        
        Aplicacao aplicacao = null;
        if (dto.getAplicacaoId() != null) {
            aplicacao = aplicacaoRepository.findById(dto.getAplicacaoId())
                    .orElseThrow(() -> new RuntimeException("Aplicacao not found with id: " + dto.getAplicacaoId()));
            tarefa.setAplicacao(aplicacao);
        }
        
        if (dto.getBaseConhecimentoIds() != null && !dto.getBaseConhecimentoIds().isEmpty()) {
            List<BaseConhecimento> baseConhecimentos = baseConhecimentoRepository.findAllById(dto.getBaseConhecimentoIds());
            tarefa.getBaseConhecimentos().addAll(baseConhecimentos);
            
            List<BaseConhecimentoResponseDTO> basesDTO = baseConhecimentos.stream()
                    .map(bc -> new BaseConhecimentoResponseDTO(bc.getId(), bc.getNome(), bc.getDescricao(), bc.getAtivo()))
                    .collect(Collectors.toList());
            
            TarefaSugeridaDTO sugestao = openAIService.gerarTarefaSugerida(dto.getNome(), dto.getDescricao(), basesDTO);
            if (sugestao != null) {
                tarefa.setTituloSugerido(sugestao.getTitulo());
                tarefa.setTarefaSugerida(sugestao.getDescricao());
            }
        } else if (aplicacao != null) {
            List<FuncaoResponseDTO> funcoes = funcaoService.findByAplicacaoId(aplicacao.getId());
            TarefaSugeridaDTO sugestao = openAIService.gerarTarefaSugeridaComAplicacao(
                dto.getNome(), 
                dto.getDescricao(), 
                aplicacao.getNome(), 
                aplicacao.getRepo(), 
                funcoes
            );
            if (sugestao != null) {
                tarefa.setTituloSugerido(sugestao.getTitulo());
                tarefa.setTarefaSugerida(sugestao.getDescricao());
            }
        }
        
        Tarefa savedTarefa = tarefaRepository.save(tarefa);
        return toResponseDTO(savedTarefa);
    }

    public List<TarefaResponseDTO> findAll(Tipo tipo) {
        if (tipo != null) {
            return tarefaRepository.findByTipo(tipo).stream()
                    .map(this::toResponseDTO)
                    .collect(Collectors.toList());
        }
        return tarefaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TarefaResponseDTO findById(Long id) {
        return tarefaRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Tarefa not found with id: " + id));
    }

    public TarefaResponseDTO update(Long id, TarefaRequestDTO dto) {
        return tarefaRepository.findById(id)
                .map(existingTarefa -> {
                    existingTarefa.setNome(dto.getNome());
                    existingTarefa.setDataCriacao(dto.getDataCriacao());
                    existingTarefa.setComplexidade(dto.getComplexidade());
                    existingTarefa.setDescricao(dto.getDescricao());
                    existingTarefa.setTipo(dto.getTipo());
                    existingTarefa.setStatus(dto.getStatus());
                    
                    if (dto.getTarefaPaiId() != null) {
                        Tarefa tarefaPai = tarefaRepository.findById(dto.getTarefaPaiId())
                                .orElseThrow(() -> new RuntimeException("Tarefa pai not found with id: " + dto.getTarefaPaiId()));
                        existingTarefa.setTarefaPai(tarefaPai);
                    } else {
                        existingTarefa.setTarefaPai(null);
                    }
                    
                    Aplicacao aplicacao = null;
                    if (dto.getAplicacaoId() != null) {
                        aplicacao = aplicacaoRepository.findById(dto.getAplicacaoId())
                                .orElseThrow(() -> new RuntimeException("Aplicacao not found with id: " + dto.getAplicacaoId()));
                        existingTarefa.setAplicacao(aplicacao);
                    } else {
                        existingTarefa.setAplicacao(null);
                    }
                    
                    existingTarefa.getBaseConhecimentos().clear();
                    if (dto.getBaseConhecimentoIds() != null && !dto.getBaseConhecimentoIds().isEmpty()) {
                        List<BaseConhecimento> baseConhecimentos = baseConhecimentoRepository.findAllById(dto.getBaseConhecimentoIds());
                        existingTarefa.getBaseConhecimentos().addAll(baseConhecimentos);
                        
                        List<BaseConhecimentoResponseDTO> basesDTO = baseConhecimentos.stream()
                                .map(bc -> new BaseConhecimentoResponseDTO(bc.getId(), bc.getNome(), bc.getDescricao(), bc.getAtivo()))
                                .collect(Collectors.toList());
                        
                        TarefaSugeridaDTO sugestao = openAIService.gerarTarefaSugerida(dto.getNome(), dto.getDescricao(), basesDTO);
                        if (sugestao != null) {
                            existingTarefa.setTituloSugerido(sugestao.getTitulo());
                            existingTarefa.setTarefaSugerida(sugestao.getDescricao());
                        }
                    } else if (aplicacao != null) {
                        List<FuncaoResponseDTO> funcoes = funcaoService.findByAplicacaoId(aplicacao.getId());
                        TarefaSugeridaDTO sugestao = openAIService.gerarTarefaSugeridaComAplicacao(
                            dto.getNome(), 
                            dto.getDescricao(), 
                            aplicacao.getNome(), 
                            aplicacao.getRepo(), 
                            funcoes
                        );
                        if (sugestao != null) {
                            existingTarefa.setTituloSugerido(sugestao.getTitulo());
                            existingTarefa.setTarefaSugerida(sugestao.getDescricao());
                        }
                    } else {
                        existingTarefa.setTituloSugerido(null);
                        existingTarefa.setTarefaSugerida(null);
                    }
                    
                    Tarefa updatedTarefa = tarefaRepository.save(existingTarefa);
                    return toResponseDTO(updatedTarefa);
                })
                .orElseThrow(() -> new RuntimeException("Tarefa not found with id: " + id));
    }

    public void delete(Long id) {
        tarefaRepository.deleteById(id);
    }
    
    private TarefaResponseDTO toResponseDTO(Tarefa tarefa) {
        TarefaResponseDTO dto = new TarefaResponseDTO();
        dto.setId(tarefa.getId());
        dto.setNome(tarefa.getNome());
        dto.setDataCriacao(tarefa.getDataCriacao());
        dto.setComplexidade(tarefa.getComplexidade());
        dto.setDescricao(tarefa.getDescricao());
        dto.setTipo(tarefa.getTipo());
        dto.setStatus(tarefa.getStatus());
        dto.setDataEstimada(tarefa.getDataEstimada());
        
        if (tarefa.getTarefaPai() != null) {
            dto.setTarefaPaiId(tarefa.getTarefaPai().getId());
            dto.setTarefaPaiNome(tarefa.getTarefaPai().getNome());
        }
        
        if (tarefa.getAplicacao() != null) {
            dto.setAplicacaoId(tarefa.getAplicacao().getId());
            dto.setAplicacaoNome(tarefa.getAplicacao().getNome());
        }
        
        dto.setTarefaSugerida(tarefa.getTarefaSugerida());
        dto.setTituloSugerido(tarefa.getTituloSugerido());
        
        if (tarefa.getBaseConhecimentos() != null && !tarefa.getBaseConhecimentos().isEmpty()) {
            List<BaseConhecimentoResponseDTO> basesDTO = tarefa.getBaseConhecimentos().stream()
                    .map(bc -> new BaseConhecimentoResponseDTO(bc.getId(), bc.getNome(), bc.getDescricao(), bc.getAtivo()))
                    .collect(Collectors.toList());
            dto.setBaseConhecimentos(basesDTO);
        }
        
        return dto;
    }
}
