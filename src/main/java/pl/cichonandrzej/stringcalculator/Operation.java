package pl.cichonandrzej.stringcalculator;

import java.util.function.BiFunction;

enum Operation {
        ADD('+', 50, (a, b) -> a + b),
        SUBTRACT('-', 50, (a, b) -> a - b),
        MULTIPLY('*', 100, (a, b) -> a * b),
        DIVIDE('/', 100, (a, b) -> a / b);

        Operation(Character symbol, Integer order, BiFunction<Integer, Integer, Integer> func) {
            this.symbol = symbol;
            this.order = order;
            this.func = func;
        }

        private final Character symbol;
        private final int order;

        private final BiFunction<Integer, Integer, Integer> func;

        public Character getSymbol() {
            return symbol;
        }

        public int getOrder() {
            return order;
        }

        public Integer apply(Integer a, Integer b) {
            return this.func.apply(a, b);
        }

        public static Operation fromSymbol(Character symbol) {
            for (Operation operation : Operation.values()) {
                if (operation.getSymbol().equals(symbol)) {
                    return operation;
                }
            }
            throw new IllegalArgumentException("Invalid symbol: " + symbol);
        }
    }