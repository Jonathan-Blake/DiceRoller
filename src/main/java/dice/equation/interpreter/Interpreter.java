package dice.equation.interpreter;

import dice.equation.expression.*;
import dice.equation.expression.operation.Operation;

import java.util.Deque;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Set;

public class Interpreter {

    private final EnumSet<Operation> supportedOperations = EnumSet.allOf(Operation.class);
    private final PostfixConvertor postfixConvertor = new PostfixConvertor(this);

    public Operand createFromInfix(final String expression){
        return createFromPostfix(postfixConvertor.convertToPostfix(expression));
    }
    public Operand createFromPostfix(final String expression){
        return buildSyntaxTree(expression);
    }
    public Set<Operation> getSupportedOperations(){
        return supportedOperations;
    }

    private Operand buildSyntaxTree(final String tree) {
        final Deque<Operand> stack = new LinkedList<>();
        final String[] tokens = tree.split(Constant.SPACE);
        if (tokens.length == 1) {
            return getOperandFromToken(tokens[0]);
        }
        for (final String token : tokens){
            final var currentOperation = Operation.fromSymbol(token.charAt(0));
            if (supportedOperations.contains(currentOperation)) {// If token is an operator
                stack.push(new Expression(currentOperation, stack.pop(), stack.pop()));
            } else {
                stack.push(getOperandFromToken(token));
            }
        }
        return stack.pop();
    }

    private Operand getOperandFromToken(final String token) {
        try {
            return new Value(Double.parseDouble(token));
        } catch (NumberFormatException ex) {
            return new Variable(token);
        }
    }
}
