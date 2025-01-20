package pl.cichonandrzej.stringcalculator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringCalculatorTest {

    @Nested
    class ValidationCases {

        @ParameterizedTest
        @ValueSource(strings = {
                "",
                "  ",
        })
        @NullSource
        void validateEmptyInput(String input) {
            //given
            //when
            Exception exception = assertThrows(InvalidInputException.class, () -> {
                StringCalculator.calculate(input);
            });
            //then
            assertEquals("INVALID INPUT: Input is empty!", exception.getMessage());
        }
        @ParameterizedTest
        @ValueSource(strings = {
                "1 + !2",
                "a",
                "1 + a",
                "1 ^ 2",
                "#fff000",
                "-",
                "10 +",
                "1 2",
                "1  2",
                "+ 5 + 2",
                "1 -5"
        })
        void validateInvalidFormula(String input) {
            //given
            //when
            Exception exception = assertThrows(InvalidInputException.class, () -> {
                StringCalculator.calculate(input);
            });
            //then
            assertTrue(exception.getMessage().startsWith("INVALID INPUT:"));
        }
    }

    @Nested
    class CalculationCases {
        @ParameterizedTest(name = "''{0}'' = {1}")
        @MethodSource("calculationCases")
        void calculationTests(String input, Integer expectedResult) throws InvalidInputException {
            assertEquals(expectedResult, StringCalculator.calculate(input));
        }

        private static Stream<Arguments> calculationCases() {
            return Stream.of(
                    Arguments.of("1 + 2 * 3 - 4", 3),
                    Arguments.of("1 + 2 * 3 * 4", 25),
                    Arguments.of("1 + 2 - 3 - 4", -4),
                    Arguments.of("1 * 2 * 3 * 4", 24),
                    Arguments.of("1 + 2 * 3 - 4", 3),
                    Arguments.of("3 / 3 * 3 / 3", 1),
                    Arguments.of("3 + 3 + 3 + 3 + 3 + 3 + 3", 21),

                    Arguments.of("1 / 2 * 3 * 4", 0),
                    Arguments.of("6 / 3", 2),
                    Arguments.of("1 + 6 / 3 * 4", 9),
                    Arguments.of("1 + 6 / 3 + 4", 7)
            );
        }
    }
}