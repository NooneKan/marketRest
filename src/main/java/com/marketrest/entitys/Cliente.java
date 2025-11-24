package com.marketrest.entitys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    public Cliente() {
    }

    public Cliente(String nome, String email, String senha, String cpf) {
        setNome(nome);      // herdado de Usuario
        setEmail(email);    // herdado de Usuario
        setSenha(senha);    // herdado de Usuario
        this.cpf = cpf;
        this.dataCadastro = LocalDateTime.now();
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

    public void setDataCadastro(LocalDateTime now) {
        this.dataCadastro = now;
    }
}
