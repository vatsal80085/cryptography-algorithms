import java.util.Scanner;

public class AffineCipher {
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

    private static boolean isValidA(int a) {
        return gcd(a, LETTERS) == 1;
    }

    private static int modInverse(int a) {
        a = Math.floorMod(a, LETTERS);

        for (int i = 1; i < LETTERS; i++) {
            if ((a * i) % LETTERS == 1) {
                return i;
            }
        }

        return -1;
    }

    private static char encryptChar(char ch, int a, int b) {
        if (ch >= 'A' && ch <= 'Z') {
            int x = ch - 'A';
            return (char) (Math.floorMod(a * x + b, LETTERS) + 'A');
        }

        if (ch >= 'a' && ch <= 'z') {
            int x = ch - 'a';
            return (char) (Math.floorMod(a * x + b, LETTERS) + 'a');
        }

        return ch;
    }

    private static char decryptChar(char ch, int aInverse, int b) {
        if (ch >= 'A' && ch <= 'Z') {
            int y = ch - 'A';
            return (char) (Math.floorMod(aInverse * (y - b), LETTERS) + 'A');
        }

        if (ch >= 'a' && ch <= 'z') {
            int y = ch - 'a';
            return (char) (Math.floorMod(aInverse * (y - b), LETTERS) + 'a');
        }

        return ch;
    }

    public static String encrypt(String plain, int a, int b) {
        if (!isValidA(a)) {
            throw new IllegalArgumentException("Key a must be coprime with 26.");
        }

        String cipher = "";
        a = Math.floorMod(a, LETTERS);
        b = Math.floorMod(b, LETTERS);

        for (int i = 0; i < plain.length(); i++) {
            cipher += encryptChar(plain.charAt(i), a, b);
        }

        return cipher;
    }

    public static String decrypt(String cipher, int a, int b) {
        if (!isValidA(a)) {
            throw new IllegalArgumentException("Key a must be coprime with 26.");
        }

        String plain = "";
        int aInverse = modInverse(a);
        b = Math.floorMod(b, LETTERS);

        for (int i = 0; i < cipher.length(); i++) {
            plain += decryptChar(cipher.charAt(i), aInverse, b);
        }

        return plain;
    }

    private static int readAKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter key a (must be coprime with 26): ");

            if (scanner.hasNextInt()) {
                int a = scanner.nextInt();
                scanner.nextLine();

                if (isValidA(a)) {
                    return Math.floorMod(a, LETTERS);
                }

                System.out.println("Invalid key. Valid a values include 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25.");
            } else {
                System.out.println("Please enter a valid integer key.");
                scanner.nextLine();
            }
        }
    }

    private static int readBKey(Scanner scanner) {
        while (true) {
            System.out.print("Enter key b: ");

            if (scanner.hasNextInt()) {
                int b = scanner.nextInt();
                scanner.nextLine();
                return Math.floorMod(b, LETTERS);
            }

            System.out.println("Please enter a valid integer key.");
            scanner.nextLine();
        }
    }

    private static void bruteForceDecrypt(String cipher) {
        System.out.println("\nPossible decryptions:");

        for (int a = 0; a < LETTERS; a++) {
            if (isValidA(a)) {
                for (int b = 0; b < LETTERS; b++) {
                    System.out.printf("a=%2d, b=%2d: %s%n", a, b, decrypt(cipher, a, b));
                }
            }
        }
    }

    private static void runSimulation() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAffine Cipher Simulation");
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
                    int a = readAKey(scanner);
                    int b = readBKey(scanner);
                    System.out.println("Ciphertext: " + encrypt(plain, a, b));
                    break;

                case "2":
                    System.out.print("Enter ciphertext: ");
                    String cipher = scanner.nextLine();
                    a = readAKey(scanner);
                    b = readBKey(scanner);
                    System.out.println("Plaintext: " + decrypt(cipher, a, b));
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
