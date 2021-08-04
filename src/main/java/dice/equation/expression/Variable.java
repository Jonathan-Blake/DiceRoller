package dice.equation.expression;

import java.util.Map;

public class Variable implements Operand {
    private final String token;

    public Variable(String token) {
        this.token = token;
    }

    @Override
    public double evaluate(Map<String, Integer> context) {
        return context.getOrDefault(token, 0);
    }

    @Override
    public StringBuilder asInfix(int level) {
        return new StringBuilder(token);
    }

    @Override
    public StringBuilder asPostFix(int level) {
        return asInfix(level);
    }

    @Override
    public String toString() {
        return asInfix(0).toString();
    }
}
