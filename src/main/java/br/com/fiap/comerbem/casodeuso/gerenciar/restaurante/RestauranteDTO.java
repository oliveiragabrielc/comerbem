package br.com.fiap.comerbem.casodeuso.gerenciar.restaurante;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioProprietario;
import br.com.fiap.comerbem.model.Endereco;
import br.com.fiap.comerbem.model.HorarioFuncionamento;
import br.com.fiap.comerbem.model.Restaurante;
import br.com.fiap.comerbem.model.TipoCozinha;

public record RestauranteDTO(Long id, String nome, Endereco endereco, TipoCozinha tipo,
                HorarioFuncionamento HorarioFuncionamento, UsuarioProprietario usuario) {

        public static RestauranteDTO transformar(Restaurante restaurante) {
                return new RestauranteDTO(restaurante.getId(), restaurante.getNome(),
                                restaurante.getEndereco(), restaurante.getTipoCozinha(),
                                restaurante.getHorarioFuncionamento(),
                                new UsuarioProprietario(restaurante.getDono().getId(), restaurante.getDono().getNome(),
                                                restaurante.getDono().getTipo()));
        }
}
