package com.marketrest.dto;

import com.marketrest.entitys.Cliente;

import java.time.LocalDateTime;

public class ClienteDTO extends UsuarioDTO {

    private String cpf;
    private LocalDateTime dataCadastro;


    public ClienteDTO() {
    }

    public ClienteDTO(Cliente cliente) {
        super(cliente);
        this.cpf = cliente.getCpf();
        this.dataCadastro = cliente.getDataCadastro();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
}
