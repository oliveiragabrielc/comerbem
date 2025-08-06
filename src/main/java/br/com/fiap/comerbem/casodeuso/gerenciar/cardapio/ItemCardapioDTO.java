package br.com.fiap.comerbem.casodeuso.gerenciar.cardapio;

import br.com.fiap.comerbem.model.ItemCardapio;

public record ItemCardapioDTO(Long id,
        String nome,
        String descricao,
        Double preco,
        Boolean somenteNoLocal,
        String caminhoFoto,
        Long restauranteId) {

    public static ItemCardapioDTO transformar(ItemCardapio item) {
        return new ItemCardapioDTO(item.getId(), item.getNome(), item.getDescricao(),
                item.getPreco().doubleValue(), item.getSomenteNoLocal(), item.getCaminhoFoto(),
                item.getRestaurante().getId());
    }
}
