package dice.equation.expression;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class VariableTest {
    Variable v = new Variable("V");

    @Test
    void evaluateWhenVariableDoesntExist() {
        HashMap<String, Integer> context = new HashMap<>();
        assertEquals(0, v.evaluate(context));
    }
    @Test
    void evaluateWhenVariableDoesExist() {
        HashMap<String, Integer> context = new HashMap<>();
        context.put("V", 1000);
        assertEquals(1000, v.evaluate(context));
    }
}