package br.com.fiap.comerbem.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.casodeuso.gerenciar.restaurante.RestauranteDTO;
import br.com.fiap.comerbem.model.Restaurante;
import br.com.fiap.comerbem.model.Usuario;
import br.com.fiap.comerbem.repository.RestauranteRepository;

@Service
public class ValidarRestauranteService {

    RestauranteRepository repository;

    @Autowired
    public ValidarRestauranteService(RestauranteRepository repository) {
        this.repository = repository;
    }

    public RestauranteDTO cadastrar(RestauranteDTO restaurante) {
        var r = repository.procurarPorNome(restaurante.nome());
        if (r != null) {
            throw new IllegalArgumentException("O nome do restaurante já está sendo utilizado!");
        }
        return salvar(restaurante);
    }

    public RestauranteDTO alterar(RestauranteDTO restaurante) {
        Restaurante restauranteAlterado = repository.findById(restaurante.id())
                .orElseThrow(() -> new NoSuchElementException("Restaurante não Encontrado"));

        restauranteAlterado.setNome(restaurante.nome());
        restauranteAlterado.setEndereco(restaurante.endereco());
        restauranteAlterado.setHorarioFuncionamento(restaurante.HorarioFuncionamento());
        restauranteAlterado.setTipoCozinha(restaurante.tipo());

        return salvar(restaurante);
    }

    public RestauranteDTO recuperar(Long id) {
        return RestauranteDTO.transformar(existe(id));
    }

    public void delete(Long id) {
        repository.delete(existe(id));
    }

    private RestauranteDTO salvar(RestauranteDTO restaurante) {
        Usuario proprietario = new Usuario();
        proprietario.setId(restaurante.usuario().id());
        proprietario.setNome(restaurante.usuario().nome());
        proprietario.setTipo(restaurante.usuario().tipo());
        return RestauranteDTO.transformar(
                repository.save(new Restaurante(restaurante.nome(), restaurante.endereco(), restaurante.tipo(),
                        restaurante.HorarioFuncionamento(), proprietario)));
    }

    private Restaurante existe(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo!");
        }

        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Restaurante não Encontrado"));
    }
}
