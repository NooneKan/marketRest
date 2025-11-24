package com.marketrest.service;

import com.marketrest.dto.ClienteDTO;
import com.marketrest.entitys.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public ClienteDTO cadastrar(Cliente cliente) {
        log.info("Cadastrando cliente: {}", cliente);

        if (cliente.getEmail() == null || cliente.getSenha() == null) {
            log.error("Falha no cadastro: email ou senha nulo");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email e senha são obrigatórios.");
        }

        if (cliente.getId() != null) {
            log.warn("Tentativa de cadastro com ID definido: {}", cliente.getId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID deve ser nulo para novo cadastro.");
        }

        cliente.setDataCadastro(LocalDateTime.now());

        entityManager.persist(cliente);
        log.info("Cliente cadastrado com sucesso: {}", cliente);

        return new ClienteDTO(cliente);
    }

    @Transactional
    public ClienteDTO alterar(Long id, Cliente dadosAtualizados) {
        log.info("Alterando cliente ID {}: {}", id, dadosAtualizados);

        Cliente clienteExistente = entityManager.find(Cliente.class, id);

        if (clienteExistente == null) {
            log.error("Cliente não encontrado para alteração: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado para alteração.");
        }

        if (dadosAtualizados.getNome() != null) clienteExistente.setNome(dadosAtualizados.getNome());
        if (dadosAtualizados.getEmail() != null) clienteExistente.setEmail(dadosAtualizados.getEmail());
        if (dadosAtualizados.getSenha() != null) clienteExistente.setSenha(dadosAtualizados.getSenha());
        if (dadosAtualizados.getCpf() != null) clienteExistente.setCpf(dadosAtualizados.getCpf());

        Cliente clienteAtualizado = entityManager.merge(clienteExistente);
        log.info("Cliente alterado com sucesso: {}", clienteAtualizado);

        return new ClienteDTO(clienteAtualizado);
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        log.info("Buscando cliente por ID: {}", id);
        Cliente cliente = entityManager.find(Cliente.class, id);

        if (cliente == null) {
            log.error("Cliente não encontrado: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.");
        }

        return new ClienteDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> buscarTodos() {
        log.info("Buscando todos os clientes");
        List<Cliente> clientes = entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class)
                .getResultList();

        log.info("Clientes encontrados: {}", clientes.size());
        return clientes.stream().map(ClienteDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando cliente ID: {}", id);
        Cliente cliente = entityManager.find(Cliente.class, id);

        if (cliente == null) {
            log.error("Cliente não encontrado para exclusão: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado para exclusão.");
        }

        entityManager.remove(cliente);
        log.info("Cliente deletado com sucesso: {}", id);
    }
}
