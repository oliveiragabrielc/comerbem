package br.com.fiap.comerbem.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLogado;
import br.com.fiap.comerbem.casodeuso.login.dto.UsuarioLoginDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class AutenticarService {

    public String secret = "SegredoDaAplicacaodsadsadsadasdsadwqeqwgfrgwerdsadsavdstwe";

    public UsuarioLogado obterToken(UsuarioLoginDTO usuario) {
        String token = Jwts.builder()
                .setSubject(usuario.login())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();

        return new UsuarioLogado(usuario.login(), token);
    }

    public boolean validar(UsuarioLogado usuarioLogado) {
        try {
            Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(usuarioLogado.token())
                    .getBody();
            return true;

        } catch (ExpiredJwtException e) {
            throw new TokenInvalidoException("Token expirado.");
        } catch (SignatureException e) {
            throw new TokenInvalidoException("Assinatura do token inválida.");
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new TokenInvalidoException("Token JWT inválido.");
        }
    }
}

class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String mensagem) {
        super(mensagem);
    }
}