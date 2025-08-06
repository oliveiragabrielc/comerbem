package br.com.fiap.comerbem.casodeuso.gerenciar.usuario;

import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioDTO;
import br.com.fiap.comerbem.casodeuso.gerenciar.usuario.dto.UsuarioNovoDTO;
import br.com.fiap.comerbem.model.Endereco;
import br.com.fiap.comerbem.model.enums.TipoUsuario;
import br.com.fiap.comerbem.service.ValidarUsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GerenciarUsuarioTest {

    @Mock
    private ValidarUsuarioService validarUsuarioService;

    @InjectMocks
    private GerenciarUsuario gerenciarUsuario;

    private UsuarioNovoDTO usuarioNovoDTO;
    private UsuarioDTO usuarioDTO;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("Estado Teste");
        usuarioNovoDTO = new UsuarioNovoDTO("Nome Teste", "teste@email.com", "loginTeste", "senha123", TipoUsuario.CLIENTE, endereco);
        usuarioDTO = new UsuarioDTO(1L, "Nome Teste", "teste@email.com", "loginTeste", TipoUsuario.CLIENTE, endereco);
    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        when(validarUsuarioService.existePorLoginEEmail(anyString(), anyString())).thenReturn(null);
        when(validarUsuarioService.salvar(any(UsuarioNovoDTO.class))).thenReturn(usuarioDTO);

        UsuarioDTO result = gerenciarUsuario.cadastrar(usuarioNovoDTO);

        assertNotNull(result);
        assertEquals(usuarioDTO.email(), result.email());
        verify(validarUsuarioService, times(1)).existePorLoginEEmail(usuarioNovoDTO.login(), usuarioNovoDTO.email());
        verify(validarUsuarioService, times(1)).salvar(usuarioNovoDTO);
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao cadastrar usuário com email já em uso")
    void deveLancarExcecaoAoCadastrarUsuarioComEmailJaEmUso() {
        when(validarUsuarioService.existePorLoginEEmail(anyString(), anyString())).thenReturn(usuarioDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            gerenciarUsuario.cadastrar(usuarioNovoDTO);
        });

        assertEquals("Esse email já está em uso!", exception.getMessage());
        verify(validarUsuarioService, times(1)).existePorLoginEEmail(usuarioNovoDTO.login(), usuarioNovoDTO.email());
        verify(validarUsuarioService, never()).salvar(any(UsuarioNovoDTO.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao cadastrar usuário com login já em uso")
    void deveLancarExcecaoAoCadastrarUsuarioComLoginJaEmUso() {
        UsuarioDTO usuarioComLoginExistente = new UsuarioDTO(2L, "Outro Nome", "outro@email.com", "loginTeste", TipoUsuario.CLIENTE, endereco);
        when(validarUsuarioService.existePorLoginEEmail(anyString(), anyString())).thenReturn(usuarioComLoginExistente);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            gerenciarUsuario.cadastrar(usuarioNovoDTO);
        });

        assertEquals("Login já está em uso", exception.getMessage());
        verify(validarUsuarioService, times(1)).existePorLoginEEmail(usuarioNovoDTO.login(), usuarioNovoDTO.email());
        verify(validarUsuarioService, never()).salvar(any(UsuarioNovoDTO.class));
    }

    @Test
    @DisplayName("Deve recuperar um usuário com sucesso")
    void deveRecuperarUsuarioComSucesso() {
        when(validarUsuarioService.existe(anyLong())).thenReturn(usuarioDTO);

        UsuarioDTO result = gerenciarUsuario.recuperar(1L);

        assertNotNull(result);
        assertEquals(usuarioDTO.id(), result.id());
        verify(validarUsuarioService, times(1)).existe(1L);
    }

    @Test
    @DisplayName("Deve alterar um usuário com sucesso")
    void deveAlterarUsuarioComSucesso() {
        UsuarioDTO usuarioAtualizado = new UsuarioDTO(1L, "Nome Atualizado", "atualizado@email.com", "loginTeste", TipoUsuario.CLIENTE, endereco);
        when(validarUsuarioService.existe(anyLong())).thenReturn(usuarioDTO);
        when(validarUsuarioService.alterar(any(UsuarioDTO.class))).thenReturn(usuarioAtualizado);

        UsuarioDTO result = gerenciarUsuario.alterar(usuarioAtualizado);

        assertNotNull(result);
        assertEquals(usuarioAtualizado.nome(), result.nome());
        verify(validarUsuarioService, times(1)).existe(usuarioAtualizado.id());
        verify(validarUsuarioService, times(1)).alterar(usuarioAtualizado);
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar alterar usuário com ID diferente")
    void deveLancarExcecaoAoAlterarUsuarioComIDDiferente() {
        UsuarioDTO usuarioComIDDiferente = new UsuarioDTO(2L, "Nome Teste", "teste@email.com", "loginTeste", TipoUsuario.CLIENTE, endereco);
        when(validarUsuarioService.existe(anyLong())).thenReturn(usuarioDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            gerenciarUsuario.alterar(usuarioComIDDiferente);
        });

        assertEquals("Não é possível alterar o ID", exception.getMessage());
        verify(validarUsuarioService, times(1)).existe(usuarioComIDDiferente.id());
        verify(validarUsuarioService, never()).alterar(any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar alterar usuário com login diferente")
    void deveLancarExcecaoAoAlterarUsuarioComLoginDiferente() {
        UsuarioDTO usuarioComLoginDiferente = new UsuarioDTO(1L, "Nome Teste", "teste@email.com", "novoLogin", TipoUsuario.CLIENTE, endereco);
        when(validarUsuarioService.existe(anyLong())).thenReturn(usuarioDTO);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            gerenciarUsuario.alterar(usuarioComLoginDiferente);
        });

        assertEquals("Não é possível alterar o Login", exception.getMessage());
        verify(validarUsuarioService, times(1)).existe(usuarioComLoginDiferente.id());
        verify(validarUsuarioService, never()).alterar(any(UsuarioDTO.class));
    }

    @Test
    @DisplayName("Deve deletar um usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {
        when(validarUsuarioService.existe(anyLong())).thenReturn(usuarioDTO);
        doNothing().when(validarUsuarioService).deletar(anyLong());

        gerenciarUsuario.deletar(usuarioDTO);

        verify(validarUsuarioService, times(1)).existe(usuarioDTO.id());
        verify(validarUsuarioService, times(1)).deletar(usuarioDTO.id());
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar deletar usuário inexistente")
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(validarUsuarioService.existe(anyLong())).thenReturn(null);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            gerenciarUsuario.deletar(usuarioDTO);
        });

        assertEquals("Não é possível excluir um usuário que não existe!", exception.getMessage());
        verify(validarUsuarioService, times(1)).existe(usuarioDTO.id());
        verify(validarUsuarioService, never()).deletar(anyLong());
    }
}

