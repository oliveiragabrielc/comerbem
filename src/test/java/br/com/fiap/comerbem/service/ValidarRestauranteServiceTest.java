package br.com.fiap.comerbem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.comerbem.casodeuso.gerenciar.restaurante.RestauranteDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioProprietario;
import br.com.fiap.comerbem.model.Endereco;
import br.com.fiap.comerbem.model.HorarioFuncionamento;
import br.com.fiap.comerbem.model.Restaurante;
import br.com.fiap.comerbem.model.TipoCozinha;
import br.com.fiap.comerbem.model.Usuario;
import br.com.fiap.comerbem.model.enums.TipoUsuario;
import br.com.fiap.comerbem.repository.RestauranteRepository;

@ExtendWith(MockitoExtension.class)
class ValidarRestauranteServiceTest {

    @Mock
    private RestauranteRepository repository;

    @InjectMocks
    private ValidarRestauranteService service;

    private RestauranteDTO restauranteDTO;
    private Restaurante restaurante;
    private UsuarioProprietario proprietario;
    private Endereco endereco;
    private HorarioFuncionamento horario;
    private TipoCozinha tipoCozinha;

    @BeforeEach
    void setUp() {
        proprietario = new UsuarioProprietario(1L, "João Silva", TipoUsuario.PROPRIETARIO);

        endereco = new Endereco();
        endereco.setRua("Rua das Flores, 123");
        endereco.setNumero("123");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");

        horario = new HorarioFuncionamento();

        tipoCozinha = TipoCozinha.PIZZA;

        restauranteDTO = new RestauranteDTO(
                1L,
                "Restaurante Bella Vista",
                endereco,
                tipoCozinha,
                horario,
                proprietario);

        Usuario usuarioModel = new Usuario();
        usuarioModel.setId(1L);
        usuarioModel.setNome("João Silva");
        usuarioModel.setTipo(TipoUsuario.PROPRIETARIO);

        restaurante = new Restaurante(
                "Restaurante Bella Vista",
                endereco,
                tipoCozinha,
                horario,
                usuarioModel);
        restaurante.setId(1L);
    }

    @Test
    void deveCadastrarRestauranteComSucesso() {
        when(repository.procurarPorNome(restauranteDTO.nome())).thenReturn(null);
        when(repository.save(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteDTO resultado = service.cadastrar(restauranteDTO);

        assertNotNull(resultado);
        assertEquals(restauranteDTO.nome(), resultado.nome());
        verify(repository).procurarPorNome(restauranteDTO.nome());
        verify(repository).save(any(Restaurante.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeJaExistir() {
        when(repository.procurarPorNome(restauranteDTO.nome())).thenReturn(restaurante);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.cadastrar(restauranteDTO));

        assertEquals("O nome do restaurante já está sendo utilizado!", exception.getMessage());
        verify(repository).procurarPorNome(restauranteDTO.nome());
        verify(repository, never()).save(any(Restaurante.class));
    }

    @Test
    void deveAlterarRestauranteComSucesso() {
        when(repository.findById(restauranteDTO.id())).thenReturn(Optional.of(restaurante));
        when(repository.save(any(Restaurante.class))).thenReturn(restaurante);

        RestauranteDTO resultado = service.alterar(restauranteDTO);

        assertNotNull(resultado);
        verify(repository).findById(restauranteDTO.id());
        verify(repository).save(any(Restaurante.class));
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoForEncontradoNoAlterar() {
        when(repository.findById(restauranteDTO.id())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> service.alterar(restauranteDTO));

        assertEquals("Restaurante não Encontrado", exception.getMessage());
        verify(repository).findById(restauranteDTO.id());
        verify(repository, never()).save(any(Restaurante.class));
    }

    @Test
    void deveRecuperarRestaurantePorId() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(restaurante));

        RestauranteDTO resultado = service.recuperar(id);

        assertNotNull(resultado);
        assertEquals(restaurante.getNome(), resultado.nome());
        verify(repository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoIdForNuloNoRecuperar() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.recuperar(null));

        assertEquals("O id não pode ser nulo!", exception.getMessage());
    }

    @Test
    void deveDeletarRestauranteComSucesso() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(restaurante));

        assertDoesNotThrow(() -> service.delete(id));

        verify(repository).delete(restaurante);
    }

    @Test
    void deveLancarExcecaoQuandoRestauranteNaoForEncontradoNoDelete() {
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> service.delete(id));

        assertEquals("Restaurante não Encontrado", exception.getMessage());
        verify(repository, never()).delete(any(Restaurante.class));
    }

    @Test
    void deveLancarExcecaoQuandoIdForNuloNoDelete() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.delete(null));

        assertEquals("O id não pode ser nulo!", exception.getMessage());
    }
}
