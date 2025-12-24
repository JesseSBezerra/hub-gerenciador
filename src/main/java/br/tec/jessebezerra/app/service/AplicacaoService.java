package br.tec.jessebezerra.app.service;

import br.tec.jessebezerra.app.model.entity.Aplicacao;
import br.tec.jessebezerra.app.repository.AplicacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AplicacaoService {

    private final AplicacaoRepository aplicacaoRepository;

    public Aplicacao save(Aplicacao aplicacao) {
        return aplicacaoRepository.save(aplicacao);
    }

    public List<Aplicacao> findAll() {
        return aplicacaoRepository.findAll();
    }

    public Optional<Aplicacao> findById(Long id) {
        return aplicacaoRepository.findById(id);
    }

    public Aplicacao update(Long id, Aplicacao aplicacao) {
        return aplicacaoRepository.findById(id)
                .map(existingAplicacao -> {
                    existingAplicacao.setNome(aplicacao.getNome());
                    existingAplicacao.setRepo(aplicacao.getRepo());
                    existingAplicacao.setTipo(aplicacao.getTipo());
                    return aplicacaoRepository.save(existingAplicacao);
                })
                .orElseThrow(() -> new RuntimeException("Aplicacao not found with id: " + id));
    }

    public void delete(Long id) {
        aplicacaoRepository.deleteById(id);
    }
}
