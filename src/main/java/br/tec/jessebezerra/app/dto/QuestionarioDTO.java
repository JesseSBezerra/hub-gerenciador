package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionarioDTO {
    private String titulo;
    private String descricao;
    private List<PerguntaDTO> perguntas;
}
