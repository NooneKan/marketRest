package com.marketrest.service;

import com.marketrest.dto.CarrinhoDTO;
import com.marketrest.entitys.Carrinho;
import com.marketrest.entitys.Cliente;
import com.marketrest.entitys.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CarrinhoService carrinhoService;

    private Cliente clienteMock;
    private Produto produtoMock;
    private Carrinho carrinhoMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente();
        clienteMock.setId(1L);
        clienteMock.setNome("João");

        produtoMock = new Produto();
        produtoMock.setId(10L);
        produtoMock.setNome("Notebook");

        carrinhoMock = new Carrinho();
        carrinhoMock.setId(100L);
        carrinhoMock.setCliente(clienteMock);
        carrinhoMock.setProdutos(new ArrayList<>());
    }

    @Test
    @DisplayName("Deve adicionar um produto ao carrinho com sucesso")
    void adicionarProdutoComSucesso() {
        when(entityManager.find(Cliente.class, 1L)).thenReturn(clienteMock);

        TypedQuery<Carrinho> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Carrinho.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(carrinhoMock));

        when(entityManager.find(Produto.class, 10L)).thenReturn(produtoMock);

        CarrinhoDTO resultado = carrinhoService.adicionarProduto(1L, 10L);

        assertNotNull(resultado);
        verify(entityManager).merge(carrinhoMock);
        assertTrue(carrinhoMock.getProdutos().contains(produtoMock));
    }

    @Test
    @DisplayName("Deve lançar erro 404 quando cliente não existir")
    void erroClienteNaoEncontrado() {
        when(entityManager.find(Cliente.class, 99L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            carrinhoService.adicionarProduto(99L, 10L);
        });
    }

    @Test
    @DisplayName("Deve limpar o carrinho com sucesso")
    void limparCarrinhoComSucesso() {
        carrinhoMock.getProdutos().add(produtoMock);

        when(entityManager.find(Cliente.class, 1L)).thenReturn(clienteMock);
        TypedQuery<Carrinho> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Carrinho.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(carrinhoMock));

        carrinhoService.limparCarrinho(1L);

        assertTrue(carrinhoMock.getProdutos().isEmpty());
        verify(entityManager).merge(carrinhoMock);
    }

    @Test
    @DisplayName("Deve criar um novo carrinho se o cliente não possuir um")
    void criarNovoCarrinhoSeNaoExistir() {
        when(entityManager.find(Cliente.class, 1L)).thenReturn(clienteMock);

        TypedQuery<Carrinho> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Carrinho.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        carrinhoService.listarCarrinho(1L);

        verify(entityManager).persist(any(Carrinho.class));
    }
}