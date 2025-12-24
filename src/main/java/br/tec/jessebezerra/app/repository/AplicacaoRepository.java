package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.Aplicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AplicacaoRepository extends JpaRepository<Aplicacao, Long> {
}
