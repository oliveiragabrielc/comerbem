package br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto;

import br.com.fiap.comerbem.model.enums.TipoUsuario;

public record UsuarioProprietario(Long id, String nome, TipoUsuario tipo) {
    
}
