package pl.cichonandrzej.stringcalculator;

import java.util.*;

/**
 * Calculator to calculate sophisticated cases in the recruitment processes:)
 * @author acichon89@gmail.com
 */
public class StringCalculator {

    private StringCalculator() {
    }

    private static final List<Character> ALLOWED_OPERATIONS = Arrays.stream(Operation.values()).map(Operation::getSymbol).toList();
    private static final List<Integer> ORDER_PRECEDENCES = Arrays.stream(Operation.values())
            .map(Operation::getOrder)
            .distinct()
            .sorted(Comparator.reverseOrder())
            .toList();

    /**
     * Calculates an Integer based on formula written in String. Currently supported operations are {@link #ALLOWED_OPERATIONS}
     * Result is converted to Integer, please mind the conversion loss during dividing.
     * @param input a valid input that must starts and ends with an integer (negative integers allowed) and surrounded by space with {@link #ALLOWED_OPERATIONS}
     *              example: '1 + 4 * -5 + 3'
     * @return a calculated result
     * @throws InvalidInputException when @param input is not valid
     */
    public static Integer calculate(String input) throws InvalidInputException {
        if (input == null || input.isEmpty() || input.replaceAll("\\s+", "").isEmpty()) {
            throw new InvalidInputException("INVALID INPUT: Input is empty!");
        }
        var tokens = input.split("\\s+");
        boolean shouldBeNumber = true;
        List<Integer> numbers = new ArrayList<>();
        List<Character> operations = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            shouldBeNumber = i % 2 == 0;
            if (shouldBeNumber) {
                try {
                    numbers.add(Integer.parseInt(tokens[i]));
                } catch (NumberFormatException e) {
                    throw new InvalidInputException("INVALID INPUT: Cannot parse input into numbers, as it may contains invalid characters");
                }
            } else {
                if (tokens[i].length() != 1) {
                    throw new InvalidInputException("INVALID INPUT: Cannot parse math operation. Only %s are allowed".formatted(ALLOWED_OPERATIONS));
                }
                if (!ALLOWED_OPERATIONS.contains(tokens[i].charAt(0))) {
                    throw new InvalidInputException("INVALID INPUT: Unrecognized/unknown math operation. Only %s are allowed".formatted(ALLOWED_OPERATIONS));
                }
                operations.add(tokens[i].charAt(0));
            }
        }
        if (!shouldBeNumber) { //check last token, should be a number
            throw new InvalidInputException("INVALID INPUT: Formula does not end with a number");
        }

        ORDER_PRECEDENCES.forEach(currentOrderValue -> {
            int idx = 0;
            while (idx < numbers.size() && idx < operations.size()) {
                Operation operation = Operation.fromSymbol(operations.get(idx));
                if (operation.getOrder() == currentOrderValue) {
                    Integer a = numbers.get(idx);
                    Integer b = numbers.get(idx + 1);
                    numbers.remove(idx);
                    numbers.remove(idx);
                    operations.remove(idx);
                    numbers.add(idx, operation.apply(a, b));
                    continue;
                }
                idx++;
            }
        });
        return numbers.get(0);
    }
}