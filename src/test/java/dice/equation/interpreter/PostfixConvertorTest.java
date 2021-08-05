package dice.equation.interpreter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PostfixConvertorTest {


    private PostfixConvertor converter = new PostfixConvertor(new Interpreter().getSupportedOperations());

    @ParameterizedTest
    @MethodSource("dice.equation.interpreter.PostfixConvertorTest#equationConversion")
    void postFixCreation(String postfix, String infix){
        assertEquals(postfix, converter.convertToPostfix(infix).trim());
    }

    private static Stream<Arguments> equationConversion(){
        return Stream.of(
                Arguments.of("A B C * + E +", "A + B * C + E"),
                Arguments.of("A B C * + E +", "A + ( B * C ) + E"),
                Arguments.of("A B * C E * +", "A * B + C * E"),
                Arguments.of("A B * C E * +", "( A * B ) + ( C * E )"),
                Arguments.of("A B + C E + *", "( A + B ) * ( C + E )"),
                Arguments.of("A B C + * E *", "A * ( B + C ) * E"),
                Arguments.of("A", "A"),
                Arguments.of("A B * C E * +", "A   *   B +    C *    E")
        );
    }
}