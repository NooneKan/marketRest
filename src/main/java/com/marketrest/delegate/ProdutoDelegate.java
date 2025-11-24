package com.marketrest.delegate;

import com.marketrest.dto.ProdutoDTO;
import com.marketrest.entitys.Produto;
import com.marketrest.service.ProdutoService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoDelegate {

    private final ProdutoService produtoService;

    public ProdutoDelegate(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public ProdutoDTO cadastrar(Produto produto) {
        return produtoService.cadastrar(produto);
    }

    public ProdutoDTO alterar(Long id, Produto produto) {
        return produtoService.alterar(id, produto);
    }

    public ProdutoDTO buscarPorId(Long id) {
        return produtoService.buscarPorId(id);
    }

    public List<ProdutoDTO> buscarTodos() {
        return produtoService.buscarTodos();
    }

    public void deletar(Long id) {
        produtoService.deletar(id);
    }
}
