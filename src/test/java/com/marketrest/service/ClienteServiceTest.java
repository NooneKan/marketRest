package com.marketrest.service;

import com.marketrest.dto.ClienteDTO;
import com.marketrest.entitys.Cliente;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteExemplo;

    @BeforeEach
    void setUp() {
        clienteExemplo = new Cliente();
        clienteExemplo.setNome("João Silva");
        clienteExemplo.setEmail("joao@email.com");
        clienteExemplo.setSenha("123456");
    }

    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso")
    void cadastrarComSucesso() {
        // Execução
        ClienteDTO resultado = clienteService.cadastrar(clienteExemplo);

        // Verificações
        assertNotNull(resultado);
        assertNotNull(clienteExemplo.getDataCadastro());
        verify(entityManager, times(1)).persist(clienteExemplo);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar cliente sem email ou senha")
    void cadastrarErroCamposNulos() {
        clienteExemplo.setEmail(null);

        assertThrows(ResponseStatusException.class, () -> {
            clienteService.cadastrar(clienteExemplo);
        });

        verify(entityManager, never()).persist(any());
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void buscarPorIdComSucesso() {
        Long id = 1L;
        clienteExemplo.setId(id);
        when(entityManager.find(Cliente.class, id)).thenReturn(clienteExemplo);

        ClienteDTO resultado = clienteService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    @DisplayName("Deve lançar 404 ao buscar ID inexistente")
    void buscarPorIdInexistente() {
        when(entityManager.find(Cliente.class, 1L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            clienteService.buscarPorId(1L);
        });
    }

    @Test
    @DisplayName("Deve alterar dados do cliente com sucesso")
    void alterarComSucesso() {
        Long id = 1L;
        Cliente dadosNovos = new Cliente();
        dadosNovos.setNome("João Alterado");

        when(entityManager.find(Cliente.class, id)).thenReturn(clienteExemplo);
        when(entityManager.merge(any())).thenReturn(clienteExemplo);

        ClienteDTO resultado = clienteService.alterar(id, dadosNovos);

        assertEquals("João Alterado", clienteExemplo.getNome());
        verify(entityManager).merge(clienteExemplo);
    }

    @Test
    @DisplayName("Deve deletar cliente com sucesso")
    void deletarComSucesso() {
        Long id = 1L;
        when(entityManager.find(Cliente.class, id)).thenReturn(clienteExemplo);

        clienteService.deletar(id);

        verify(entityManager, times(1)).remove(clienteExemplo);
    }

    @Test
    @DisplayName("Deve buscar todos os clientes")
    void buscarTodos() {
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Cliente.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(clienteExemplo));

        List<ClienteDTO> resultado = clienteService.buscarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(clienteExemplo.getNome(), resultado.get(0).getNome());
        assertEquals(clienteExemplo.getEmail(), resultado.get(0).getEmail());
        assertNotNull(resultado);
    }
}
