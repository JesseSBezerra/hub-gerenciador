package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaReportItemDTO {
    private String nomeTarefa;
    private List<String> responsaveis;
    private LocalDate dataCriacao;
    private LocalDate dataEstimada;
    private boolean semana1;
    private boolean semana2;
    private boolean semana3;
    private boolean semana4;
}
