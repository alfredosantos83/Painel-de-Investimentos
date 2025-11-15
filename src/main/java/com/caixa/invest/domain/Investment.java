package com.caixa.invest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "investments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Product.TipoProduto tipo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private BigDecimal rentabilidade;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "prazo_meses")
    private Integer prazoMeses;

    @Enumerated(EnumType.STRING)
    private StatusInvestimento status;

    @Column(name = "data_resgate")
    private LocalDate dataResgate;

    public enum StatusInvestimento {
        ATIVO,
        RESGATADO,
        VENCIDO
    }
}
