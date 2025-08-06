package br.com.fiap.comerbem.casodeuso.gerenciar.restaurante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioProprietario;
import br.com.fiap.comerbem.model.enums.TipoUsuario;
import br.com.fiap.comerbem.service.ValidarRestauranteService;
import br.com.fiap.comerbem.service.ValidarUsuarioService;

@Service
public class GerenciarRestaurante {

    ValidarUsuarioService validarUsuarioService;
    ValidarRestauranteService validarRestauranteService;

    @Autowired
    public GerenciarRestaurante(ValidarUsuarioService validarUsuarioService,
            ValidarRestauranteService validarRestauranteService) {
        this.validarUsuarioService = validarUsuarioService;
        this.validarRestauranteService = validarRestauranteService;
    }

    public RestauranteDTO cadastrar(RestauranteDTO restaurante) {
        var usuario = validarUsuarioService.existe(restaurante.usuario().id());

        if (usuario.tipo() != TipoUsuario.PROPRIETARIO) {
            throw new IllegalArgumentException("Para criação do restaurante o usuário necessita ser proprietário!");
        }

        return validarRestauranteService.cadastrar(restaurante);

    }

    public RestauranteDTO alterar(RestauranteDTO restaurante) {
        if (restaurante == null || restaurante.id() == null) {
            throw new IllegalArgumentException("Restaurante a ser alterado não informado");
        }

        RestauranteDTO restauranteAlterado = validarRestauranteService.recuperar(restaurante.id());

        verificarDono(restaurante, restauranteAlterado.usuario().id());

        return validarRestauranteService.alterar(restauranteAlterado);
    }

    public RestauranteDTO recuperar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo");
        }
        return validarRestauranteService.recuperar(id);
    }

    public void deletar(Long id, UsuarioProprietario proprietario) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo");
        }

        RestauranteDTO recuperado = validarRestauranteService.recuperar(id);
        verificarDono(recuperado, proprietario.id());
        validarRestauranteService.delete(id);
    }

    private boolean verificarDono(RestauranteDTO requisicao, Long id) {
        if (requisicao.usuario().tipo() != TipoUsuario.PROPRIETARIO) {
            throw new IllegalAccessError("O Usuário não é do tipo proprietário");
        }
        if (!id.equals(requisicao.usuario().id())) {
            throw new IllegalAccessError("O Usuário não é proprietário desse restaurante");
        }
        return true;
    }

}
