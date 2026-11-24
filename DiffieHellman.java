import java.math.BigInteger;
import java.util.Scanner;

// Java Program for implementation of Diffie-Hellman Key Exchange
public class DiffieHellman {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BigInteger alpha = readAlpha(scanner);
        BigInteger q = readPrime(scanner, alpha);
        BigInteger keyA = readPrivateKey(scanner, "keyA");
        BigInteger keyB = readPrivateKey(scanner, "keyB");

        BigInteger publicKeyA = alpha.modPow(keyA, q);
        BigInteger publicKeyB = alpha.modPow(keyB, q);

        BigInteger sharedKeyA = publicKeyB.modPow(keyA, q);
        BigInteger sharedKeyB = publicKeyA.modPow(keyB, q);

        System.out.println("\nPublic key of A: " + publicKeyA);
        System.out.println("Public key of B: " + publicKeyB);
        System.out.println("Shared key calculated by A: " + sharedKeyA);
        System.out.println("Shared key calculated by B: " + sharedKeyB);

        scanner.close();
    }

    private static BigInteger readPrime(Scanner scanner, BigInteger alpha) {
        while (true) {
            System.out.print("Enter prime number q: ");
            try {
                BigInteger q = new BigInteger(scanner.nextLine().trim());
                if (q.compareTo(alpha) > 0 && q.isProbablePrime(20)) {
                    return q;
                }
                System.out.println("q must be a prime number greater than alpha.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static BigInteger readAlpha(Scanner scanner) {
        while (true) {
            System.out.print("Enter alpha (greater than 1): ");
            try {
                BigInteger alpha = new BigInteger(scanner.nextLine().trim());
                if (alpha.compareTo(BigInteger.ONE) > 0) {
                    return alpha;
                }
                System.out.println("alpha must be greater than 1.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static BigInteger readPrivateKey(Scanner scanner, String name) {
        while (true) {
            System.out.print("Enter private key " + name + ": ");
            try {
                BigInteger key = new BigInteger(scanner.nextLine().trim());
                if (key.compareTo(BigInteger.ZERO) > 0) {
                    return key;
                }
                System.out.println(name + " must be greater than 0.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}
