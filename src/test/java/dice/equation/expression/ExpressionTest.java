package dice.equation.expression;

import dice.equation.expression.operation.Operation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    Map<String, Integer> context = new HashMap<>();
    public static final Random RANDOM = new Random();
    public static final EnumSet<Operation> OPERATIONS_WITHOUT_DICE = EnumSet.complementOf(EnumSet.of(Operation.DICE,Operation.CLOSE_BRACKET,Operation.OPEN_BRACKET));

    @ParameterizedTest
    @MethodSource
    void evaluateSimple(Double d1, Double d2, Operation operation) {
        Expression exp = new Expression(operation, new Value(d2), new Value(d1));
        assertEquals(exp.evaluate(context), operation.resolve(d1,d2));
    }
    @ParameterizedTest
    @MethodSource
    void evaluateComplex(Operand d1, Operand d2, Operation operation) {
        Expression exp = new Expression(operation, d2, d1);
        assertEquals(exp.evaluate(context), operation.resolve(d1.evaluate(context),d2.evaluate(context)));
    }

    private static Stream<Arguments> evaluateSimple(){
        return Stream.generate(()-> Arguments.of( RANDOM.nextDouble(),
                RANDOM.nextDouble(),
                getRandomOperationWithoutDice())
        ).limit(1000);
    }

    private static Stream<Arguments> evaluateComplex(){
        return Stream.generate(() ->Arguments.of(generateExpression(0), generateExpression(0), getRandomOperationWithoutDice())).limit(1000);
    }

    private static Operand generateExpression(int recursion) {
        if (recursion >10) {
            return new Value(RANDOM.nextDouble());
        }
        return switch (RANDOM.nextInt(2)) {
            case 0 -> new Expression(getRandomOperationWithoutDice(), generateExpression(recursion + 1), generateExpression(recursion + 1));
            case 1 -> new Value(RANDOM.nextDouble());
            default -> new Value(0);
        };
    }

    private static Operation getRandomOperationWithoutDice() {
        return OPERATIONS_WITHOUT_DICE.toArray(new Operation[0])[RANDOM.nextInt(OPERATIONS_WITHOUT_DICE.size())];
    }
}