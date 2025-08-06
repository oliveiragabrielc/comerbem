package br.com.fiap.comerbem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.comerbem.casodeuso.gerenciar.cardapio.ItemCardapioDTO;
import br.com.fiap.comerbem.model.ItemCardapio;
import br.com.fiap.comerbem.model.Restaurante;
import br.com.fiap.comerbem.repository.ItemCardapioRepository;

@ExtendWith(MockitoExtension.class)
class ValidarItemCardapioServiceTest {

    @Mock
    private ItemCardapioRepository repository;

    @Mock
    private ValidarRestauranteService validarRestauranteService;

    @InjectMocks
    private ValidarItemCardapioService service;

    private ItemCardapioDTO itemValido;
    private ItemCardapio itemCardapio;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        
        itemValido = new ItemCardapioDTO(
            1L,
            "Pizza Margherita",
            "Pizza tradicional com molho de tomate, mussarela e manjericão",
            25.90,
            false,
            "/images/pizza-margherita.jpg",
            1L
        );

        itemCardapio = new ItemCardapio(
            1L,
            "Pizza Margherita",
            "Pizza tradicional com molho de tomate, mussarela e manjericão",
            BigDecimal.valueOf(25.90),
            false,
            "/images/pizza-margherita.jpg",
            restaurante
        );
    }

    @Test
    void deveCadastrarItemComSucesso() {
        when(repository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        ItemCardapioDTO resultado = service.cadastrar(itemValido);

        assertNotNull(resultado);
        assertEquals(itemValido.nome(), resultado.nome());
        assertEquals(itemValido.preco(), resultado.preco());
        verify(repository).save(any(ItemCardapio.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        ItemCardapioDTO itemInvalido = new ItemCardapioDTO(
            null, "", "Descrição", 25.90, false, "/image.jpg", 1L
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.cadastrar(itemInvalido)
        );

        assertEquals("O nome do item não pode ser vazio.", exception.getMessage());
        verify(repository, never()).save(any(ItemCardapio.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNull() {
        ItemCardapioDTO itemInvalido = new ItemCardapioDTO(
            null, null, "Descrição", 25.90, false, "/image.jpg", 1L
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.cadastrar(itemInvalido)
        );

        assertEquals("O nome do item não pode ser vazio.", exception.getMessage());
        verify(repository, never()).save(any(ItemCardapio.class));
    }

    @Test
    void deveLancarExcecaoQuandoPrecoForNegativo() {
        ItemCardapioDTO itemInvalido = new ItemCardapioDTO(
            null, "Pizza", "Descrição", -10.0, false, "/image.jpg", 1L
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.cadastrar(itemInvalido)
        );

        assertEquals("O preço do item não pode ser negativo.", exception.getMessage());
        verify(repository, never()).save(any(ItemCardapio.class));
    }

    @Test
    void deveLancarExcecaoQuandoPrecoForNull() {
        ItemCardapioDTO itemInvalido = new ItemCardapioDTO(
            null, "Pizza", "Descrição", null, false, "/image.jpg", 1L
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.cadastrar(itemInvalido)
        );

        assertEquals("O preço do item não pode ser negativo.", exception.getMessage());
        verify(repository, never()).save(any(ItemCardapio.class));
    }

    @Test
    void deveListarTodosOsItens() {
        List<ItemCardapio> itens = Arrays.asList(itemCardapio);
        when(repository.findAll()).thenReturn(itens);

        List<ItemCardapioDTO> resultado = service.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(itemCardapio.getNome(), resultado.get(0).nome());
        verify(repository).findAll();
    }

    @Test
    void deveListarItensPorRestaurante() {
        Long restauranteId = 1L;
        List<ItemCardapio> itens = Arrays.asList(itemCardapio);
        when(repository.listarItemRestaurante(restauranteId)).thenReturn(itens);

        List<ItemCardapioDTO> resultado = service.listarTodos(restauranteId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(itemCardapio.getNome(), resultado.get(0).nome());
        verify(repository).listarItemRestaurante(restauranteId);
    }

    @Test
    void deveRecuperarItemPorId() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(itemCardapio));

        ItemCardapioDTO resultado = service.recuperar(id);

        assertNotNull(resultado);
        assertEquals(itemCardapio.getNome(), resultado.nome());
        assertEquals(itemCardapio.getPreco().doubleValue(), resultado.preco());
        verify(repository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoIdForNuloNoRecuperar() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.recuperar(null)
        );

        assertEquals("O id não pode ser nulo.", exception.getMessage());
        verify(repository, never()).findById(any());
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoForEncontradoNoRecuperar() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> service.recuperar(id)
        );

        assertEquals("Item não Encontrado", exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    void deveAlterarItemComSucesso() {
        Long id = 1L;
        ItemCardapioDTO itemAtualizado = new ItemCardapioDTO(
            1L, "Pizza Pepperoni", "Nova descrição", 30.0, true, "/nova-imagem.jpg", 1L
        );
        
        when(repository.findById(id)).thenReturn(Optional.of(itemCardapio));
        when(repository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        ItemCardapioDTO resultado = service.alterar(id, itemAtualizado);

        assertNotNull(resultado);
        verify(repository).findById(id);
        verify(repository).save(any(ItemCardapio.class));
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoForEncontradoNoAlterar() {
        Long id = 999L;
        ItemCardapioDTO itemAtualizado = new ItemCardapioDTO(
            1L, "Pizza", "Descrição", 25.0, false, "/image.jpg", 1L
        );
        
        when(repository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
            NoSuchElementException.class,
            () -> service.alterar(id, itemAtualizado)
        );

        assertEquals("Item não Encontrado", exception.getMessage());
        verify(repository).findById(id);
        verify(repository, never()).save(any(ItemCardapio.class));
    }

    @Test
    void deveDeletarItemComSucesso() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        boolean resultado = service.delete(id);

        assertTrue(resultado);
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deveRetornarFalseQuandoItemNaoExistirNoDelete() {
        Long id = 999L;
        when(repository.existsById(id)).thenReturn(false);

        boolean resultado = service.delete(id);

        assertFalse(resultado);
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(id);
    }

    @Test
    void deveLancarExcecaoQuandoIdForNuloNoDelete() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.delete(null)
        );

        assertEquals("O id não pode ser nulo.", exception.getMessage());
        verify(repository, never()).existsById(any());
        verify(repository, never()).deleteById(any());
    }
}

