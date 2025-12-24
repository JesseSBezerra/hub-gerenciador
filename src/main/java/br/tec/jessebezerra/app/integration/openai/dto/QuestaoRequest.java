package br.tec.jessebezerra.app.integration.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestaoRequest {
    
    private Long aplicacaoId;
    private String questao;
}
