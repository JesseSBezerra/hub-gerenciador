package br.tec.jessebezerra.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetoResponseDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private List<DetalhesProjetoResponseDTO> detalhes;
}
