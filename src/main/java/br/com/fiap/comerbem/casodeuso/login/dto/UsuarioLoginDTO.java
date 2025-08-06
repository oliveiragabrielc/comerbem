package br.com.fiap.comerbem.casodeuso.login.dto;

import jakarta.validation.constraints.NotNull;

public record UsuarioLoginDTO(@NotNull String login, @NotNull String senha) {

}
