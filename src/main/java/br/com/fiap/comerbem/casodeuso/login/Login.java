package br.com.fiap.comerbem.casodeuso.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioDTO;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLogado;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLoginDTO;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioMudarSenha;
import br.com.fiap.comerbem.service.AutenticarService;
import br.com.fiap.comerbem.service.ValidarUsuarioService;

@Service
public class Login {

    ValidarUsuarioService validarUsuarioService;
    AutenticarService autenticarService;

    @Autowired
    public Login(ValidarUsuarioService validarUsuarioService, AutenticarService autenticarService) {
        this.validarUsuarioService = validarUsuarioService;
        this.autenticarService = autenticarService;
    }

    public UsuarioLogado autenticar(UsuarioLoginDTO usuario) {

        if (usuario == null || usuario.login() == null || usuario.senha() == null) {
            throw new IllegalArgumentException("Login e senha devem ser preenchidos.");
        }

        validarUsuarioService.autenticar(usuario);

        return autenticarService.obterToken(usuario);
    }

    public UsuarioDTO alterarSenha(UsuarioMudarSenha usuario) {

        if (usuario == null || usuario.login() == null || usuario.login() == null) {
            throw new IllegalArgumentException("Login e email devem ser preenchidos.");
        }

        return validarUsuarioService.alterarSenha(usuario);

    }

}
