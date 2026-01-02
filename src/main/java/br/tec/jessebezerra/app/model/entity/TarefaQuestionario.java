package br.tec.jessebezerra.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBG_TAREFA_QUESTIONARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaQuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pergunta;

    @Column(columnDefinition = "TEXT")
    private String resposta;
}
