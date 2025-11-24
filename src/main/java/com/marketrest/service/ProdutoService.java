package com.marketrest.service;

import com.marketrest.dto.ProdutoDTO;
import com.marketrest.entitys.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    private static final Logger log = LoggerFactory.getLogger(ProdutoService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ProdutoDTO cadastrar(Produto produto) {
        log.info("Cadastrando produto: {}", produto);

        if (produto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID deve ser nulo ao cadastrar");
        }

        entityManager.persist(produto);
        return new ProdutoDTO(produto);
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = entityManager.find(Produto.class, id);

        if (produto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }

        return new ProdutoDTO(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarTodos() {
        List<Produto> produtos = entityManager
                .createQuery("SELECT p FROM Produto p", Produto.class)
                .getResultList();

        return produtos.stream().map(ProdutoDTO::new).toList();
    }

    @Transactional
    public ProdutoDTO alterar(Long id, Produto atualizado) {
        Produto existente = entityManager.find(Produto.class, id);

        if (existente == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");

        if (atualizado.getNome() != null) existente.setNome(atualizado.getNome());
        if (atualizado.getDescricao() != null) existente.setDescricao(atualizado.getDescricao());
        if (atualizado.getValor() != null) existente.setValor(atualizado.getValor());

        entityManager.merge(existente);

        return new ProdutoDTO(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = entityManager.find(Produto.class, id);

        if (produto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");

        entityManager.remove(produto);
    }
}
