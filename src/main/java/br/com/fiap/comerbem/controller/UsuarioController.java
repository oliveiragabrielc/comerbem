package br.com.fiap.comerbem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.GerenciarUsuario;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioNovoDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    GerenciarUsuario gerenciar;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> procurarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(gerenciar.recuperar(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@Valid @RequestBody UsuarioNovoDTO usuario) {
        return ResponseEntity.ok(gerenciar.cadastrar(usuario));
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizar(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(gerenciar.alterar(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@Valid @RequestBody UsuarioDTO dto) {
        gerenciar.deletar(dto);
        return ResponseEntity.noContent().build();
    }
}
