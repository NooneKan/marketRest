package com.marketrest.controller;

import com.marketrest.delegate.CarrinhoDelegate;
import com.marketrest.dto.CarrinhoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private static final Logger log = LoggerFactory.getLogger(CarrinhoController.class);

    private final CarrinhoDelegate delegate;

    public CarrinhoController(CarrinhoDelegate delegate) {
        this.delegate = delegate;
    }

    @PostMapping("/{clienteId}/adicionar/{produtoId}")
    public ResponseEntity<CarrinhoDTO> adicionarProduto(
            @PathVariable Long clienteId,
            @PathVariable Long produtoId) {

        log.info("POST /api/carrinho/{}/adicionar/{}", clienteId, produtoId);

        CarrinhoDTO dto = delegate.adicionarProduto(clienteId, produtoId);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{clienteId}/remover/{produtoId}")
    public ResponseEntity<CarrinhoDTO> removerProduto(
            @PathVariable Long clienteId,
            @PathVariable Long produtoId) {

        log.info("DELETE /api/carrinho/{}/remover/{}", clienteId, produtoId);

        CarrinhoDTO dto = delegate.removerProduto(clienteId, produtoId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<CarrinhoDTO> listarCarrinho(@PathVariable Long clienteId) {
        log.info("GET /api/carrinho/{}", clienteId);
        return ResponseEntity.ok(delegate.listarCarrinho(clienteId));
    }

    @DeleteMapping("/{clienteId}/limpar")
    public ResponseEntity<CarrinhoDTO> limparCarrinho(@PathVariable Long clienteId) {
        log.info("DELETE /api/carrinho/{}/limpar", clienteId);
        return ResponseEntity.ok(delegate.limparCarrinho(clienteId));
    }
}
