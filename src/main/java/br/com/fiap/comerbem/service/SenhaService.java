package br.com.fiap.comerbem.service;

import org.springframework.stereotype.Service;

@Service
public class SenhaService {

    public boolean validarSenha(String senhaRecebida, String senhaAtual) {

        if (!compararSenha(senhaRecebida, senhaAtual)) {
            throw new IllegalArgumentException("A senha não corresponde a esse usuário.");
        }

        return true;
    }

    private boolean compararSenha(String senhaPassada, String senhaRegistrada) {
        return senhaPassada.equals(senhaRegistrada);
        // return passwordEncoder.matches(senhaPassada, senhaRegistrada);
    }

    public String encryptarSenha(String senha) {
        return senha;
        // return passwordEncoder.encode(senha);
    }

    

}
