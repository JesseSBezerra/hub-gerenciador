package br.tec.jessebezerra.app.dto;

import br.tec.jessebezerra.app.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String nome;
    private UserRole funcao;
}
