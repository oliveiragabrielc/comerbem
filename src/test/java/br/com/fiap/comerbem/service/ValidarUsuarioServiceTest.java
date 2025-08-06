package br.com.fiap.comerbem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioNovoDTO;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLogado;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLoginDTO;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioMudarSenha;
import br.com.fiap.comerbem.model.Endereco;
import br.com.fiap.comerbem.model.Usuario;
import br.com.fiap.comerbem.model.enums.TipoUsuario;
import br.com.fiap.comerbem.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class ValidarUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SenhaService senhaService;

    @InjectMocks
    private ValidarUsuarioService validarUsuarioService;

    private Usuario usuario;
    private UsuarioNovoDTO usuarioNovoDTO;
    private UsuarioDTO usuarioDTO;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setRua("SQN 306");
        endereco.setNumero("110");
        endereco.setCidade("Brasilia");
        endereco.setEstado("DF");
        usuario = new Usuario(1L, "Fulano", "fulano@gmail.com", "fulano110", endereco, TipoUsuario.ADMINISTRADOR,
                LocalDate.now());
        usuario.setSenha("senhateste123");
        usuarioNovoDTO = new UsuarioNovoDTO("Beltrano", "beltrano@gmail.com", "bel110", "novaSenha",
                TipoUsuario.CLIENTE, endereco);
        usuarioDTO = new UsuarioDTO(1L, "Beltrano", "beltrano@gmail.com", "bel110", TipoUsuario.CLIENTE, endereco);
    }

    @Test
    @DisplayName("Deve salvar um novo usuário com sucesso")
    void deveSalvarNovoUsuarioComSucesso() {
        when(senhaService.encryptarSenha(anyString())).thenReturn("senhaHash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO result = validarUsuarioService.salvar(usuarioNovoDTO);

        assertNotNull(result);
        assertEquals(usuario.getNome(), result.nome());
        verify(senhaService, times(1)).encryptarSenha(usuarioNovoDTO.senha());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve alterar um usuário existente com sucesso")
    void deveAlterarUsuarioExistenteComSucesso() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO result = validarUsuarioService.alterar(usuarioDTO);

        assertNotNull(result);
        assertEquals(usuario.getNome(), result.nome());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(any(Usuario.class));

        validarUsuarioService.deletar(usuarioDTO.id());

        verify(usuarioRepository, times(1)).delete(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve retornar UsuarioDTO se o usuário existir por login e email")
    void deveRetornarUsuarioDTOSucesso() {
        when(usuarioRepository.procurarPorParametros(anyString(), anyString())).thenReturn(usuario);

        UsuarioDTO result = validarUsuarioService.existePorLoginEEmail("loginTeste", "email@gmail.com");

        assertNotNull(result);
        assertEquals(usuario.getLogin(), result.login());
        verify(usuarioRepository, times(1)).procurarPorParametros("email@gmail.com", "loginTeste");
    }

    @Test
    @DisplayName("Deve retornar null se o usuário não existir por login e email")
    void deveRetornarNullSeUsuarioNaoExistir() {
        when(usuarioRepository.procurarPorParametros(anyString(), anyString())).thenReturn(null);

        UsuarioDTO result = validarUsuarioService.existePorLoginEEmail("loginInexistente",
                "emailInexistente@gmail.com");

        assertNull(result);
        verify(usuarioRepository, times(1)).procurarPorParametros("emailInexistente@gmail.com", "loginInexistente");
    }

    @Test
    @DisplayName("Deve retornar UsuarioDTO se o usuário existir por ID")
    void deveRetornarUsuarioDTOPorIDSucesso() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));

        UsuarioDTO result = validarUsuarioService.existe(1L);

        assertNotNull(result);
        assertEquals(usuario.getId(), result.id());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar NoSuchElementException se o usuário não existir por ID")
    void deveLancarExcecaoSeUsuarioNaoExistirPorID() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            validarUsuarioService.existe(99L);
        });

        assertEquals("Usuário com ID 99 não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se o ID do usuário for nulo")
    void deveLancarExcecaoSeIDUsuarioForNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validarUsuarioService.existe(null);
        });

        assertEquals("ID do usuário não pode ser nulo.", exception.getMessage());
        verify(usuarioRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Deve autenticar o usuário com sucesso")
    void deveAutenticarUsuarioComSucesso() {
        UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO("loginTeste", "senhaHash");
        when(usuarioRepository.procurarPorLogin(anyString())).thenReturn(usuario);
        when(senhaService.validarSenha(eq("senhaHash"), eq(usuario.getSenha()))).thenReturn(true);

        UsuarioLogado result = validarUsuarioService.autenticar(usuarioLoginDTO);

        assertNotNull(result);
        assertEquals(usuario.getLogin(), result.usuario());
        assertNull(result.token());
        verify(usuarioRepository, times(1)).procurarPorLogin("loginTeste");
        verify(senhaService, times(1)).validarSenha("senhaHash", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se o usuário não for encontrado na autenticação")
    void deveLancarExcecaoSeUsuarioNaoEncontradoNaAutenticacao() {
        UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO("loginInexistente", "senhaQualquer");
        when(usuarioRepository.procurarPorLogin(anyString())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validarUsuarioService.autenticar(usuarioLoginDTO);
        });

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).procurarPorLogin("loginInexistente");
        verify(senhaService, never()).validarSenha(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se a senha for inválida na autenticação")
    void deveLancarExcecaoSeSenhaInvalidaNaAutenticacao() {
        UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO("loginTeste", "senhaInvalida");
        when(usuarioRepository.procurarPorLogin(anyString())).thenReturn(usuario);
        doThrow(new IllegalArgumentException("A senha não corresponde a esse usuário."))
                .when(senhaService).validarSenha(eq("senhaInvalida"), eq(usuario.getSenha()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validarUsuarioService.autenticar(usuarioLoginDTO);
        });

        assertEquals("A senha não corresponde a esse usuário.", exception.getMessage());
        verify(usuarioRepository, times(1)).procurarPorLogin("loginTeste");
        verify(senhaService, times(1)).validarSenha("senhaInvalida", usuario.getSenha());
    }

    @Test
    @DisplayName("Deve alterar a senha do usuário com sucesso")
    void deveAlterarSenhaUsuarioComSucesso() {
        UsuarioMudarSenha usuarioMudarSenha = new UsuarioMudarSenha("loginTeste", "email@gmail.com", "novaSenhaHash");
        when(usuarioRepository.procurarPorParametros(anyString(), anyString())).thenReturn(usuario);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO result = validarUsuarioService.alterarSenha(usuarioMudarSenha);

        assertNotNull(result);
        assertEquals("novaSenhaHash", usuario.getSenha());
        verify(usuarioRepository, times(1)).procurarPorParametros("email@gmail.com", "loginTeste");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar alterar senha de usuário não encontrado")
    void deveLancarExcecaoAoAlterarSenhaUsuarioNaoEncontrado() {
        UsuarioMudarSenha usuarioMudarSenha = new UsuarioMudarSenha("loginInexistente", "emailInexistente@gmail.com",
                "novaSenha");
        when(usuarioRepository.procurarPorParametros(anyString(), anyString())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            validarUsuarioService.alterarSenha(usuarioMudarSenha);
        });

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).procurarPorParametros("emailInexistente@gmail.com", "loginInexistente");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
