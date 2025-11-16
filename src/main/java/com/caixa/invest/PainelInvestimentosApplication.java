package com.caixa.invest;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class PainelInvestimentosApplication implements QuarkusApplication {

    public static void main(String... args) {
        Quarkus.run(PainelInvestimentosApplication.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        Quarkus.waitForExit();
        return 0;
    }
}
