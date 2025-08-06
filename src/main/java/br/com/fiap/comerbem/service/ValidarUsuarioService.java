package br.com.fiap.comerbem.service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioNovoDTO;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLogado;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLoginDTO;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioMudarSenha;
import br.com.fiap.comerbem.model.Usuario;
import br.com.fiap.comerbem.repository.UsuarioRepository;

@Service
public class ValidarUsuarioService {

    UsuarioRepository repository;
    SenhaService senhaService;

    @Autowired
    public ValidarUsuarioService(UsuarioRepository repository, SenhaService senhaService) {
        this.repository = repository;
        this.senhaService = senhaService;
    }

    public UsuarioDTO salvar(UsuarioNovoDTO usuarioNovoDTO) {
        return UsuarioDTO.transformar(
                repository.save(new Usuario(usuarioNovoDTO.nome(), usuarioNovoDTO.email(), usuarioNovoDTO.login(),
                        senhaService.encryptarSenha(usuarioNovoDTO.senha()), usuarioNovoDTO.tipo(),
                        usuarioNovoDTO.endereco(), LocalDate.now())));
    }

    public UsuarioDTO alterar(UsuarioDTO usuarioDTO) {
        return UsuarioDTO.transformar(
                repository.save(new Usuario(usuarioDTO.id(), usuarioDTO.nome(), usuarioDTO.email(), usuarioDTO.login(),
                        usuarioDTO.endereco(), usuarioDTO.tipo(), LocalDate.now())));

    }

    public void deletar(Long id) {
        Usuario usuarioDeletado = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário com ID " + id + " não encontrado."));
        repository.delete(usuarioDeletado);
    }

    public UsuarioDTO existePorLoginEEmail(String login, String email) {
        Usuario usuarioRecuperado = repository.procurarPorParametros(email, login);

        if (usuarioRecuperado == null) {
            return null;
        }

        return UsuarioDTO.transformar(usuarioRecuperado);
    }

    public UsuarioDTO existe(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo.");
        }
        Usuario usuario = repository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Usuário com ID " + id + " não encontrado."));
        return UsuarioDTO.transformar(usuario);
    }

    public UsuarioLogado autenticar(UsuarioLoginDTO usuario) {

        Usuario usuarioCandidato = repository.procurarPorLogin(usuario.login());

        if (usuarioCandidato == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        senhaService.validarSenha(usuario.senha(), usuarioCandidato.getSenha());

        return new UsuarioLogado(usuarioCandidato.getLogin(), null);
    }


    public UsuarioDTO alterarSenha(UsuarioMudarSenha usuario) {

        Usuario usuarioRecuperado = repository.procurarPorParametros(usuario.email(), usuario.login());

        if (usuarioRecuperado == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        usuarioRecuperado.setSenha(usuario.novaSenha());

        return UsuarioDTO.transformar(repository.save(usuarioRecuperado));

    }

}
