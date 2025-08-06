package br.com.fiap.comerbem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.comerbem.casodeuso.gerenciar.restaurante.GerenciarRestaurante;
import br.com.fiap.comerbem.casodeuso.gerenciar.restaurante.RestauranteDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioProprietario;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/restaurante")
public class RestauranteController {

    @Autowired
    GerenciarRestaurante gerenciar;

    @PostMapping
    public ResponseEntity<RestauranteDTO> cadastrar(@RequestBody RestauranteDTO restaurante) {
        return ResponseEntity.ok(gerenciar.cadastrar(restaurante));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteDTO> recuperar(@Param("id") Long id) {
        return ResponseEntity.ok(gerenciar.recuperar(id));
    }

    @PutMapping
    public ResponseEntity<RestauranteDTO> alterar(@RequestBody RestauranteDTO restaurante) {
        return ResponseEntity.ok(gerenciar.alterar(restaurante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@Param("id") Long id, UsuarioProprietario proprietario) {
        gerenciar.deletar(id, proprietario);
        return ResponseEntity.ok().build();
    }

}
