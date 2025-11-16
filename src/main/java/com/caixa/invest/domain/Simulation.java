package com.caixa.invest.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulations")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Simulation extends PanacheEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "valor_investido", nullable = false)
    private BigDecimal valorInvestido;

    @Column(name = "valor_final", nullable = false)
    private BigDecimal valorFinal;

    @Column(name = "rentabilidade_efetiva", nullable = false)
    private BigDecimal rentabilidadeEfetiva;

    @Column(name = "prazo_meses", nullable = false)
    private Integer prazoMeses;

    @Column(name = "data_simulacao", nullable = false, updatable = false)
    private LocalDateTime dataSimulacao;

    @Column(name = "imposto_renda")
    private BigDecimal impostoRenda;

    @Column(name = "valor_liquido")
    private BigDecimal valorLiquido;

    @PrePersist
    public void prePersist() {
        if (this.dataSimulacao == null) {
            this.dataSimulacao = LocalDateTime.now();
        }
    }
}
