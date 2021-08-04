package dice.equation.interpreter;

import dice.equation.expression.Expression;
import dice.equation.expression.Operand;
import dice.equation.expression.Value;
import dice.equation.expression.Variable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {
    private static final Map<String, Integer> EMPTY_MAP = Collections.emptyMap();
    private static final LinkedHashMap<String, Integer> ABCD_MAP= new LinkedHashMap<>();

    private Interpreter interpreter = new Interpreter();
    private Map<String, Integer> context = new HashMap<>();

    @BeforeAll
    static void setMap(){
        ABCD_MAP.put("A",1);
        ABCD_MAP.put("B",2);
        ABCD_MAP.put("C",3);
        ABCD_MAP.put("E",4);
    }

    @Nested
    class VariableTests {

        @ParameterizedTest
        @MethodSource("dice.equation.interpreter.InterpreterTest#equationConversion")
        void postFixCreation(String postfix, String infix){
            assertEquals(postfix, interpreter.createFromPostfix(postfix).asPostFix(0).toString());
//            assertEquals(infix, interpreter.createFromPostfix(postfix).asInfix(0).toString());
            assertEquals(postfix, interpreter.createFromInfix(infix).asPostFix(0).toString());
//            assertEquals(infix, interpreter.createFromInfix(infix).asInfix(0).toString());
        }

        @Test
        void PostFixToSolved(){
            assertEquals(11, interpreter.createFromPostfix("A B C * + E + ").evaluate(ABCD_MAP));
            assertEquals(21, interpreter.createFromPostfix("A B + C E + * ").evaluate(ABCD_MAP));
            assertEquals(14, interpreter.createFromPostfix("A B * C E * + ").evaluate(ABCD_MAP));
            assertEquals(20, interpreter.createFromPostfix("A B C + * E * ").evaluate(ABCD_MAP));
            assertEquals(14, interpreter.createFromPostfix("A B * C E * + ").evaluate(ABCD_MAP));
        }
    }

    @Nested
    class wholeProcessTests {

        @Nested
        class trivalMaths{
            @Test
            void FiveEqualsFive(){
                Operand exp = interpreter.createFromPostfix("5");
                assertTrue(exp instanceof Value);
                assertEquals(5, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("5");
                assertTrue(exp instanceof Value);
                assertEquals(5, exp.evaluate(EMPTY_MAP));
            }
            @Test
            void MinusFiveEqualsMinusFive(){
                Operand exp = interpreter.createFromPostfix("-5");
                assertTrue(exp instanceof Value);
                assertEquals(-5, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("-5");
                assertTrue(exp instanceof Value);
                assertEquals(-5, exp.evaluate(EMPTY_MAP));
            }
            @Test
            void ThreeEqualsThreeByMap(){
                Operand exp = interpreter.createFromPostfix("C");
                assertTrue(exp instanceof Variable);
                assertEquals(3, exp.evaluate(ABCD_MAP));
                exp = interpreter.createFromInfix("C");
                assertTrue(exp instanceof Variable);
                assertEquals(3, exp.evaluate(ABCD_MAP));
            }
        }

        @Nested
        class simpleMaths {
            @ParameterizedTest
            @MethodSource("dice.equation.interpreter.InterpreterTest#simpleEquations")
            void simpleEquations(final String infix, final double expectedResult, final Map<String, Integer> map){
                final Operand exp = interpreter.createFromInfix(infix);
                assertTrue(exp instanceof Expression);
                assertEquals(expectedResult, exp.evaluate(map));
            }
//            @Test
//            void OneMinusOneEqualsZero(){
//                final Operand exp = interpreter.createFromInfix("1 - 1");
//                assertTrue(exp instanceof Expression);
//                assertEquals(0, exp.evaluate(ABCD_MAP));
//            }
//
//            @Test
//            void TwoTimesThreeEqualsSix(){
//                final Operand exp = interpreter.createFromInfix("2 * 3");
//                assertTrue(exp instanceof Expression);
//                assertEquals(6, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void ThreeTimesTwoEqualsSix(){
//                final Operand exp = interpreter.createFromInfix("3 * 2");
//                assertTrue(exp instanceof Expression);
//                assertEquals(6, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void TwoDividedThreeEqualsTwoThirds(){
//                final Operand exp = interpreter.createFromInfix("2 / 3");
//                assertTrue(exp instanceof Expression);
//                assertEquals(2d / 3d, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void ThreeDividedTwoEqualsOnePointFive(){
//                final Operand exp = interpreter.createFromInfix("3 / 2");
//                assertTrue(exp instanceof Expression);
//                assertEquals(1.5, exp.evaluate(ABCD_MAP));
////            }
//            @Test
//            void OnePlusOneEqualsTwoByMap(){
//                final Operand exp = interpreter.createFromInfix("A + A");
//                assertTrue(exp instanceof Expression);
//                assertEquals(2, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void OneMinusOneEqualsZeroByMap(){
//                final Operand exp = interpreter.createFromInfix("A - A");
//                assertTrue(exp instanceof Expression);
//                assertEquals(0, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void TwoTimesThreeEqualsSixByMap(){
//                final Operand exp = interpreter.createFromInfix("B * C");
//                assertTrue(exp instanceof Expression);
//                assertEquals(6, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void ThreeTimesTwoEqualsSixByMap(){
//                final Operand exp = interpreter.createFromInfix("C * B");
//                assertTrue(exp instanceof Expression);
//                assertEquals(6, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void TwoDividedThreeEqualsTwoThirdsByMap(){
//                final Operand exp = interpreter.createFromInfix("B / C");
//                assertTrue(exp instanceof Expression);
//                assertEquals(2d / 3d, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void ThreeDividedTwoEqualsOnePointFiveByMap(){
//                final Operand exp = interpreter.createFromInfix("C / B");
//                assertTrue(exp instanceof Expression);
//                assertEquals(1.5, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void ThreeDiceOneEqualsThree(){
//                final Operand exp = interpreter.createFromInfix("3 d 1");
//                assertTrue(exp instanceof Expression);
//                assertEquals(3, exp.evaluate(ABCD_MAP));
//            }
//            @Test
//            void ThreeDiceOneEqualsThreeByMap(){
//                final Operand exp = interpreter.createFromInfix("C d A");
//                assertTrue(exp instanceof Expression);
//                assertEquals(3, exp.evaluate(ABCD_MAP));
//            }
        }

        @Nested
        class complexMaths {
            @Test
            void multiplicationResolveBeforeAddition(){
                Operand exp = interpreter.createFromInfix("1 + 2 * 2");
                assertEquals(5, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("1 * 2 + 2");
                assertEquals(4, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("2 + 2 * 1");
                assertEquals(4, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("2 * 2 + 1");
                assertEquals(5, exp.evaluate(EMPTY_MAP));
            }
            @Test
            void divisionResolveBeforeAddition(){
                Operand exp = interpreter.createFromInfix("1 + 2 / 2");
                assertEquals(2, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("1 / 2 + 2");
                assertEquals(2.5, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("2 / 2 + 1");
                assertEquals(2, exp.evaluate(EMPTY_MAP));
                exp = interpreter.createFromInfix("2 + 2 / 1");
                assertEquals(4, exp.evaluate(EMPTY_MAP));
            }
        }

    }

    @ParameterizedTest
    @MethodSource
    void createFromPostfix(String expression, Double result) {
        assertEquals(result, interpreter.createFromPostfix(expression).evaluate(context));
    }

    @ParameterizedTest
    @MethodSource
    void createFromPostfixDice(final String expression, final Double min, final Double max, final Double average) {
        final List<Double> results = new LinkedList<>();
        for(int i =0; i < 10000; i++){
            double result = interpreter.createFromPostfix(expression).evaluate(context);
            assertTrue(result <=max);
            assertTrue(result >= min);
            results.add(result);
        }
        OptionalDouble mean = results.stream().mapToDouble(d-> d).average();
        assertTrue(mean.isPresent());
        assertEquals(mean.getAsDouble(), average, 0.1);
    }

    @ParameterizedTest
    @MethodSource
    void createFromInfixWithoutBrackets(final String infix){
        final Operand fromInfix = interpreter.createFromInfix(infix);
        final String calculatedTree = fromInfix.toString();
        System.out.println(infix +" converts to "+fromInfix+" and equals "+ fromInfix.evaluate(context));
        assertEquals(infix, calculatedTree);
    }

    @ParameterizedTest
    @MethodSource
    void createFromInfixWithBrackets(final String infix, final String result){
        final Operand fromInfix = interpreter.createFromInfix(infix);
        final String calculatedTree = fromInfix.asInfix(0).toString();
        System.out.println(infix +" converts to "+fromInfix+" and equals "+ fromInfix.evaluate(context));
        assertEquals(result, calculatedTree);
    }

    private static Stream<Arguments> createFromPostfixDice(){
        return Stream.of(
                Arguments.of("5 2 d", 5d, 10d, 7.5d),
                Arguments.of("3 2 + 2 d", 5d, 10d, 7.5d),
                Arguments.of("2 5 d", 2d, 10d, 6d),
                Arguments.of("1 20 d", 1d, 20d, 10.5d),
                Arguments.of("20 1 d", 20d, 20d, 20d),
                Arguments.of("5 2 d 5 +", 10d, 15d, 12.5d),
                Arguments.of("2 5 d 5 +", 7d, 17d, 11d),
                Arguments.of("1 20 d 5 +", 6d, 25d, 15.5d),
                Arguments.of("20 1 d 5 +", 25d, 25d, 25d)
        );
    }

    private static Stream<Arguments> createFromPostfix(){
        return Stream.of(
                Arguments.of("5 2 +", 7d),
                Arguments.of("5 2 *", 10d),
                Arguments.of("5 2 -", 3d),
                Arguments.of("5 2 /", 2.5d)
        );
    }

    private static Stream<Arguments> createFromInfixWithoutBrackets(){
        return Stream.of(
                Arguments.of("5.0 + 2.0"),
                Arguments.of("5.0 * 2.0"),
                Arguments.of("5.0 - 2.0"),
                Arguments.of("5.0 / 2.0"),
                Arguments.of("5.0 + 2.0 + 8.0"),
                Arguments.of("5.0 * 2.0 + 8.0"),
                Arguments.of("5.0 - 2.0 + 8.0"),
                Arguments.of("5.0 / 2.0 + 8.0"),
                Arguments.of("5.0 + 2.0 * 8.0"),
                Arguments.of("5.0 + 2.0 - 8.0"),
                Arguments.of("5.0 + 2.0 / 8.0"),
                Arguments.of("5.0 - 2.0 + 8.0"),
                Arguments.of("5.0 - 2.0 * 8.0"),
                Arguments.of("5.0 - 2.0 - 8.0"),
                Arguments.of("5.0 - 2.0 / 8.0"),
                Arguments.of("5.0 * 2.0 + 8.0"),
                Arguments.of("5.0 * 2.0 * 8.0"),
                Arguments.of("5.0 * 2.0 - 8.0"),
                Arguments.of("5.0 * 2.0 / 8.0"),
                Arguments.of("5.0 / 2.0 + 8.0"),
                Arguments.of("5.0 / 2.0 * 8.0"),
                Arguments.of("5.0 / 2.0 - 8.0"),
                Arguments.of("5.0 / 2.0 / 8.0")
        );
    }

    private static Stream<Arguments> createFromInfixWithBrackets(){
        return Stream.of(
                Arguments.of("( 5.0 + 2.0 )", "5.0 + 2.0"),
                Arguments.of("( 5.0 * 2.0 )", "5.0 * 2.0"),
                Arguments.of("( 5.0 - 2.0 )", "5.0 - 2.0"),
                Arguments.of("( 5.0 / 2.0 )", "5.0 / 2.0"),
                Arguments.of("( 5.0 * 2.0 ) + ( 8.0 * 2.0 )", "5.0 * 2.0 + 8.0 * 2.0"), // 5.0 2.0 * 8.0 2.0 * +
                Arguments.of(" 5.0 * 2.0  +  8.0 * 2.0 ", "5.0 * 2.0 + 8.0 * 2.0"),
                Arguments.of(" 5.0 * ( 2.0  +  8.0 ) * 2.0 ", "5.0 * 2.0 + 8.0 * 2.0")
//                Arguments.of("5.0 * 2.0 + 8.0"),
//                Arguments.of("5.0 - 2.0 + 8.0"),
//                Arguments.of("5.0 / 2.0 + 8.0"),
//                Arguments.of("5.0 + 2.0 * 8.0"),
//                Arguments.of("5.0 + 2.0 - 8.0"),
//                Arguments.of("5.0 + 2.0 / 8.0"),
//                Arguments.of("5.0 - 2.0 + 8.0"),
//                Arguments.of("5.0 - 2.0 * 8.0"),
//                Arguments.of("5.0 - 2.0 - 8.0"),
//                Arguments.of("5.0 - 2.0 / 8.0"),
//                Arguments.of("5.0 * 2.0 + 8.0"),
//                Arguments.of("5.0 * 2.0 * 8.0"),
//                Arguments.of("5.0 * 2.0 - 8.0"),
//                Arguments.of("5.0 * 2.0 / 8.0"),
//                Arguments.of("5.0 / 2.0 + 8.0"),
//                Arguments.of("5.0 / 2.0 * 8.0"),
//                Arguments.of("5.0 / 2.0 - 8.0"),
//                Arguments.of("5.0 / 2.0 / 8.0")
        );
    }

    private static Stream<Arguments> equationConversion(){
        return Stream.of(
                Arguments.of("A B C * + E +", "A + B * C + E"),
                Arguments.of("A B + C E + *", "( A + B ) * ( C + E )"),
                Arguments.of("A B * C E * +", "A * B + C * E"),
                Arguments.of("A B C + * E *", "A * ( B + C ) * E"),
                Arguments.of("A B * C E * +", "A * B + C * E")
        );
    }
    private static Stream<Arguments> simpleEquations(){
        return Stream.of(
                Arguments.of("1 + 1", 2, EMPTY_MAP),
                Arguments.of("1 - 1", 0, EMPTY_MAP),
                Arguments.of("2 * 3", 6, EMPTY_MAP),
                Arguments.of("3 * 2", 6, EMPTY_MAP),
                Arguments.of("2 / 3", 2d/3d, EMPTY_MAP),
                Arguments.of("3 / 2", 1.5, EMPTY_MAP),
                Arguments.of("A + A", 2, ABCD_MAP),
                Arguments.of("A - A", 0, ABCD_MAP),
                Arguments.of("B * C", 6, ABCD_MAP),
                Arguments.of("C * B", 6, ABCD_MAP),
                Arguments.of("B / C", 2d/3d, ABCD_MAP),
                Arguments.of("3 d 1", 3, EMPTY_MAP),
                Arguments.of("C d A", 3, ABCD_MAP)
        );
    }
}