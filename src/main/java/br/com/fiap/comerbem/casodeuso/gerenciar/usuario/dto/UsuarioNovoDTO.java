package br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto;

import br.com.fiap.comerbem.model.Endereco;
import br.com.fiap.comerbem.model.enums.TipoUsuario;

public record UsuarioNovoDTO(
        String nome,
        String email,
        String login,
        String senha,
        TipoUsuario tipo,
        Endereco endereco) {
}
