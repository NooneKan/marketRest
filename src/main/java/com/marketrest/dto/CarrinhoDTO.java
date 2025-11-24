package com.marketrest.dto;

import com.marketrest.entitys.Carrinho;

import java.util.List;

public class CarrinhoDTO {
    private Long id;
    private Long clienteId;
    private List<ProdutoDTO> produtos;
    private Double total;

    public CarrinhoDTO(Carrinho carrinho) {
        this.id = carrinho.getId();
        this.clienteId = carrinho.getCliente().getId();

        // Se for null, vira lista vazia
        if (carrinho.getProdutos() != null) {
            this.produtos = carrinho.getProdutos().stream()
                    .map(ProdutoDTO::new)
                    .toList();
        } else {
            this.produtos = List.of();
        }

        this.total = this.produtos.stream()
                .mapToDouble(p -> p.getValor() != null ? p.getValor() : 0.0)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
