package dice.equation.expression;

import dice.equation.expression.operation.Operation;

import java.util.Map;

public class Expression implements Operand {
    private final Operation operation;

    private final Operand right;
    private final Operand left;

    public Expression(Operation operation, Operand right, Operand left) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    public double evaluate(Map<String, Integer> context) {
        return operation.resolve(left.evaluate(context), right.evaluate(context));
    }

    public StringBuilder asInfix(int level) {
        final var leftStringBuilder = left.asInfix(level + 1);
        final var rightStringBuilder = right.asInfix(level + 1);
        return leftStringBuilder
                .append(Constant.SPACE)
                .append(operation.getSymbol())
                .append(Constant.SPACE)
                .append(rightStringBuilder);
    }

    @Override
    public StringBuilder asPostFix(int level) {
        return left.asPostFix(level+1)
                .append(Constant.SPACE)
                .append(right.asPostFix(level+1))
                .append(Constant.SPACE)
                .append(operation.getSymbol());
    }

    @Override
    public String toString() {
        return asInfix(0).toString();
    }
}
