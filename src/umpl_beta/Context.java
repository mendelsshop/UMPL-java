package umpl_beta;

import java.util.Optional;

public class Context {
    public Context (int index, String input) {
            Index = index;
            Input = input;
        }

        private int Index;

        public Context IncrementIndex() {
            return new Context(Index + 1, Input);
        }

        private String Input;

        public Context (String input) {
            Index = 0;
            Input = input;
        }

        public Optional<Character> currentChar() {
            try {
                return Optional.of(Input.charAt(Index));
            } catch (IndexOutOfBoundsException e) {
                return Optional.empty();
            }
        }

        public int getIndex() {
            return Index;
        }

        public String getInput() {
            return Input;
        }
}
