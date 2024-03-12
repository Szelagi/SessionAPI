package pl.szelagi.component.constructor;

import java.util.ArrayList;

public class PlayerRecoveryLambdas<T> {
    private final ArrayList<T> lambdas = new ArrayList<>();

    public PlayerRecoveryLambdas<T> add(T lambda) {
        lambdas.add(lambda);
        return this;
    }

    public ArrayList<T> getLambdas() {
        return lambdas;
    }

    public boolean isEmpty() {
        return lambdas.isEmpty();
    }
}
