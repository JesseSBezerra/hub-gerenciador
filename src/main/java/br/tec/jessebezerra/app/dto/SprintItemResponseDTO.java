package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SprintItemResponseDTO {
    private Long id;
    private Long sprintId;
    private String sprintNome;
    private Long tarefaId;
    private String tarefaNome;
}
