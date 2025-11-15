package com.caixa.invest.repository;

import com.caixa.invest.domain.Investment;
import com.caixa.invest.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    
    List<Investment> findByClientId(Long clientId);
    
    List<Investment> findByClientIdOrderByDataDesc(Long clientId);
    
    List<Investment> findByClientIdAndStatus(Long clientId, Investment.StatusInvestimento status);
    
    @Query("SELECT COUNT(i) FROM Investment i WHERE i.client.id = :clientId " +
           "AND i.data >= :dataInicio")
    Integer countMovimentacoesByClientSince(
        @Param("clientId") Long clientId,
        @Param("dataInicio") LocalDate dataInicio
    );
    
    @Query("SELECT SUM(i.valor) FROM Investment i WHERE i.client.id = :clientId " +
           "AND i.status = 'ATIVO'")
    BigDecimal sumValorAtivoByClient(@Param("clientId") Long clientId);
    
    @Query("SELECT i.tipo, COUNT(i) FROM Investment i WHERE i.client.id = :clientId " +
           "GROUP BY i.tipo ORDER BY COUNT(i) DESC")
    List<Object[]> findPreferenciasByClient(@Param("clientId") Long clientId);
}
