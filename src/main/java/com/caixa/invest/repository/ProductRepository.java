package com.caixa.invest.repository;

import com.caixa.invest.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByTipo(Product.TipoProduto tipo);
    
    List<Product> findByRisco(Product.NivelRisco risco);
    
    List<Product> findByAtivoTrue();
    
    @Query("SELECT p FROM Product p WHERE p.tipo = :tipo AND p.ativo = true " +
           "AND :prazo BETWEEN p.prazoMinimoMeses AND p.prazoMaximoMeses " +
           "AND :valor BETWEEN p.valorMinimo AND p.valorMaximo")
    Optional<Product> findByTipoAndValidParams(
        @Param("tipo") Product.TipoProduto tipo,
        @Param("prazo") Integer prazo,
        @Param("valor") BigDecimal valor
    );
    
    @Query("SELECT p FROM Product p WHERE p.risco IN :riscos AND p.ativo = true")
    List<Product> findByRiscoIn(@Param("riscos") List<Product.NivelRisco> riscos);
}
