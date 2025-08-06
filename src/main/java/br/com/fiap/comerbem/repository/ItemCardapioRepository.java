package br.com.fiap.comerbem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.comerbem.model.ItemCardapio;

public interface ItemCardapioRepository  extends JpaRepository<ItemCardapio, Long>{
    
    @Query("SELECT i FROM  ItemCardapio i WHERE i.restaurante.id = :id")
    List<ItemCardapio> listarItemRestaurante(@Param("id") Long id);
}
