package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.BaseConhecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseConhecimentoRepository extends JpaRepository<BaseConhecimento, Long> {
}
