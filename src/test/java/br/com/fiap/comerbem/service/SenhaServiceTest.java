package br.com.fiap.comerbem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SenhaServiceTest {

    @InjectMocks
    private SenhaService senhaService;

    @BeforeEach
    void setUp() {
        // Não há mocks para configurar para esta classe simples
    }

    @Test
    @DisplayName("Deve validar a senha quando as senhas correspondem")
    void deveValidarSenhaQuandoSenhasCorrespondem() {
        String senhaRecebida = "minhasenha123";
        String senhaAtual = "minhasenha123";

        assertTrue(senhaService.validarSenha(senhaRecebida, senhaAtual));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando as senhas não correspondem")
    void deveLancarExcecaoQuandoSenhasNaoCorrespondem() {
        String senhaRecebida = "minhasenha123";
        String senhaAtual = "senhadiferente";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            senhaService.validarSenha(senhaRecebida, senhaAtual);
        });

        assertEquals("A senha não corresponde a esse usuário.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar a senha criptografada (neste caso, a própria senha)")
    void deveRetornarSenhaCriptografada() {
        String senha = "senhaParaCriptografar";
        String senhaCriptografada = senhaService.encryptarSenha(senha);

        assertEquals(senha, senhaCriptografada);
    }
}


