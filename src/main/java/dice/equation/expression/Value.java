package dice.equation.expression;

import java.util.Map;

public class Value implements Operand {
    private final double content;

    public Value(double v) {
        this.content = v;
    }

    @Override
    public double evaluate(Map<String, Integer> context) {
        return content;
    }

    @Override
    public StringBuilder asInfix(int level) {
        return new StringBuilder(String.valueOf(content));
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
