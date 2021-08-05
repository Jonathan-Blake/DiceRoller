package dice.equation.expression;

import java.util.Map;

public interface Operand {
    double evaluate(Map<String, Integer> context);

    StringBuilder asInfix(int level);

    StringBuilder asPostFix(int level);
}
