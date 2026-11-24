import java.util.Scanner;

public class MultiplicativeCipher {
    private static final int LETTERS = 26;

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

    private static boolean isValidKey(int key) {
        return gcd(key, LETTERS) == 1;
    }

    private static int modInverse(int key) {
        key = Math.floorMod(key, LETTERS);

        for (int i = 1; i < LETTERS; i++) {
            if ((key * i) % LETTERS == 1) {
                return i;
            }
        }

        return -1;
    }

    private static char encryptChar(char ch, int key) {
        if (ch >= 'A' && ch <= 'Z') {
            int x = ch - 'A';
            return (char) (Math.floorMod(key * x, LETTERS) + 'A');
        }

        if (ch >= 'a' && ch <= 'z') {
            int x = ch - 'a';
            return (char) (Math.floorMod(key * x, LETTERS) + 'a');
        }

        return ch;
    }

    private static char decryptChar(char ch, int keyInverse) {
        if (ch >= 'A' && ch <= 'Z') {
            int y = ch - 'A';
            return (char) (Math.floorMod(keyInverse * y, LETTERS) + 'A');
        }

        if (ch >= 'a' && ch <= 'z') {
            int y = ch - 'a';
            return (char) (Math.floorMod(keyInverse * y, LETTERS) + 'a');
        }

        return ch;
    }

    public static String encrypt(String plain, int key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Key must be coprime with 26.");
        }

        String cipher = "";
        key = Math.floorMod(key, LETTERS);

        for (int i = 0; i < plain.length(); i++) {
            cipher += encryptChar(plain.charAt(i), key);
        }

        return cipher;
    }

    public static String decrypt(String cipher, int key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Key must be coprime with 26.");
        }

        String plain = "";
        int keyInverse = modInverse(key);

        for (int i = 0; i < cipher.length(); i++) {
            plain += decryptChar(cipher.charAt(i), keyInverse);
        }

        return plain;
    }

    private static int readKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter key (must be coprime with 26): ");

            if (scanner.hasNextInt()) {
                int key = scanner.nextInt();
                scanner.nextLine();

                if (isValidKey(key)) {
                    return Math.floorMod(key, LETTERS);
                }

                System.out.println("Invalid key. Valid keys include 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25.");
            } else {
                System.out.println("Please enter a valid integer key.");
                scanner.nextLine();
            }
        }
    }

    private static void bruteForceDecrypt(String cipher) {
        System.out.println("\nPossible decryptions:");

        for (int key = 0; key < LETTERS; key++) {
            if (isValidKey(key)) {
                System.out.printf("Key %2d: %s%n", key, decrypt(cipher, key));
            }
        }
    }

    private static void runSimulation() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMultiplicative Cipher Simulation");
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
