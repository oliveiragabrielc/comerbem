package br.com.fiap.comerbem.service;

import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLogado;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLoginDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AutenticarServiceTest {

    @InjectMocks
    private AutenticarService autenticarService;

    private String testSecret;

    @BeforeEach
    void setUp() {
        // Usar um segredo de teste para evitar conflitos com o segredo real da aplicação
        testSecret = "SegredoDeTesteParaAutenticacaoJWT12345678901234567890";
        autenticarService.secret = testSecret;
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido para o usuário")
    void deveGerarTokenValido() {
        UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO("testuser", "password123");
        UsuarioLogado usuarioLogado = autenticarService.obterToken(usuarioLoginDTO);

        assertNotNull(usuarioLogado);
        assertEquals("testuser", usuarioLogado.usuario());
        assertNotNull(usuarioLogado.token());

        // Validar o token gerado
        assertTrue(autenticarService.validar(usuarioLogado));
    }

    @Test
    @DisplayName("Deve validar um token JWT válido")
    void deveValidarTokenValido() {
        String login = "validuser";
        String token = Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validade
                .signWith(SignatureAlgorithm.HS256, testSecret.getBytes())
                .compact();

        UsuarioLogado usuarioLogado = new UsuarioLogado(login, token);

        assertTrue(autenticarService.validar(usuarioLogado));
    }

    @Test
    @DisplayName("Deve lançar TokenInvalidoException para token expirado")
    void deveLancarExcecaoParaTokenExpirado() {
        String login = "expireduser";
        String expiredToken = Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // 1 hora atrás
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 30)) // 30 minutos atrás
                .signWith(SignatureAlgorithm.HS256, testSecret.getBytes())
                .compact();

        UsuarioLogado usuarioLogado = new UsuarioLogado(login, expiredToken);

        TokenInvalidoException exception = assertThrows(TokenInvalidoException.class, () -> {
            autenticarService.validar(usuarioLogado);
        });

        assertEquals("Token expirado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar TokenInvalidoException para assinatura de token inválida")
    void deveLancarExcecaoParaAssinaturaInvalida() {
        String login = "invaliduser";
        String wrongSecret = "SegredoErradoParaAssinaturaJWT12345678901234567890";
        String invalidSignatureToken = Jwts.builder()
                .setSubject(login)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, wrongSecret.getBytes())
                .compact();

        UsuarioLogado usuarioLogado = new UsuarioLogado(login, invalidSignatureToken);

        TokenInvalidoException exception = assertThrows(TokenInvalidoException.class, () -> {
            autenticarService.validar(usuarioLogado);
        });

        assertEquals("Assinatura do token inválida.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar TokenInvalidoException para token JWT malformado")
    void deveLancarExcecaoParaTokenMalformado() {
        UsuarioLogado usuarioLogado = new UsuarioLogado("malformeduser", "token.malformed.invalid");

        TokenInvalidoException exception = assertThrows(TokenInvalidoException.class, () -> {
            autenticarService.validar(usuarioLogado);
        });

        assertEquals("Token JWT inválido.", exception.getMessage());
    }
}


