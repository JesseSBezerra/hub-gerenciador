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
public class TarefaRequestDTO {
    
    private String nome;
    private LocalDate dataCriacao;
    private Complexidade complexidade;
    private String descricao;
    private Tipo tipo;
    private Long tarefaPaiId;
    private Status status;
    private Long aplicacaoId;
    private List<Long> baseConhecimentoIds;
}
