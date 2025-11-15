package com.caixa.invest.repository;

import com.caixa.invest.domain.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    
    List<Simulation> findByClientId(Long clientId);
    
    List<Simulation> findByProductId(Long productId);
    
    @Query("SELECT s FROM Simulation s WHERE s.client.id = :clientId " +
           "ORDER BY s.dataSimulacao DESC")
    List<Simulation> findByClientIdOrderByDataSimulacaoDesc(@Param("clientId") Long clientId);
    
    @Query("SELECT s.product.nome as produto, " +
           "CAST(s.dataSimulacao as LocalDate) as data, " +
           "COUNT(s) as quantidadeSimulacoes, " +
           "AVG(s.valorFinal) as mediaValorFinal " +
           "FROM Simulation s " +
           "WHERE CAST(s.dataSimulacao as LocalDate) BETWEEN :dataInicio AND :dataFim " +
           "GROUP BY s.product.nome, CAST(s.dataSimulacao as LocalDate)")
    List<Object[]> findSimulacoesPorProdutoEDia(
        @Param("dataInicio") LocalDate dataInicio,
        @Param("dataFim") LocalDate dataFim
    );
    
    @Query("SELECT COUNT(s) FROM Simulation s WHERE s.dataSimulacao >= :inicio")
    Long countSimulacoesSince(@Param("inicio") LocalDateTime inicio);
}
