import java.util.Scanner;

/**
 * Hill Cipher - A polygraphic substitution cipher using matrix operations.
 * 
 * The Hill Cipher encrypts plaintext by treating pairs of letters as column vectors,
 * multiplying them by a key matrix (mod 26), and converting the result back to letters.
 * 
 * Key Requirements:
 * - Uses a 2x2 key matrix for encryption/decryption
 * - The matrix determinant must be coprime with 26 for decryption to be possible
 * - Operates on pairs of letters (digraphs)
 */
public class HillCipher {
    // Total number of letters in English alphabet
    private static final int LETTERS = 26;

    /**
     * Calculates the Greatest Common Divisor (GCD) of two integers.
     * Uses Euclidean algorithm for efficient computation.
     * 
     * @param a First integer
     * @param b Second integer
     * @return GCD of a and b (always positive)
     */
    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    /**
     * Computes the modular multiplicative inverse of a value modulo 26.
     * The modular inverse of 'a' (mod m) is a number 'x' such that (a * x) % m == 1
     * 
     * @param value The value to find the inverse for
     * @return The modular inverse (1-25), or -1 if no inverse exists
     */
    private static int modInverse(int value) {
        value = Math.floorMod(value, LETTERS);

        for (int candidate = 1; candidate < LETTERS; candidate++) {
            if ((value * candidate) % LETTERS == 1) {
                return candidate;
            }
        }

        return -1;
    }

    /**
     * Validates that the key matrix is suitable for Hill Cipher encryption/decryption.
     * 
     * For a 2x2 key matrix, the determinant must be coprime with 26.
     * This ensures the matrix is invertible mod 26, allowing decryption.
     * Determinant = (a*d - b*c), where key = [[a, b], [c, d]]
     * 
     * @param key The 2x2 key matrix to validate
     * @throws IllegalArgumentException if determinant is not coprime with 26
     */
    private static void validateKey(int[][] key) {
        // Calculate 2x2 determinant: det = a*d - b*c
        int determinant = key[0][0] * key[1][1] - key[0][1] * key[1][0];

        // Check if GCD(determinant, 26) == 1 (coprime condition)
        if (gcd(determinant, LETTERS) != 1) {
            throw new IllegalArgumentException(
                    "The matrix determinant must be coprime with 26.");
        }
    }

