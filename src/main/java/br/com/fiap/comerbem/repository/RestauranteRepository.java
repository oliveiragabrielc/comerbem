package br.com.fiap.comerbem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.comerbem.model.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {


    @Query("SELECT r FROM Restaurante r WHERE r.nome = :nome")
    Restaurante procurarPorNome(@Param("nome") String nome);

}
