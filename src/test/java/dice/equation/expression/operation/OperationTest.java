package dice.equation.expression.operation;


import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Nested
    class fromSymbol{

        @Test
        void fromSymbolPlus() {
            assertEquals(Operation.PLUS, Operation.fromSymbol('+'));
        }
        @Test
        void fromSymbolMinus() {
            assertEquals(Operation.MINUS, Operation.fromSymbol('-'));
        }
        @Test
        void fromSymbolMultiply() {
            assertEquals(Operation.MULTIPLY, Operation.fromSymbol('*'));
        }
        @Test
        void fromSymbolDivide() {
            assertEquals(Operation.DIVIDE, Operation.fromSymbol('/'));
        }
        @Test
        void fromSymbolDice() {
            assertEquals(Operation.DICE, Operation.fromSymbol('d'));
        }
        @Test
        void fromSymbolInvalid(){
            assertNull(Operation.fromSymbol('a'));
        }
    }

    @Nested
    class resolve {
        @ParameterizedTest
        @MethodSource("dice.equation.expression.operation.OperationTest#resolveProvider")
        void resolvePlus(Double d1, Double d2) {
            assertEquals(d1 + d2, Operation.PLUS.resolve(d1, d2));
            assertEquals(d2 + d1, Operation.PLUS.resolve(d2, d1));
        }

        @ParameterizedTest
        @MethodSource("dice.equation.expression.operation.OperationTest#resolveProvider")
        void resolveMinus(Double d1, Double d2) {
            assertEquals(d1 - d2, Operation.MINUS.resolve(d1, d2));
            assertEquals(d2 - d1, Operation.MINUS.resolve(d2, d1));
        }

        @ParameterizedTest
        @MethodSource("dice.equation.expression.operation.OperationTest#resolveProvider")
        void resolveMultiply(Double d1, Double d2) {
            System.out.println(d1+"  "+d1);
            assertEquals(d1 * d2, Operation.MULTIPLY.resolve(d1, d2));
            assertEquals(d1 * d2, Operation.MULTIPLY.resolve(d2, d1));
        }

        @ParameterizedTest
        @MethodSource("dice.equation.expression.operation.OperationTest#resolveProvider")
        void resolveDivide(Double d1, Double d2) {
            System.out.println(d1+"  "+d1);
            assertEquals(d1 / d2, Operation.DIVIDE.resolve(d1, d2));
            assertEquals(d2 / d1, Operation.DIVIDE.resolve(d2, d1));
        }
    }

    @Nested
    class hasPrecedence {
        @Test
        void hasPrecedencePlus() {
            Operation operation = Operation.PLUS;
            assertTrue(operation.hasPrecedence(Operation.PLUS));
            assertTrue(operation.hasPrecedence(Operation.MINUS));
            assertFalse(operation.hasPrecedence(Operation.MULTIPLY));
            assertFalse(operation.hasPrecedence(Operation.DIVIDE));
            assertFalse(operation.hasPrecedence(Operation.DICE));
        }
        @Test
        void hasPrecedenceMinus() {
            Operation operation = Operation.MINUS;
            assertTrue(operation.hasPrecedence(Operation.PLUS));
            assertTrue(operation.hasPrecedence(Operation.MINUS));
            assertFalse(operation.hasPrecedence(Operation.MULTIPLY));
            assertFalse(operation.hasPrecedence(Operation.DIVIDE));
            assertFalse(operation.hasPrecedence(Operation.DICE));
        }
        @Test
        void hasPrecedenceMultiply() {
            Operation operation = Operation.MULTIPLY;
            assertTrue(operation.hasPrecedence(Operation.PLUS));
            assertTrue(operation.hasPrecedence(Operation.MINUS));
            assertTrue(operation.hasPrecedence(Operation.MULTIPLY));
            assertTrue(operation.hasPrecedence(Operation.DIVIDE));
            assertTrue(operation.hasPrecedence(Operation.DICE));
        }
        @Test
        void hasPrecedenceDivide() {
            Operation operation = Operation.DIVIDE;
            assertTrue(operation.hasPrecedence(Operation.PLUS));
            assertTrue(operation.hasPrecedence(Operation.MINUS));
            assertTrue(operation.hasPrecedence(Operation.MULTIPLY));
            assertTrue(operation.hasPrecedence(Operation.DIVIDE));
            assertTrue(operation.hasPrecedence(Operation.DICE));
        }
        @Test
        void hasPrecedenceDice() {
            Operation operation = Operation.DICE;
            assertTrue(operation.hasPrecedence(Operation.PLUS));
            assertTrue(operation.hasPrecedence(Operation.MINUS));
            assertTrue(operation.hasPrecedence(Operation.MULTIPLY));
            assertTrue(operation.hasPrecedence(Operation.DIVIDE));
            assertTrue(operation.hasPrecedence(Operation.DICE));
        }
        @Test
        void hasPrecedenceOpenBracket(){
            EnumSet<Operation> values = EnumSet.allOf(Operation.class);
            for(Operation operation: values){
                assertFalse(Operation.OPEN_BRACKET.hasPrecedence(operation),operation+" has lower precedence than '('");
            }
        }
        @Test
        void hasPrecedenceCloseBracket(){
            EnumSet<Operation> enumSet = EnumSet.complementOf(EnumSet.of(Operation.CLOSE_BRACKET));
            for(Operation operation: enumSet){
                assertFalse(Operation.CLOSE_BRACKET.hasPrecedence(operation), operation+" has higher precedence than ')'");
            }
            assertTrue(Operation.CLOSE_BRACKET.hasPrecedence(Operation.CLOSE_BRACKET));
        }

        @ParameterizedTest
        @MethodSource("dice.equation.expression.operation.OperationTest#doubleOperationProvider")
        void precedenceMatchesBehaviour(Operation op, Operation other){
            assertEquals(precedenceLong(op.getSymbol(), other.getSymbol()), op.hasPrecedence(other));
        }

    }

    private static Stream<Arguments> resolveProvider(){
        Random r = new Random();
        List<Arguments> argumentsList = new LinkedList<>();
        for( int i =0; i<1000; i++){
            argumentsList.add(() -> {
                Object[] arr = new Double[2];
                arr[0] = r.nextDouble();
                arr[1] = r.nextDouble();
                return arr;
            });
        }
        return argumentsList.stream();
    }
    private static Stream<Arguments> doubleOperationProvider(){
        LinkedList<Arguments> args = new LinkedList<>();
//        EnumSet<Operation> enumSet = EnumSet.complementOf( EnumSet.of(Operation.OPEN_BRACKET, Operation.CLOSE_BRACKET));
        EnumSet<Operation> enumSet = EnumSet.allOf(Operation.class);
        for(Operation op: enumSet){
            for (Operation other: enumSet){
                args.add(Arguments.of(op,other));
            }
        }
        return args.stream();
    }

    private static boolean precedenceLong(char a, char b) {
        String high = "*/d", low = "+-";
        if (a == '(') {
            return false;
        }
        if (a == ')' && b == '(') {
            System.out.println(")-(");
            return false;
        }
        if (b == '(') {
            //opening, do not empty stack
            return false;
        }
        if (b == ')') {
            //closing empty stack
            return true;
        }
        if (high.indexOf(a) >  - 1 && low.indexOf(b) >  - 1) {
            return true;
        }
        if (high.indexOf(a) >  - 1 && high.indexOf(b) >  - 1) {
            return true;
        }
        return low.indexOf(a) > -1 && low.indexOf(b) > -1;
    }
}