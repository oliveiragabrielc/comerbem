package br.com.fiap.comerbem.casodeuso.gerenciar.restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioProprietario;
import br.com.fiap.comerbem.model.Endereco;
import br.com.fiap.comerbem.model.HorarioFuncionamento;
import br.com.fiap.comerbem.model.TipoCozinha;
import br.com.fiap.comerbem.model.enums.TipoUsuario;
import br.com.fiap.comerbem.service.ValidarRestauranteService;
import br.com.fiap.comerbem.service.ValidarUsuarioService;

@ExtendWith(MockitoExtension.class)
class GerenciarRestauranteTest {

    @Mock
    private ValidarUsuarioService validarUsuarioService;

    @Mock
    private ValidarRestauranteService validarRestauranteService;

    @InjectMocks
    private GerenciarRestaurante gerenciarRestaurante;

    private RestauranteDTO restauranteDTO;
    private UsuarioProprietario usuarioProprietario;

    @BeforeEach
    void setUp() {
        usuarioProprietario = new UsuarioProprietario(1L, "Gabriel", TipoUsuario.PROPRIETARIO);
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("Estado Teste");
        restauranteDTO = new RestauranteDTO(1L, "Restaurante Teste", endereco, TipoCozinha.PIZZA, null,
                usuarioProprietario);
    }

    @Test
    @DisplayName("Deve cadastrar um restaurante com sucesso")
    void deveCadastrarRestauranteComSucesso() {
        when(validarUsuarioService.existe(anyLong()))
                .thenReturn(new UsuarioDTO(usuarioProprietario.id(), usuarioProprietario.nome(), "email@test.com",
                        usuarioProprietario.nome(), usuarioProprietario.tipo(), null));
        when(validarRestauranteService.cadastrar(any(RestauranteDTO.class))).thenReturn(restauranteDTO);

        RestauranteDTO result = gerenciarRestaurante.cadastrar(restauranteDTO);

        assertNotNull(result);
        assertEquals(restauranteDTO.nome(), result.nome());
        verify(validarUsuarioService, times(1)).existe(restauranteDTO.usuario().id());
        verify(validarRestauranteService, times(1)).cadastrar(restauranteDTO);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao cadastrar restaurante com usuário não proprietário")
    void deveLancarExcecaoAoCadastrarRestauranteComUsuarioNaoProprietario() {
        UsuarioProprietario usuarioNaoProprietario = new UsuarioProprietario(2L, "Nome Cliente", TipoUsuario.CLIENTE);
        when(validarUsuarioService.existe(anyLong()))
                .thenReturn(new UsuarioDTO(usuarioNaoProprietario.id(), usuarioNaoProprietario.nome(), "email@test.com",
                        usuarioNaoProprietario.nome(), usuarioNaoProprietario.tipo(), null));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarRestaurante.cadastrar(restauranteDTO);
        });

