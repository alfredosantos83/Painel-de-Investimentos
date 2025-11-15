package com.caixa.invest.repository;

import com.caixa.invest.domain.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {
    
    List<Telemetry> findByServiceName(String serviceName);
    
    @Query("SELECT t.serviceName as nome, " +
           "COUNT(t) as quantidadeChamadas, " +
           "AVG(t.responseTimeMs) as mediaTempoRespostaMs " +
           "FROM Telemetry t " +
           "WHERE t.timestamp BETWEEN :inicio AND :fim " +
           "GROUP BY t.serviceName")
    List<Object[]> findTelemetriaByPeriodo(
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim
    );
    
    @Query("SELECT AVG(t.responseTimeMs) FROM Telemetry t WHERE t.serviceName = :serviceName " +
           "AND t.timestamp >= :since")
    Long findAvgResponseTimeByServiceSince(
        @Param("serviceName") String serviceName,
        @Param("since") LocalDateTime since
    );
}
