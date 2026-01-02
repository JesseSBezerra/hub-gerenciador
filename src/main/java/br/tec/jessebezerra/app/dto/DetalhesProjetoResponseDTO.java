package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalhesProjetoResponseDTO {
    
    private Long id;
    private Long projetoId;
    private String descricao;
}
