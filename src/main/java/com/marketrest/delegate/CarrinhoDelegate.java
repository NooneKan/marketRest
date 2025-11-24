package com.marketrest.delegate;

import com.marketrest.dto.CarrinhoDTO;
import com.marketrest.service.CarrinhoService;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoDelegate {

    private final CarrinhoService carrinhoService;

    public CarrinhoDelegate(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    public CarrinhoDTO adicionarProduto(Long clienteId, Long produtoId) {
        return carrinhoService.adicionarProduto(clienteId, produtoId);
    }

    public CarrinhoDTO removerProduto(Long clienteId, Long produtoId) {
        return carrinhoService.removerProduto(clienteId, produtoId);
    }

    public CarrinhoDTO listarCarrinho(Long clienteId) {
        return carrinhoService.listarCarrinho(clienteId);
    }

    public CarrinhoDTO limparCarrinho(Long clienteId) {
        return carrinhoService.limparCarrinho(clienteId);
    }
}
