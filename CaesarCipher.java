import java.util.Scanner;

public class CaesarCipher {
    private static final int letters = 26;

    private static char shift(char ch, int key) {
        if (ch >= 'A' && ch <= 'Z') {
            return (char) ((ch - 'A' + key + letters) % letters + 'A');
        }

        if (ch >= 'a' && ch <= 'z') {
            return (char) ((ch - 'a' + key + letters) % letters + 'a');
        }

        return ch;
    }

    public static String encrypt(String plain, int key) {
        String cipher = "";
        key = Math.floorMod(key, letters);

        for (int i = 0; i < plain.length(); i++) {
            cipher += shift(plain.charAt(i), key);
        }

        return cipher;
    }

    public static String decrypt(String cipher, int key) {
        return encrypt(cipher, -key);
    }

    private static int readKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter shift key: ");

            if (scanner.hasNextInt()) {
                int key = scanner.nextInt();
                scanner.nextLine();
                return Math.floorMod(key, letters);
            }

            System.out.println("Please enter a valid integer key.");
            scanner.nextLine();
        }
    }

    private static void bruteForceDecrypt(String cipher) {
        System.out.println("\nPossible decryptions:");

        for (int key = 0; key < letters; key++) {
            System.out.printf("Key %2d: %s%n", key, decrypt(cipher, key));
        }
    }

    private static void runSimulation() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nCaesar Cipher Simulation");
            System.out.println("1. Encrypt message");
            System.out.println("2. Decrypt message");
            System.out.println("3. Brute-force decrypt");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter plaintext: ");
                    String plain = scanner.nextLine();
                    int key = readKey(scanner);
                    System.out.println("Ciphertext: " + encrypt(plain, key));
                    break;

                case "2":
                    System.out.print("Enter ciphertext: ");
                    String cipher = scanner.nextLine();
                    key = readKey(scanner);
                    System.out.println("Plaintext: " + decrypt(cipher, key));
                    break;

                case "3":
                    System.out.print("Enter ciphertext: ");
                    cipher = scanner.nextLine();
                    bruteForceDecrypt(cipher);
                    break;

                case "4":
                    System.out.println("Goodbye.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Choose 1, 2, 3, or 4.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        runSimulation();
    }
}
