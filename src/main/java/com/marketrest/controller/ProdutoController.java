package com.marketrest.controller;

import com.marketrest.delegate.ProdutoDelegate;
import com.marketrest.dto.ProdutoDTO;
import com.marketrest.entitys.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private static final Logger log = LoggerFactory.getLogger(ProdutoController.class);

    private final ProdutoDelegate produtoDelegate;

    public ProdutoController(ProdutoDelegate produtoDelegate) {
        this.produtoDelegate = produtoDelegate;
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@RequestBody Produto produto) {
        log.info("POST /api/produtos - body: {}", produto);
        ProdutoDTO dto = produtoDelegate.cadastrar(produto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/produtos/{}", id);
        return ResponseEntity.ok(produtoDelegate.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        log.info("GET /api/produtos");
        return ResponseEntity.ok(produtoDelegate.buscarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> alterar(@PathVariable Long id, @RequestBody Produto produto) {
        log.info("PUT /api/produtos/{} - body: {}", id, produto);
        return ResponseEntity.ok(produtoDelegate.alterar(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("DELETE /api/produtos/{}", id);
        produtoDelegate.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
