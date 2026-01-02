package br.tec.jessebezerra.app.repository;

import br.tec.jessebezerra.app.model.entity.DetalhesProjeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalhesProjetoRepository extends JpaRepository<DetalhesProjeto, Long> {
    List<DetalhesProjeto> findByProjetoId(Long projetoId);
}
