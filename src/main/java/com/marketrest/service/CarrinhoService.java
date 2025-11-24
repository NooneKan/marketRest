package com.marketrest.service;

import com.marketrest.dto.CarrinhoDTO;
import com.marketrest.entitys.Carrinho;
import com.marketrest.entitys.Cliente;
import com.marketrest.entitys.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CarrinhoService {

    private static Logger log = LoggerFactory.getLogger(CarrinhoService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CarrinhoDTO adicionarProduto(Long clienteId, Long produtoId) {
        log.info("Adicionando produto {} ao carrinho do cliente {}", produtoId, clienteId);

        Carrinho carrinho = obterOuCriarCarrinho(clienteId);

        Produto produto = entityManager.find(Produto.class, produtoId);
        if (produto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }

        carrinho.getProdutos().add(produto);
        entityManager.merge(carrinho);

        return new CarrinhoDTO(carrinho);
    }

    @Transactional
    public CarrinhoDTO removerProduto(Long clienteId, Long produtoId) {
        log.info("Removendo produto {} do carrinho do cliente {}", produtoId, clienteId);

        Carrinho carrinho = obterOuCriarCarrinho(clienteId);

        carrinho.getProdutos().removeIf(p -> p.getId().equals(produtoId));
        entityManager.merge(carrinho);

        return new CarrinhoDTO(carrinho);
    }

    @Transactional(readOnly = true)
    public CarrinhoDTO listarCarrinho(Long clienteId) {
        Carrinho carrinho = obterOuCriarCarrinho(clienteId);
        return new CarrinhoDTO(carrinho);
    }

    @Transactional
    public CarrinhoDTO limparCarrinho(Long clienteId) {
        log.info("Limpando carrinho do cliente {}", clienteId);

        Carrinho carrinho = obterOuCriarCarrinho(clienteId);
        carrinho.getProdutos().clear();

        entityManager.merge(carrinho);

        return new CarrinhoDTO(carrinho);
    }

    private Carrinho obterOuCriarCarrinho(Long clienteId) {
        log.info("Obtendo carrinho do cliente {}", clienteId);

        Cliente cliente = entityManager.find(Cliente.class, clienteId);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }

        List<Carrinho> lista = entityManager.createQuery(
                        "SELECT c FROM Carrinho c WHERE c.cliente.id = :id", Carrinho.class)
                .setParameter("id", clienteId)
                .getResultList();

        if (!lista.isEmpty()) {
            return lista.get(0);
        }

        Carrinho novo = new Carrinho();
        novo.setCliente(cliente);

        entityManager.persist(novo);

        return novo;
    }
}
