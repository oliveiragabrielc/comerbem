package br.com.fiap.comerbem.casodeuso.gerenciar.usuario;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioNovoDTO;
import br.com.fiap.comerbem.service.ValidarUsuarioService;

@Service
public class GerenciarUsuario {

    ValidarUsuarioService validarUsuarioService;

    @Autowired
    public GerenciarUsuario(ValidarUsuarioService validarUsuarioService) {
        this.validarUsuarioService = validarUsuarioService;
    }

    public UsuarioDTO cadastrar(UsuarioNovoDTO usuarioDTO) {
        var usuarioRecuperado = validarUsuarioService.existePorLoginEEmail(usuarioDTO.login(), usuarioDTO.email());

        if (usuarioRecuperado != null) {

            if (Objects.equals(usuarioDTO.email(), usuarioRecuperado.email())) {
                throw new IllegalStateException("Esse email já está em uso!");
            }

            if (Objects.equals(usuarioDTO.login(), usuarioRecuperado.login())) {
                throw new IllegalStateException("Login já está em uso");
            }

        }
        return validarUsuarioService.salvar(usuarioDTO);
    }

    public UsuarioDTO recuperar(Long id) {
        return validarUsuarioService.existe(id);
    }

    public UsuarioDTO alterar(UsuarioDTO usuario) {
        var usuarioAlterado = validarUsuarioService.existe(usuario.id());

        if (usuario.id() != usuarioAlterado.id()) {
            throw new IllegalStateException("Não é possível alterar o ID");
        }

        if (usuario.login() != usuarioAlterado.login()) {
            throw new IllegalStateException("Não é possível alterar o Login");
        }
        return validarUsuarioService.alterar(usuario);
    }

    public void deletar(UsuarioDTO usuarioDTO) {
        var usuarioExcluido = validarUsuarioService.existe(usuarioDTO.id());

        if (usuarioExcluido == null) {
            throw new IllegalStateException("Não é possível excluir um usuário que não existe!");
        }

        validarUsuarioService.deletar(usuarioExcluido.id());
    }

}
