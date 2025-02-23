
import java.util.*;

public class Hangman {
    private static final int MAX_ATTEMPTS = 6;
    private static final String WORDS_FILE = "words.txt";

    private static final Map<String, List<String>> CATEGORIES = new HashMap<>();

    static {
        CATEGORIES.put("fruits", Arrays.asList("apple", "banana", "cherry", "grape", "orange", "mango", "strawberry", "pineapple", "watermelon", "blueberry"));
        CATEGORIES.put("animals", Arrays.asList("tiger", "elephant", "giraffe", "dolphin", "penguin", "kangaroo", "crocodile", "butterfly", "octopus", "hamster"));
        CATEGORIES.put("technology", Arrays.asList("computer", "keyboard", "monitor", "internet", "smartphone", "software", "hardware", "algorithm", "cybersecurity", "programming"));
        CATEGORIES.put("common", Arrays.asList("adventure", "puzzle", "challenge", "victory", "imagination", "mystery", "journey", "fantasy", "exploration", "discovery"));
    }

    private static final String[] HANGMAN_STAGES = {
            "  +---+\n      |\n      |\n      |\n     ===",
            "  +---+\n  O   |\n      |\n      |\n     ===",
            "  +---+\n  O   |\n  |   |\n      |\n     ===",
            "  +---+\n  O   |\n /|   |\n      |\n     ===",
            "  +---+\n  O   |\n /|\\  |\n      |\n     ===",
            "  +---+\n  O   |\n /|\\  |\n /    |\n     ===",
            "  +---+\n  O   |\n /|\\  |\n / \\  |\n     ==="
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            playGame(scanner);
            System.out.print("\nDo you want to play again? (yes/no): ");
        } while (scanner.next().equalsIgnoreCase("yes"));

        System.out.println("Thanks for playing! Goodbye!");
        scanner.close();
    }

    private static void playGame(Scanner scanner) {
        String category = chooseCategory(scanner);
        String word = getRandomWord(category);

        if (word.equals("error")) {
            System.out.println("Error loading words. Make sure 'words.txt' exists.");
            return;
        }

        Set<Character> guessedLetters = new HashSet<>();
        Set<Character> wrongLetters = new HashSet<>();
        int wrongGuesses = 0;

        while (wrongGuesses < MAX_ATTEMPTS) {
            System.out.println(HANGMAN_STAGES[wrongGuesses]);
            System.out.println("\nWord: " + getMaskedWord(word, guessedLetters));
            System.out.println("Wrong guesses: " + wrongLetters);

            System.out.print("Enter a letter: ");
            String input = scanner.next().toLowerCase();

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Invalid input! Please enter a single letter.");
                continue;
            }

            char letter = input.charAt(0);
            if (guessedLetters.contains(letter) || wrongLetters.contains(letter)) {
                System.out.println("You already guessed that letter.");
                continue;
            }

            if (word.contains(String.valueOf(letter))) {
                guessedLetters.add(letter);
            } else {
                wrongLetters.add(letter);
                wrongGuesses++;
            }

            if (isWordGuessed(word, guessedLetters)) {
                System.out.println("\nCongratulations! You guessed the word: " + word);
                return;
            }
        }

        System.out.println(HANGMAN_STAGES[MAX_ATTEMPTS]);
        System.out.println("You lost! The correct word was: " + word);
    }

    private static String chooseCategory(Scanner scanner) {
        System.out.println("Choose a category: " + CATEGORIES.keySet());
        String category;
        while (true) {
            System.out.print("Enter category: ");
            category = scanner.next().toLowerCase();
            if (CATEGORIES.containsKey(category)) break;
            System.out.println("Invalid category! Please choose from " + CATEGORIES.keySet());
        }
        return category;
    }

    private static String getRandomWord(String category) {
        List<String> words = CATEGORIES.getOrDefault(category, Collections.emptyList());
        if (words.isEmpty()) return "error";
        return words.get(new Random().nextInt(words.size()));
    }

    private static String getMaskedWord(String word, Set<Character> guessedLetters) {
        StringBuilder maskedWord = new StringBuilder();
        for (char c : word.toCharArray()) {
            maskedWord.append(guessedLetters.contains(c) ? c : "_");
        }
        return maskedWord.toString();
    }

    private static boolean isWordGuessed(String word, Set<Character> guessedLetters) {
        for (char c : word.toCharArray()) {
            if (!guessedLetters.contains(c)) return false;
        }
        return true;
    }
}
