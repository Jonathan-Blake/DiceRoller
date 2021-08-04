package dice.equation.expression.operation;

import dice.equation.expression.Constant;

import java.util.function.BinaryOperator;

import static dice.equation.expression.operation.Precedence.*;

public enum Operation {

    PLUS('+', Double::sum, LOW),
    MINUS('-', (a, b) -> a-b, LOW),
    MULTIPLY('*', (a, b) -> a*b, HIGH),
    DIVIDE('/', (a, b) -> a/b, HIGH),
    DICE('d', (a, b) -> a * ( Constant.RANDOM.nextInt(b.intValue())+ 1 ), HIGH),
    OPEN_BRACKET('(', (a,b)-> {
        throw new InvalidOperationException("Attempted to execute Bracket as operation");
    }, BRACKET),
    CLOSE_BRACKET(')', (a,b)-> {
        throw new InvalidOperationException("Attempted to execute Bracket as operation");
    }, BRACKET);


    private final char symbol;
    private final BinaryOperator<Double> function;
    private final Precedence precedence;

    Operation(char symbol, BinaryOperator<Double> function, Precedence precedence) {
        this.symbol = symbol;
        this.function = function;
        this.precedence = precedence;
    }

    public static Operation fromSymbol(char symbol) {
        for(Operation operation: values()){
            if(symbol == operation.getSymbol()){
                return operation;
            }
        }
        return null;
    }

    public double resolve(double a, double b){
        return function.apply(a,b);
    }

    public char getSymbol() {
        return this.symbol;
    }

    public Precedence getPrecedence() {
        return this.precedence;
    }

    public boolean hasPrecedence(final Operation op) {
        if (this == OPEN_BRACKET) {
            return false;
        }
        if (this == CLOSE_BRACKET && op == OPEN_BRACKET) {
            System.out.println(")-(");
            return false;
        }
        if (op == OPEN_BRACKET) {
            //opening, do not empty stack
            return false;
        }
        if (op == CLOSE_BRACKET) {
            //closing empty stack
            return true;
        }
        return getPrecedence().compareTo(op.getPrecedence())>=0;
    }
}

enum Precedence implements Comparable<Precedence> {
    BRACKET(Integer.MIN_VALUE),
    LOW(1),
    HIGH(2);

    final int value;

    Precedence(int i) {
        this.value = i;
    }
}