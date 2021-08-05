package dice.equation.interpreter;

import dice.equation.expression.Constant;
import dice.equation.expression.operation.Operation;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Set;

public class PostfixConvertor {
    //TODO: Move tests out of the Interpreter class into this.
    private final Set<Operation> supportedOperations;

    public PostfixConvertor(Set<Operation> operationSet) {
        this.supportedOperations = operationSet;
    }

    String convertToPostfix(final String expr) {
        final Deque<Operation> operationsStack = new LinkedList<>();
        final var out = new StringBuilder();
        Operation topSymbol = Operation.PLUS;
        final String[] tokens = expr.trim().split(Constant.SPACE);
        if (tokens.length == 1) {
            return expr;
        }
        Arrays.stream(tokens).sequential().filter(token -> !token.isBlank()).forEach(token -> handleToken(operationsStack, out, token, topSymbol));
        operationsStack.forEach(op -> appendSymbol(out, op.getSymbol()));
        final var ret = out.toString();
        System.out.println("Converted to postfix : " + expr + " -> " + ret);
        return ret;
    }

    private Operation handleToken(final Deque<Operation> operationsStack, final StringBuilder out, final String token, Operation topSymbol) {
        boolean empty;
        final var currentOperation = Operation.fromSymbol(token.charAt(0));
        if (!supportedOperations.contains(currentOperation)) {
            out.append(token).append(Constant.SPACE);
        } else {
            while (!(empty = operationsStack.isEmpty()) &&
                    (topSymbol = operationsStack.pop()).hasPrecedence(currentOperation)
            ) {
                appendSymbol(out, topSymbol.getSymbol());
            }
            if (!empty) {
                operationsStack.push(topSymbol);
            }
            if (empty || currentOperation != Operation.CLOSE_BRACKET) {
                operationsStack.push(currentOperation);
            } else {
                topSymbol = operationsStack.pop();
            }
        }
        return topSymbol;
    }

    private StringBuilder appendSymbol(StringBuilder out, char symbol) {
        return out.append(symbol).append(Constant.SPACE);
    }
}