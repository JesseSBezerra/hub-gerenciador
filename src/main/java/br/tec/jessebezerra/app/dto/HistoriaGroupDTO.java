package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaGroupDTO {
    private String historiaNome;
    private List<TarefaReportItemDTO> subtarefas;
}
