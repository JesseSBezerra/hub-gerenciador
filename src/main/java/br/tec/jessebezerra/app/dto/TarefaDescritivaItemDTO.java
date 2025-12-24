package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaDescritivaItemDTO {
    private String nomeTarefa;
    private String tituloSugerido;
    private String tarefaSugerida;
}
