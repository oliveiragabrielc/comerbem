package br.com.fiap.comerbem.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.casodeuso.gerenciar.cardapio.ItemCardapioDTO;
import br.com.fiap.comerbem.model.ItemCardapio;
import br.com.fiap.comerbem.model.Restaurante;
import br.com.fiap.comerbem.repository.ItemCardapioRepository;

@Service
public class ValidarItemCardapioService {

    private ItemCardapioRepository repository;
    ValidarRestauranteService validarRestauranteService;

    @Autowired
    public ValidarItemCardapioService(ItemCardapioRepository repository,
            ValidarRestauranteService validarRestauranteService) {
        this.repository = repository;
        this.validarRestauranteService = validarRestauranteService;
    }

    public ItemCardapioDTO cadastrar(ItemCardapioDTO item) {
        if (item.nome() == null || item.nome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do item não pode ser vazio.");
        }
        if (item.preco() == null || item.preco() < 0) {
            throw new IllegalArgumentException("O preço do item não pode ser negativo.");
        }
        return salvar(item);
    }

    public List<ItemCardapioDTO> listarTodos() {
        return repository.findAll().stream().map(item -> ItemCardapioDTO.transformar(item)).toList();
    }

    public List<ItemCardapioDTO> listarTodos(Long restaurantId) {
        return repository.listarItemRestaurante(restaurantId).stream().map(item -> ItemCardapioDTO.transformar(item))
                .toList();
    }

    public ItemCardapioDTO recuperar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo.");
        }
        return ItemCardapioDTO.transformar(
                repository.findById(id).orElseThrow(() -> new NoSuchElementException("Item não Encontrado")));
    }

    public ItemCardapioDTO alterar(Long id, ItemCardapioDTO itemAtualizado) {

        ItemCardapio itemExistente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Item não Encontrado"));

        itemExistente.setNome(itemAtualizado.nome());
        itemExistente.setDescricao(itemAtualizado.descricao());
        itemExistente.setPreco(BigDecimal.valueOf(itemAtualizado.preco()));
        itemExistente.setSomenteNoLocal(itemAtualizado.somenteNoLocal());
        itemExistente.setCaminhoFoto(itemAtualizado.caminhoFoto());

        return salvar(itemExistente);

    }

    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O id não pode ser nulo.");
        }
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private ItemCardapioDTO salvar(ItemCardapioDTO item) {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(item.restauranteId());

        return ItemCardapioDTO.transformar(repository.save(new ItemCardapio(item.id(),
                item.nome(),
                item.descricao(),
                BigDecimal.valueOf(item.preco()),
                item.somenteNoLocal(),
                item.caminhoFoto(),
                restaurante)));
    }

    private ItemCardapioDTO salvar(ItemCardapio item) {
        return ItemCardapioDTO.transformar(repository.save(new ItemCardapio(item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getPreco(),
                item.getSomenteNoLocal(),
                item.getCaminhoFoto(),
                item.getRestaurante())));
    }
}
