package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaQuestionarioRequestDTO {
    
    private Long tarefaId;
    private String pergunta;
    private String resposta;
}
