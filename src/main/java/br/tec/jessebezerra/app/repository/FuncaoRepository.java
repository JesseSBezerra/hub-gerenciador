package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Long> {
    
    List<Funcao> findByAplicacaoId(Long aplicacaoId);
}
