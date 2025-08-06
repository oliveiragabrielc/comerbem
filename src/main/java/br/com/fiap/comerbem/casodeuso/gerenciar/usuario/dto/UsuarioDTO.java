package br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto;

import br.com.fiap.comerbem.model.Endereco;
import br.com.fiap.comerbem.model.Usuario;
import br.com.fiap.comerbem.model.enums.TipoUsuario;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        @NotNull(message = "O Campo ID não pode ser nulo") Long id,
        @NotNull(message = "O Campo nome não pode ser nulo") String nome,
        @NotNull(message = "O Campo email não pode ser nulo") String email,
        @NotNull(message = "O Campo login não pode ser nulo") String login,
        @NotNull(message = "O Campo tipo não pode ser nulo") TipoUsuario tipo,
        @NotNull(message = "O Campo endereço não pode ser nulo") Endereco endereco) {

    public static UsuarioDTO transformar(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getLogin(),
                usuario.getTipo(),
                usuario.getEndereco());
    }

}


