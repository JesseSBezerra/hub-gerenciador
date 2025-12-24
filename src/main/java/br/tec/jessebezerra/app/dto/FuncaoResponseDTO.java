package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncaoResponseDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private Long aplicacaoId;
    private String aplicacaoNome;
}
