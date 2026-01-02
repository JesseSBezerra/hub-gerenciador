package br.tec.jessebezerra.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBG_DETALHES_PROJETO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalhesProjeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;
}
