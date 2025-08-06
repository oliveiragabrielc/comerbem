package br.com.fiap.comerbem.casodeuso.gerenciar.cardapio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.comerbem.casodeuso.gerenciar.cardapio.GerenciarCardapio;
import br.com.fiap.comerbem.casodeuso.gerenciar.cardapio.ItemCardapioDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.restaurante.RestauranteDTO;
import br.com.fiap.comerbem.model.TipoCozinha;
import br.com.fiap.comerbem.service.ValidarItemCardapioService;
import br.com.fiap.comerbem.service.ValidarRestauranteService;

@ExtendWith(MockitoExtension.class)
class GerenciarCardapioTest {

    @Mock
    private ValidarRestauranteService validarRestauranteService;

    @Mock
    private ValidarItemCardapioService validarItemCardapioService;

    @InjectMocks
    private GerenciarCardapio gerenciarCardapio;

    private ItemCardapioDTO itemCardapioDTO;
    private RestauranteDTO restauranteDTO;

    @BeforeEach
    void setUp() {
        itemCardapioDTO = new ItemCardapioDTO(1L, "Pizza", "Pizza de Calabresa", 35.00, true, "Descrição da Pizza", 1L);
        restauranteDTO = new RestauranteDTO(1L, "Pizzaria Teste", null, TipoCozinha.PIZZA, null, null);
    }

    @Test
    @DisplayName("Deve cadastrar um item de cardápio com sucesso")
    void deveCadastrarItemCardapioComSucesso() {
        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);
        when(validarItemCardapioService.cadastrar(any(ItemCardapioDTO.class))).thenReturn(itemCardapioDTO);

        ItemCardapioDTO result = gerenciarCardapio.cadastrar(itemCardapioDTO);

        assertNotNull(result);
        assertEquals(itemCardapioDTO.nome(), result.nome());
        verify(validarRestauranteService, times(1)).recuperar(itemCardapioDTO.restauranteId());
        verify(validarItemCardapioService, times(1)).cadastrar(itemCardapioDTO);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao cadastrar item com restaurante inexistente")
    void deveLancarExcecaoAoCadastrarItemComRestauranteInexistente() {
        RestauranteDTO restauranteInexistente = new RestauranteDTO(1L, "Pizzaria Teste", null, null, null, null); // Tipo nulo para simular restaurante inexistente
        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteInexistente);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarCardapio.cadastrar(itemCardapioDTO);
        });

        assertEquals("O restaurante a qual o item está sendo cadastrado não existe!", exception.getMessage());
        verify(validarRestauranteService, times(1)).recuperar(itemCardapioDTO.restauranteId());
        verify(validarItemCardapioService, never()).cadastrar(any(ItemCardapioDTO.class));
    }


    @Test
    @DisplayName("Deve alterar um item de cardápio com sucesso")
    void deveAlterarItemCardapioComSucesso() {
        when(validarItemCardapioService.recuperar(anyLong())).thenReturn(itemCardapioDTO);
        when(validarItemCardapioService.alterar(anyLong(), any(ItemCardapioDTO.class))).thenReturn(itemCardapioDTO);

        ItemCardapioDTO result = gerenciarCardapio.alterar(itemCardapioDTO);

        assertNotNull(result);
        assertEquals(itemCardapioDTO.nome(), result.nome());
        verify(validarItemCardapioService, times(1)).recuperar(itemCardapioDTO.id());
        verify(validarItemCardapioService, times(1)).alterar(itemCardapioDTO.id(), itemCardapioDTO);
    }
    void deveLancarExcecaoAoAlterarItemInexistente() {
        when(validarItemCardapioService.recuperar(anyLong())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarCardapio.alterar(itemCardapioDTO);
        });

        assertEquals("O item a qual está tentando alterar não existe!", exception.getMessage());
        verify(validarItemCardapioService, times(1)).recuperar(itemCardapioDTO.id());
        verify(validarItemCardapioService, never()).alterar(anyLong(), any(ItemCardapioDTO.class));
    }

    @Test
    @DisplayName("Deve recuperar a lista de todos os itens de cardápio")
    void deveRecuperarListaDeTodosItensCardapio() {
        List<ItemCardapioDTO> listaItens = Arrays.asList(itemCardapioDTO, new ItemCardapioDTO(2L, "Suco", "Suco de Laranja", 10.00, true, "Suco natural", 1L));
        when(validarItemCardapioService.listarTodos()).thenReturn(listaItens);

        List<ItemCardapioDTO> result = gerenciarCardapio.recuperarLista();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(validarItemCardapioService, times(1)).listarTodos();
    }

    @Test
    @DisplayName("Deve recuperar a lista de itens de cardápio por ID de restaurante")
    void deveRecuperarListaItensCardapioPorRestauranteID() {
        List<ItemCardapioDTO> listaItens = Collections.singletonList(itemCardapioDTO);
        when(validarItemCardapioService.listarTodos(anyLong())).thenReturn(listaItens);

        List<ItemCardapioDTO> result = gerenciarCardapio.recuperarLista(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(validarItemCardapioService, times(1)).listarTodos(1L);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao recuperar lista com ID de restaurante nulo")
    void deveLancarExcecaoAoRecuperarListaComRestauranteIDNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarCardapio.recuperarLista(null);
        });

        assertEquals("É necessário passar o id do restaurante!", exception.getMessage());
        verify(validarItemCardapioService, never()).listarTodos(anyLong());
    }

    @Test
    @DisplayName("Deve deletar um item de cardápio com sucesso")
    void deveDeletarItemCardapioComSucesso() {
        when(validarItemCardapioService.recuperar(anyLong())).thenReturn(itemCardapioDTO);
        when(validarItemCardapioService.delete(anyLong())).thenReturn(true);

        gerenciarCardapio.deletar(1L);

        verify(validarItemCardapioService, times(1)).recuperar(1L);
        verify(validarItemCardapioService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar deletar item com ID nulo")
    void deveLancarExcecaoAoDeletarItemComIDNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarCardapio.deletar(null);
        });

        assertEquals("O id do item não pode ser nulo", exception.getMessage());
        verify(validarItemCardapioService, never()).recuperar(anyLong());
        verify(validarItemCardapioService, never()).delete(anyLong());
    }
}


