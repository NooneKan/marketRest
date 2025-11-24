package com.marketrest.controller;

import com.marketrest.delegate.ClienteDelegate;
import com.marketrest.dto.ClienteDTO;
import com.marketrest.entitys.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteDelegate clienteDelegate;

    public ClienteController(ClienteDelegate clienteDelegate) {
        this.clienteDelegate = clienteDelegate;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody Cliente cliente) {
        log.info("POST /api/clientes - body: {}", cliente);
        ClienteDTO dto = clienteDelegate.cadastrar(cliente);
        log.info("Cliente criado: {}", dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        log.info("GET /api/clientes/{}", id);
        ClienteDTO dto = clienteDelegate.buscarPorId(id);
        log.info("Cliente retornado: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        log.info("GET /api/clientes");
        List<ClienteDTO> clientes = clienteDelegate.buscarTodos();
        log.info("Clientes encontrados: {}", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> alterarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        log.info("PUT /api/clientes/{} - body: {}", id, cliente);
        ClienteDTO dto = clienteDelegate.alterar(id, cliente);
        log.info("Cliente alterado: {}", dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        log.info("DELETE /api/clientes/{}", id);
        clienteDelegate.deletar(id);
        log.info("Cliente deletado: {}", id);
        return ResponseEntity.noContent().build();
    }
}
