package com.marketrest.service;

import com.marketrest.dto.ProdutoDTO;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produtoExemplo;

    @BeforeEach
    void setUp() {
        produtoExemplo = new Produto();
        produtoExemplo.setNome("Arroz 5kg");
        produtoExemplo.setDescricao("Arroz Agulhinha Tipo 1");
        produtoExemplo.setValor(25.90);
    }

    @Test
    @DisplayName("Deve cadastrar um produto com sucesso")
    void cadastrarComSucesso() {
        ProdutoDTO resultado = produtoService.cadastrar(produtoExemplo);

        assertNotNull(resultado);
        assertEquals("Arroz 5kg", resultado.getNome());
        verify(entityManager, times(1)).persist(produtoExemplo);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar produto com ID preenchido")
    void cadastrarErroIdPreenchido() {
        produtoExemplo.setId(1L);

        assertThrows(ResponseStatusException.class, () -> {
            produtoService.cadastrar(produtoExemplo);
        });

        verify(entityManager, never()).persist(any());
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    void buscarPorIdComSucesso() {
        Long id = 1L;
        produtoExemplo.setId(id);
        when(entityManager.find(Produto.class, id)).thenReturn(produtoExemplo);

        ProdutoDTO resultado = produtoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
    }

    @Test
    @DisplayName("Deve alterar apenas os campos fornecidos do produto")
    void alterarComSucesso() {
        Long id = 1L;
        produtoExemplo.setId(id);

        Produto dadosNovos = new Produto();
        dadosNovos.setNome("Arroz Integral"); // Só vamos alterar o nome

        when(entityManager.find(Produto.class, id)).thenReturn(produtoExemplo);

        ProdutoDTO resultado = produtoService.alterar(id, dadosNovos);

        assertEquals("Arroz Integral", resultado.getNome());
        assertEquals(25.90, produtoExemplo.getValor());
        verify(entityManager).merge(produtoExemplo);
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void buscarTodosComSucesso() {
        TypedQuery<Produto> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Produto.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(produtoExemplo));

        List<ProdutoDTO> resultado = produtoService.buscarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Deve deletar um produto com sucesso")
    void deletarComSucesso() {
        Long id = 1L;
        when(entityManager.find(Produto.class, id)).thenReturn(produtoExemplo);

        assertDoesNotThrow(() -> produtoService.deletar(id));

        verify(entityManager).remove(produtoExemplo);
    }
}