        assertEquals("Para criação do restaurante o usuário necessita ser proprietário!", exception.getMessage());
        verify(validarUsuarioService, times(1)).existe(restauranteDTO.usuario().id());
        verify(validarRestauranteService, never()).cadastrar(any(RestauranteDTO.class));
    }

    @Test
    @DisplayName("Deve alterar um restaurante com sucesso")
    void deveAlterarRestauranteComSucesso() {
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Nova");
        enderecoAtualizado.setNumero("456");
        enderecoAtualizado.setCidade("Cidade Nova");
        enderecoAtualizado.setEstado("Estado Novo");

        LocalTime abertura = LocalTime.of(7, 0, 0);
        LocalTime fechamento = LocalTime.of(18, 0, 0);
        HorarioFuncionamento horarioFuncionamento = new HorarioFuncionamento(abertura, fechamento);

        RestauranteDTO restauranteAtualizado = new RestauranteDTO(1L, "Restaurante", enderecoAtualizado,
                TipoCozinha.CHURRASCARIA, horarioFuncionamento, usuarioProprietario);

        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);
        when(validarRestauranteService.alterar(any(RestauranteDTO.class))).thenReturn(restauranteAtualizado);

        RestauranteDTO result = gerenciarRestaurante.alterar(restauranteAtualizado);

        assertNotNull(result);
        assertEquals(restauranteAtualizado.nome(), result.nome());
        verify(validarRestauranteService, times(1)).recuperar(restauranteAtualizado.id());

    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar alterar restaurante com ID nulo")
    void deveLancarExcecaoAoAlterarRestauranteComIDNulo() {
        Endereco enderecoComIdNulo = new Endereco();
        enderecoComIdNulo.setRua("Rua Teste");
        enderecoComIdNulo.setNumero("123");
        enderecoComIdNulo.setCidade("Cidade Teste");
        enderecoComIdNulo.setEstado("Estado Teste");

        LocalTime abertura = LocalTime.of(7, 0, 0);
        LocalTime fechamento = LocalTime.of(18, 0, 0);
        HorarioFuncionamento horarioFuncionamento = new HorarioFuncionamento(abertura, fechamento);

        RestauranteDTO restauranteComIdNulo = new RestauranteDTO(null, "Restaurante", enderecoComIdNulo,
                TipoCozinha.CHURRASCARIA, horarioFuncionamento, usuarioProprietario);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarRestaurante.alterar(restauranteComIdNulo);
        });

        assertEquals("Restaurante a ser alterado não informado", exception.getMessage());
        verify(validarRestauranteService, never()).recuperar(anyLong());
        verify(validarRestauranteService, never()).alterar(any(RestauranteDTO.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalAccessError ao tentar alterar restaurante com usuário não proprietário")
    void deveLancarIllegalAccessErrorAoAlterarRestauranteComUsuarioNaoProprietario() {
        UsuarioProprietario usuarioNaoProprietario = new UsuarioProprietario(2L, "Nome Cliente", TipoUsuario.CLIENTE);
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Nova");
        enderecoAtualizado.setNumero("456");
        enderecoAtualizado.setCidade("Cidade Nova");
        enderecoAtualizado.setEstado("Estado Novo");

        LocalTime abertura = LocalTime.of(7, 0, 0);
        LocalTime fechamento = LocalTime.of(18, 0, 0);
        HorarioFuncionamento horarioFuncionamento = new HorarioFuncionamento(abertura, fechamento);

        RestauranteDTO restauranteAtualizado = new RestauranteDTO(1L, "Restaurante", enderecoAtualizado,
                TipoCozinha.CHURRASCARIA, horarioFuncionamento, usuarioNaoProprietario);
                
        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);

        IllegalAccessError exception = assertThrows(IllegalAccessError.class, () -> {
            gerenciarRestaurante.alterar(restauranteAtualizado);
        });

        assertEquals("O Usuário não é do tipo proprietário", exception.getMessage());
        verify(validarRestauranteService, times(1)).recuperar(restauranteAtualizado.id());
        verify(validarRestauranteService, never()).alterar(any(RestauranteDTO.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalAccessError ao tentar alterar restaurante com proprietário diferente")
    void deveLancarIllegalAccessErrorAoAlterarRestauranteComProprietarioDiferente() {
        UsuarioProprietario outroProprietario = new UsuarioProprietario(2L, "Outro Proprietario",
                TipoUsuario.PROPRIETARIO);
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Nova");
        enderecoAtualizado.setNumero("456");
        enderecoAtualizado.setCidade("Cidade Nova");
        enderecoAtualizado.setEstado("Estado Novo");

        LocalTime abertura = LocalTime.of(7, 0, 0);
        LocalTime fechamento = LocalTime.of(18, 0, 0);
        HorarioFuncionamento horarioFuncionamento = new HorarioFuncionamento(abertura, fechamento);

        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);
        
        RestauranteDTO restauranteAtualizado = new RestauranteDTO(1L, "Restaurante", enderecoAtualizado,
                TipoCozinha.CHURRASCARIA, horarioFuncionamento, outroProprietario);

        IllegalAccessError exception = assertThrows(IllegalAccessError.class, () -> {
            gerenciarRestaurante.alterar(restauranteAtualizado);
        });

        assertEquals("O Usuário não é proprietário desse restaurante", exception.getMessage());
        verify(validarRestauranteService, times(1)).recuperar(restauranteAtualizado.id());
        verify(validarRestauranteService, never()).alterar(any(RestauranteDTO.class));
    }

    @Test
    @DisplayName("Deve recuperar um restaurante com sucesso")
    void deveRecuperarRestauranteComSucesso() {
        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);

        RestauranteDTO result = gerenciarRestaurante.recuperar(1L);

        assertNotNull(result);
        assertEquals(restauranteDTO.nome(), result.nome());
        verify(validarRestauranteService, times(1)).recuperar(1L);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao recuperar restaurante com ID nulo")
    void deveLancarExcecaoAoRecuperarRestauranteComIDNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarRestaurante.recuperar(null);
        });

        assertEquals("O id não pode ser nulo", exception.getMessage());
        verify(validarRestauranteService, never()).recuperar(anyLong());
    }

    @Test
    @DisplayName("Deve deletar um restaurante com sucesso")
    void deveDeletarRestauranteComSucesso() {
        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);
        doNothing().when(validarRestauranteService).delete(anyLong());

        gerenciarRestaurante.deletar(1L, usuarioProprietario);

        verify(validarRestauranteService, times(1)).recuperar(1L);
        verify(validarRestauranteService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar deletar restaurante com ID nulo")
    void deveLancarExcecaoAoDeletarRestauranteComIDNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciarRestaurante.deletar(null, usuarioProprietario);
        });

        assertEquals("O id não pode ser nulo", exception.getMessage());
        verify(validarRestauranteService, never()).recuperar(anyLong());
        verify(validarRestauranteService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("Deve lançar IllegalAccessError ao tentar deletar restaurante com usuário não proprietário")
    void deveLancarIllegalAccessErrorAoDeletarRestauranteComUsuarioNaoProprietario() {
        UsuarioProprietario usuarioNaoProprietario = new UsuarioProprietario(2L, "Nome Cliente", TipoUsuario.CLIENTE);
        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);

        IllegalAccessError exception = assertThrows(IllegalAccessError.class, () -> {
            gerenciarRestaurante.deletar(1L, usuarioNaoProprietario);
        });

        assertEquals("O Usuário não é proprietário desse restaurante", exception.getMessage());
        verify(validarRestauranteService, times(1)).recuperar(anyLong());
        verify(validarRestauranteService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("Deve lançar IllegalAccessError ao tentar deletar restaurante com proprietário diferente")
    void deveLancarIllegalAccessErrorAoDeletarRestauranteComProprietarioDiferente() {
        UsuarioProprietario outroProprietario = new UsuarioProprietario(2L, "Outro Proprietario",
                TipoUsuario.PROPRIETARIO);
        when(validarRestauranteService.recuperar(anyLong())).thenReturn(restauranteDTO);

        IllegalAccessError exception = assertThrows(IllegalAccessError.class, () -> {
            gerenciarRestaurante.deletar(1L, outroProprietario);
        });

        assertEquals("O Usuário não é proprietário desse restaurante", exception.getMessage());
        verify(validarRestauranteService, times(1)).recuperar(anyLong());
        verify(validarRestauranteService, never()).delete(anyLong());
    }
}
