package com.caixa.invest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String email;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "volume_total_investido")
    private BigDecimal volumeTotalInvestido;

    @Column(name = "frequencia_movimentacoes")
    private Integer frequenciaMovimentacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferencia_investimento")
    private PreferenciaInvestimento preferenciaInvestimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_risco")
    private PerfilRisco perfilRisco;

    @Column(name = "pontuacao_risco")
    private Integer pontuacaoRisco;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Investment> investments = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Simulation> simulations = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        if (this.volumeTotalInvestido == null) {
            this.volumeTotalInvestido = BigDecimal.ZERO;
        }
        if (this.frequenciaMovimentacoes == null) {
            this.frequenciaMovimentacoes = 0;
        }
    }

    public enum PreferenciaInvestimento {
        LIQUIDEZ,
        RENTABILIDADE,
        EQUILIBRADO
    }

    public enum PerfilRisco {
        CONSERVADOR,
        MODERADO,
        AGRESSIVO
    }
}
