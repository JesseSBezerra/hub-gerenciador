package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioTarefaResponseDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private Long tarefaId;
    private String tarefaNome;
    private LocalDate dataVinculo;
}
