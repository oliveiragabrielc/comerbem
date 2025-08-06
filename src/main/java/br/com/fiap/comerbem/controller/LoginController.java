package br.com.fiap.comerbem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.comerbem.casodeuso.login.Login;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLogado;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLoginDTO;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioMudarSenha;


@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    Login login;

    @PostMapping
    public ResponseEntity<UsuarioLogado> login(@RequestBody UsuarioLoginDTO usuario) {
        return ResponseEntity.ok(login.autenticar(usuario));
    }

    @PutMapping("/trocar")
    public ResponseEntity alterar(@RequestBody UsuarioMudarSenha usuario) {
        login.alterarSenha(usuario);
        return ResponseEntity.ok().build();
    }

}
