package com.caixa.invest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulations")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Simulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @CreatedDate
    @Column(name = "data_simulacao", nullable = false, updatable = false)
    private LocalDateTime dataSimulacao;

    @Column(name = "imposto_renda")
    private BigDecimal impostoRenda;

    @Column(name = "valor_liquido")
    private BigDecimal valorLiquido;
}
