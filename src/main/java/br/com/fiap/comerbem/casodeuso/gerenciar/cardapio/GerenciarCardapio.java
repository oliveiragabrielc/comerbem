package br.com.fiap.comerbem.casodeuso.gerenciar.cardapio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.service.ValidarItemCardapioService;
import br.com.fiap.comerbem.service.ValidarRestauranteService;

@Service
public class GerenciarCardapio {

    ValidarRestauranteService validarRestauranteService;
    ValidarItemCardapioService validarItemCardapioService;

    @Autowired
    public GerenciarCardapio(ValidarRestauranteService validarRestauranteService,
            ValidarItemCardapioService validarItemCardapioService) {
        this.validarRestauranteService = validarRestauranteService;
        this.validarItemCardapioService = validarItemCardapioService;
    }

    public ItemCardapioDTO cadastrar(ItemCardapioDTO item) {
        var restaurante = validarRestauranteService.recuperar(item.restauranteId());

        if (restaurante.tipo() == null) {
            throw new IllegalArgumentException("O restaurante a qual o item está sendo cadastrado não existe!");
        }

        return validarItemCardapioService.cadastrar(item);

    }

    public ItemCardapioDTO alterar(ItemCardapioDTO item) {

        var itemExistente = validarItemCardapioService.recuperar(item.id());

        if (itemExistente == null) {
            throw new IllegalArgumentException("O item a qual está tentando alterar não existe!");
        }

        return validarItemCardapioService.alterar(item.id(), item);
    }

    public List<ItemCardapioDTO> recuperarLista() {
        return validarItemCardapioService.listarTodos();
    }

    public List<ItemCardapioDTO> recuperarLista(Long restauranteID) {
        if (restauranteID == null) {
            throw new IllegalArgumentException("É necessário passar o id do restaurante!");
        }
        return validarItemCardapioService.listarTodos(restauranteID);
    }

    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id do item não pode ser nulo");
        }

        ItemCardapioDTO recuperado = validarItemCardapioService.recuperar(id);

        validarItemCardapioService.delete(recuperado.id());
    }
}
