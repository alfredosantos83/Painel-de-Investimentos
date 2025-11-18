package com.caixa.invest.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Product extends PanacheEntity {

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProduto tipo;

    @Column(nullable = false)
    private BigDecimal rentabilidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelRisco risco;

    @Column(name = "prazo_minimo_meses")
    private Integer prazoMinimoMeses;

    @Column(name = "prazo_maximo_meses")
    private Integer prazoMaximoMeses;

    @Column(name = "valor_minimo")
    private BigDecimal valorMinimo;

    @Column(name = "valor_maximo")
    private BigDecimal valorMaximo;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "liquidez_dias")
    private Integer liquidezDias;

    private String descricao;

    public enum TipoProduto {
        CDB,
        LCI,
        LCA,
        TESOURO_DIRETO,
        FUNDO,
        FUNDO_MULTIMERCADO,
        FUNDO_RENDA_FIXA,
        FUNDO_ACOES,
        POUPANCA
    }

    public enum NivelRisco {
        BAIXO,
        MEDIO,
        ALTO
    }

    @PrePersist
    public void prePersist() {
        if (this.ativo == null) {
            this.ativo = true;
        }
    }
}

