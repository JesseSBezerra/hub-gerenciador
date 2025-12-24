package br.tec.jessebezerra.app.dto;

import br.tec.jessebezerra.app.model.enums.Complexidade;
import br.tec.jessebezerra.app.model.enums.Status;
import br.tec.jessebezerra.app.model.enums.Tipo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaResponseDTO {
    
    private Long id;
    private String nome;
    private LocalDate dataCriacao;
    private Complexidade complexidade;
    private String descricao;
    private Tipo tipo;
    private Long tarefaPaiId;
    private String tarefaPaiNome;
    private Status status;
    private LocalDate dataEstimada;
    private Long aplicacaoId;
    private String aplicacaoNome;
    private String tarefaSugerida;
    private String tituloSugerido;
    private List<BaseConhecimentoResponseDTO> baseConhecimentos;
}
