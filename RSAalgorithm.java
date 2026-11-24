// Java Program for implementation of RSA Algorithm

import java.math.BigInteger;
import java.util.Scanner;

public class RSAalgorithm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigInteger[] keys = new BigInteger[3]; // e, d, n

        BigInteger p = readPrime(scanner, "p");
        BigInteger q;

        do {
            q = readPrime(scanner, "q");

            if (p.equals(q)) {
                System.out.println("p and q should be different prime numbers.");
            }
        } while (p.equals(q));

        generateKeys(p, q, keys);

        System.out.println("Public Key (e, n): (" + keys[0] + ", " + keys[2] + ")");
        System.out.println("Private Key (d, n): (" + keys[1] + ", " + keys[2] + ")");

        int choice = readChoice(scanner);

        if (choice == 1) {
            BigInteger message = readNumber(scanner, "message", keys[2]);
            BigInteger cipher = encrypt(message, keys[0], keys[2]);

            System.out.println("Encrypted Message: " + cipher);
        } else {
            BigInteger cipher = readNumber(scanner, "ciphertext", keys[2]);
            BigInteger message = decrypt(cipher, keys[1], keys[2]);

            System.out.println("Decrypted Message: " + message);
        }

        scanner.close();
    }

    static BigInteger readPrime(Scanner scanner, String name) {
        while (true) {
            System.out.print("Enter prime number " + name + ": ");
            String input = scanner.nextLine();

            try {
                BigInteger number = new BigInteger(input);

                if (isPrime(number)) {
                    return number;
                }

                System.out.println(name + " must be a prime number greater than 1.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    static boolean isPrime(BigInteger number) {
        return number.compareTo(BigInteger.ONE) > 0 && number.isProbablePrime(10);
    }

    // RSA Key Generation
    static void generateKeys(BigInteger p, BigInteger q, BigInteger[] keys) {
        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose e, where 1 < e < phi(n) and gcd(e, phi(n)) == 1
        BigInteger e;
        for (e = new BigInteger("2"); e.compareTo(phi) < 0; e = e.add(BigInteger.ONE)) {
            if (e.gcd(phi).equals(BigInteger.ONE)) {
                break;
            }
        }

        // Compute d such that e * d = 1 (mod phi(n))
        BigInteger d = modInverse(e, phi);

        keys[0] = e; // Public Key (e)
        keys[1] = d; // Private Key (d)
        keys[2] = n; // Modulus (n)
    }

    static BigInteger modInverse(BigInteger e, BigInteger phi) {
        return e.modInverse(phi);
    }

    static int readChoice(Scanner scanner) {
        while (true) {
            System.out.println("\n1. Encrypt a message");
            System.out.println("2. Decrypt a ciphertext");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine();

            if (input.equals("1")) {
                return 1;
            }

            if (input.equals("2")) {
                return 2;
            }

            System.out.println("Please choose 1 or 2.");
        }
    }

    static BigInteger readNumber(Scanner scanner, String name, BigInteger n) {
        while (true) {
            System.out.print("Enter " + name + " as a number less than " + n + ": ");
            String input = scanner.nextLine();

            try {
                BigInteger number = new BigInteger(input);

                if (number.compareTo(BigInteger.ZERO) >= 0 && number.compareTo(n) < 0) {
                    return number;
                }

                System.out.println(name + " must be from 0 to " + n.subtract(BigInteger.ONE) + ".");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Encrypt message using public key (e, n)
    static BigInteger encrypt(BigInteger message, BigInteger e, BigInteger n) {
        return power(message, e, n);
    }

    // Decrypt message using private key (d, n)
    static BigInteger decrypt(BigInteger cipher, BigInteger d, BigInteger n) {
        return power(cipher, d, n);
    }

    static BigInteger power(BigInteger base, BigInteger expo, BigInteger m) {
        return base.modPow(expo, m);
    }
}
