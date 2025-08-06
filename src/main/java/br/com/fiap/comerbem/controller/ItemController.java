package br.com.fiap.comerbem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.comerbem.casodeuso.gerenciar.cardapio.GerenciarCardapio;
import br.com.fiap.comerbem.casodeuso.gerenciar.cardapio.ItemCardapioDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/cardapio")
public class ItemController {
    

    @Autowired
    GerenciarCardapio gerenciar;

    @PostMapping
    public ResponseEntity<ItemCardapioDTO> cadastrar(@RequestBody ItemCardapioDTO restaurante) {
        return ResponseEntity.ok(gerenciar.cadastrar(restaurante));
    }

    @GetMapping
    public ResponseEntity<List<ItemCardapioDTO>> recuperar() {
        return ResponseEntity.ok(gerenciar.recuperarLista());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ItemCardapioDTO>> recuperar(@Param("id") Long id) {
        return ResponseEntity.ok(gerenciar.recuperarLista(id));
    }

    @PutMapping
    public ResponseEntity<ItemCardapioDTO> alterar(@RequestBody ItemCardapioDTO restaurante) {
        return ResponseEntity.ok(gerenciar.alterar(restaurante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@Param("id") Long id) {
        gerenciar.deletar(id);
        return ResponseEntity.ok().build();
    }
}
