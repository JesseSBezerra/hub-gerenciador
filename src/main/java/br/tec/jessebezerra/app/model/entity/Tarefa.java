package br.tec.jessebezerra.app.model.entity;

import br.tec.jessebezerra.app.model.enums.Complexidade;
import br.tec.jessebezerra.app.model.enums.Status;
import br.tec.jessebezerra.app.model.enums.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TBG_TAREFA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataCriacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Complexidade complexidade;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @ManyToOne
    @JoinColumn(name = "tarefa_id")
    private Tarefa tarefaPai;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "aplicacao_id")
    private Aplicacao aplicacao;

    @Column(columnDefinition = "TEXT")
    private String tarefaSugerida;

    @Column(length = 255)
    private String tituloSugerido;

    @ManyToMany
    @JoinTable(
        name = "TBG_TAREFA_BASE_CONHECIMENTO",
        joinColumns = @JoinColumn(name = "tarefa_id"),
        inverseJoinColumns = @JoinColumn(name = "base_conhecimento_id")
    )
    private Set<BaseConhecimento> baseConhecimentos = new HashSet<>();

    @Transient
    public LocalDate getDataEstimada() {
        if (dataCriacao == null || complexidade == null) {
            return null;
        }

        return switch (complexidade) {
            case MUITO_SIMPLES -> dataCriacao.plusWeeks(1);
            case SIMPLES -> dataCriacao.plusWeeks(2);
            case MEDIA -> dataCriacao.plusWeeks(3);
            case COMPLEXA -> dataCriacao.plusWeeks(4);
        };
    }
}
