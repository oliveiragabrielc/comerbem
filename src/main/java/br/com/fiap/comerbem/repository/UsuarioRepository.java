package br.com.fiap.comerbem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fiap.comerbem.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email OR u.login = :login ")
    Usuario procurarPorParametros(@Param("email") String email, @Param("login") String login );

    @Query("SELECT u FROM Usuario u WHERE u.login = :login")
    Usuario procurarPorLogin(@Param("login") String login);
}