    /**
     * Computes the inverse of a 2x2 key matrix for decryption.
     * 
     * Matrix inversion formula for 2x2 matrix:
     * If key = [[a, b], [c, d]], then key^(-1) = (1/det) * [[d, -b], [-c, a]]
     * All operations are performed modulo 26.
     * 
     * @param key The 2x2 encryption key matrix
     * @return The inverse matrix used for decryption
     */
    private static int[][] inverseKey(int[][] key) {
        // Calculate determinant
        int determinant = key[0][0] * key[1][1] - key[0][1] * key[1][0];
        
        // Find modular inverse of determinant
        int determinantInverse = modInverse(determinant);
        
        // Create adjugate matrix: [[d, -b], [-c, a]]
        int[][] inverse = {
                {key[1][1], -key[0][1]},
                {-key[1][0], key[0][0]}
        };

        // Multiply by determinant inverse and apply modulo 26
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 2; column++) {
                inverse[row][column] = Math.floorMod(
                        determinantInverse * inverse[row][column], LETTERS);
            }
        }

        return inverse;
    }

    /**
     * Converts an integer value to a character (A-Z or a-z).
     * The value is reduced modulo 26 to map to valid alphabet positions.
     * 
     * @param value The integer value to convert (0-25 represents A/a-Z/z)
     * @param uppercase If true, returns uppercase (A-Z); if false, lowercase (a-z)
     * @return The corresponding letter character
     */
    private static char toCharacter(int value, boolean uppercase) {
        // Determine base character: 'A' for uppercase, 'a' for lowercase
        char firstLetter = uppercase ? 'A' : 'a';
        
        // Map value to letter: value % 26 gives position in alphabet, add to base
        return (char) (Math.floorMod(value, LETTERS) + firstLetter);
    }

    /**
     * Core transformation logic for both encryption and decryption.
     * 
     * Encryption Process:
     * 1. Extract letters from message, storing positions and case info
     * 2. Pad with 'X' if odd number of letters (for encryption only)
     * 3. Process letter pairs: [p1, p2] = key * [p1, p2] mod 26
     * 4. Reconstruct message with transformed letters, preserving positions
     * 
     * Decryption uses the inverse key matrix (handled by caller).
     * 
     * @param message The text to encrypt/decrypt
     * @param key The key matrix (encryption or inverse for decryption)
     * @param padOddLength If true, pad with 'X' for odd letters; if false, throw error
     * @return The transformed message with same structure as input
     */
    private static String transform(String message, int[][] key, boolean padOddLength) {
        // Store numeric values of letters (0-25 for A-Z, a-z)
        int[] letterValues = new int[message.length() + 1];
        StringBuilder transformed = new StringBuilder();
        
        // Track original positions and case of letters in message
        int[] letterPositions = new int[message.length()];
        boolean[] uppercase = new boolean[message.length()];
        int letterCount = 0;

        // Extract all letters from message, recording their positions and case
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);

            if (ch >= 'A' && ch <= 'Z') {
                letterValues[letterCount] = ch - 'A';
                letterPositions[letterCount] = i;
                uppercase[letterCount] = true;
                letterCount++;
            } else if (ch >= 'a' && ch <= 'z') {
                letterValues[letterCount] = ch - 'a';
                letterPositions[letterCount] = i;
                uppercase[letterCount] = false;
                letterCount++;
            }
        }

        // Hill Cipher requires even number of letters for matrix multiplication
        if (letterCount % 2 != 0) {
            if (!padOddLength) {
                throw new IllegalArgumentException(
                        "The ciphertext must contain an even number of letters.");
            }
            // Pad with 'X' (value 23)
            letterValues[letterCount] = 'X' - 'A';
            letterPositions = growPositions(letterPositions, letterCount + 1);
            uppercase = growCaseFlags(uppercase, letterCount + 1);
            letterPositions[letterCount] = message.length();
            uppercase[letterCount] = true;
            letterCount++;
        }

        // Apply matrix transformation to each pair of letters
        // [c1] = [a b] [p1]
        // [c2]   [c d] [p2]  (mod 26)
        for (int i = 0; i < letterCount; i += 2) {
            int first = letterValues[i];
            int second = letterValues[i + 1];
            
            // First letter: a*p1 + b*p2 (mod 26)
            transformed.append(toCharacter(
                    key[0][0] * first + key[0][1] * second, uppercase[i]));
            
            // Second letter: c*p1 + d*p2 (mod 26)
            transformed.append(toCharacter(
                    key[1][0] * first + key[1][1] * second, uppercase[i + 1]));
        }

        // Reconstruct message: place transformed letters back at original positions
        StringBuilder result = new StringBuilder(message);
        int transformedIndex = 0;

        for (int i = 0; i < message.length(); i++) {
            if (isLetter(message.charAt(i))) {
                result.setCharAt(i, transformed.charAt(transformedIndex++));
            }
        }

        // If padding was added, append the padded character to result
        if (letterCount > message.replaceAll("[^A-Za-z]", "").length()) {
            result.append(transformed.charAt(transformedIndex));
        }

        return result.toString();
    }

    /**
     * Expands an integer array to a larger size.
     * Used to accommodate padding when adding extra letters.
     * 
     * @param positions The original array
     * @param requiredLength The new required length
     * @return A new array with expanded size, original elements copied
     */
    private static int[] growPositions(int[] positions, int requiredLength) {
        int[] expanded = new int[requiredLength];
        System.arraycopy(positions, 0, expanded, 0, positions.length);
        return expanded;
    }

    /**
     * Expands a boolean array to a larger size.
     * Used to track case (uppercase/lowercase) information for padded letters.
     * 
     * @param flags The original boolean array
     * @param requiredLength The new required length
     * @return A new array with expanded size, original elements copied
     */
    private static boolean[] growCaseFlags(boolean[] flags, int requiredLength) {
        boolean[] expanded = new boolean[requiredLength];
        System.arraycopy(flags, 0, expanded, 0, flags.length);
        return expanded;
    }

    /**
     * Checks if a character is a letter (A-Z or a-z).
     * 
     * @param ch The character to check
     * @return true if ch is a letter, false otherwise
     */
    private static boolean isLetter(char ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z');
    }

    /**
     * Encrypts plaintext using the Hill Cipher.
     * 
     * Process:
     * 1. Validate the key matrix (determinant must be coprime with 26)
     * 2. Transform plaintext by multiplying letter pairs with key matrix
     * 3. Pad odd-length text with 'X' to ensure even number of letters
     * 
     * @param plain The plaintext to encrypt (letters, digits, punctuation)
     * @param key The 2x2 encryption key matrix
     * @return The encrypted ciphertext with same structure as plaintext
     * @throws IllegalArgumentException if key matrix is invalid
     */
    public static String encrypt(String plain, int[][] key) {
        validateKey(key);
        return transform(plain, key, true);
    }

    /**
     * Decrypts ciphertext using the Hill Cipher.
     * 
     * Process:
     * 1. Validate the key matrix
     * 2. Compute the inverse of the key matrix
     * 3. Transform ciphertext using the inverse matrix
     * 4. Do not pad since ciphertext has exact even number of letters
     * 
     * @param cipher The ciphertext to decrypt
     * @param key The 2x2 encryption key matrix (used to compute inverse)
     * @return The decrypted plaintext
     * @throws IllegalArgumentException if key matrix is invalid
     */
    public static String decrypt(String cipher, int[][] key) {
        validateKey(key);
        return transform(cipher, inverseKey(key), false);
    }

    /**
     * Reads an integer from user input with validation.
     * Keeps prompting until a valid integer is entered.
     * 
     * @param scanner The Scanner object for reading input
     * @param prompt The message to display to the user
     * @return The valid integer entered by the user
     */
    private static int readInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);

            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();  // Consume newline
                return value;
            }

            System.out.println("Please enter a valid integer.");
            scanner.nextLine();  // Clear invalid input
        }
    }

    /**
     * Reads a 2x2 key matrix from user input with validation.
     * Keeps prompting until a valid key is entered.
     * 
     * Valid key: determinant must be coprime with 26 for invertibility.
     * 
     * @param scanner The Scanner object for reading input
     * @return A valid 2x2 key matrix
     */
    private static int[][] readKey(Scanner scanner) {
        while (true) {
            System.out.println("Enter the 2x2 key matrix:");
            int[][] key = {
                    {readInteger(scanner, "a: "), readInteger(scanner, "b: ")},
                    {readInteger(scanner, "c: "), readInteger(scanner, "d: ")}
            };

            try {
                validateKey(key);
                return key;
            } catch (IllegalArgumentException exception) {
                System.out.println("Invalid key: " + exception.getMessage());
            }
        }
    }

    /**
     * Runs the interactive Hill Cipher simulation.
     * Provides a menu for users to encrypt/decrypt messages and manage keys.
     * 
     * Menu Options:
     * 1. Encrypt a plaintext message with a given key
     * 2. Decrypt a ciphertext message with a given key
     * 3. Exit the program
     */
    private static void runSimulation() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nHill Cipher Simulation");
            System.out.println("1. Encrypt message");
            System.out.println("2. Decrypt message");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Encryption workflow
                    System.out.print("Enter plaintext: ");
                    String plain = scanner.nextLine();
                    int[][] encryptionKey = readKey(scanner);
                    System.out.println("Ciphertext: " + encrypt(plain, encryptionKey));
                    break;

                case "2":
                    // Decryption workflow
                    System.out.print("Enter ciphertext: ");
                    String cipher = scanner.nextLine();
                    int[][] decryptionKey = readKey(scanner);
                    System.out.println("Plaintext: " + decrypt(cipher, decryptionKey));
                    break;

                case "3":
                    // Exit program
                    System.out.println("Goodbye.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Choose 1, 2, or 3.");
                    break;
            }
        }
    }

    /**
     * Main entry point for the Hill Cipher program.
     * Launches the interactive simulation.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        runSimulation();
    }
}
