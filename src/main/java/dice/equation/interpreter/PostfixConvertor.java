package dice.equation.interpreter;

import dice.equation.expression.Constant;
import dice.equation.expression.operation.Operation;

import java.util.Deque;
import java.util.LinkedList;

public class PostfixConvertor {
    //TODO: Move tests out of the Interpreter class into this.
    private final Interpreter interpreter;

    public PostfixConvertor(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    String convertToPostfix(final String expr) {
        //TODO: Cognitive complexity
        final Deque<Operation> operationsStack = new LinkedList<>();
        final var out = new StringBuilder();
        Operation topSymbol = Operation.PLUS;
        boolean empty;
        final String[] tokens = expr.trim().split(Constant.SPACE);
        if (tokens.length == 1) {
            return expr;
        }
        for (String token : tokens) {
            if (token.isBlank()) {
                System.out.println("Skipping Blank token, please check formula for double spaces.");
                continue;
            }
            var currentOperation = Operation.fromSymbol(token.charAt(0));
            if (!interpreter.getSupportedOperations().contains(currentOperation)) {
                out.append(token);
                out.append(Constant.SPACE);
            } else {
                while (!(empty = operationsStack.isEmpty()) &&
                        (topSymbol = operationsStack.pop()).hasPrecedence(currentOperation)
                ) {
                    out.append(topSymbol.getSymbol()).append(Constant.SPACE);
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
        }
        operationsStack.forEach(op -> out.append(op.getSymbol()).append(Constant.SPACE));
        final var ret = out.toString();
        System.out.println("Converted to postfix : " + expr + " -> " + ret);
        return ret;
    }
}