package br.tec.jessebezerra.app.model.entity;

import br.tec.jessebezerra.app.model.enums.TipoAplicacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBG_APLICACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aplicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String repo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAplicacao tipo;
}
