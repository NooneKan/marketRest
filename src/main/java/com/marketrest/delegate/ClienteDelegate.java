package com.marketrest.delegate;

import com.marketrest.dto.ClienteDTO;
import com.marketrest.entitys.Cliente;
import com.marketrest.service.ClienteService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteDelegate {

    private final ClienteService clienteService;

    public ClienteDelegate(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public ClienteDTO cadastrar(Cliente cliente) {
        return clienteService.cadastrar(cliente);
    }

    public ClienteDTO alterar(Long id, Cliente dadosAtualizados) {
        return clienteService.alterar(id, dadosAtualizados);
    }

    public ClienteDTO buscarPorId(Long id) {
        return clienteService.buscarPorId(id);
    }

    public void deletar(Long id) {
        clienteService.deletar(id);
    }

    public List<ClienteDTO> buscarTodos() {
        return clienteService.buscarTodos();
    }

}
