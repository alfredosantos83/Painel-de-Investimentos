package com.caixa.invest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PainelInvestimentosApplication {

    public static void main(String[] args) {
        SpringApplication.run(PainelInvestimentosApplication.class, args);
    }
}
