// Java Program for implementation of RSA Digital Signature

import java.math.BigInteger;
import java.util.Scanner;

public class RSADigitalSignature {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BigInteger p = readPrime(scanner, "p");
        BigInteger q;

        do {
            q = readPrime(scanner, "q");
            if (p.equals(q)) {
                System.out.println("p and q must be different prime numbers.");
            }
        } while (p.equals(q));

        BigInteger n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = readPublicExponent(scanner, phi);
        BigInteger d = e.modInverse(phi);

        System.out.println("\nPublic Key (e, n): (" + e + ", " + n + ")");
        System.out.println("Private Key (d, n): (" + d + ", " + n + ")");
        System.out.println("Note: the message is used directly as h(x), since no external hash algorithm is used.");

        while (true) {
            System.out.println("\n1. Sign a message");
            System.out.println("2. Verify a signature");
            System.out.println("3. Recover a message from ciphertext");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": {
                    BigInteger messageToSign = readNumber(scanner, "message hash h(x)", n);
                    BigInteger signature = sign(messageToSign, d, n);
                    System.out.println("Signature: " + signature);
                    break;
                }

                case "2": {
                    BigInteger messageToVerify = readNumber(scanner, "message hash h(x)", n);
                    BigInteger signatureToVerify = readNumber(scanner, "signature", n);
                    boolean valid = verify(messageToVerify, signatureToVerify, e, n);
                    System.out.println(valid
                            ? "Signature is valid."
                            : "Signature is invalid.");
                    break;
                }

                case "3": {
                    BigInteger ciphertext = readNumber(scanner, "ciphertext c", n);
                    BigInteger message = recoverMessage(ciphertext, d, n);
                    System.out.println("n = p * q = " + n);
                    System.out.println("Recovered message m: " + message);
                    break;
                }

                case "4":
                    scanner.close();
                    return;

                default:
                    System.out.println("Please choose 1, 2, 3, or 4.");
            }
        }
    }

    static BigInteger readPrime(Scanner scanner, String name) {
        while (true) {
            System.out.print("Enter prime number " + name + ": ");
            String input = scanner.nextLine();

            try {
                BigInteger number = new BigInteger(input);
                if (number.isProbablePrime(20)) {
                    return number;
                }
                System.out.println(name + " must be a prime number.");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    static BigInteger readPublicExponent(Scanner scanner, BigInteger phi) {
        while (true) {
            System.out.print("Enter public exponent e: ");
            String input = scanner.nextLine();

            try {
                BigInteger e = new BigInteger(input);
                if (e.compareTo(BigInteger.ONE) > 0
                        && e.compareTo(phi) < 0
                        && e.gcd(phi).equals(BigInteger.ONE)) {
                    return e;
                }
                System.out.println("e must be greater than 1, less than phi(n), and coprime to phi(n).");
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
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

    // Create signature: s = h(x)^d mod n
    static BigInteger sign(BigInteger messageHash, BigInteger d, BigInteger n) {
        return messageHash.modPow(d, n);
    }

    // Verify signature: h(x) == s^e mod n
    static boolean verify(BigInteger messageHash, BigInteger signature, BigInteger e, BigInteger n) {
        BigInteger recoveredHash = signature.modPow(e, n);
        return recoveredHash.equals(messageHash);
    }

    // Recover message: m = c^d mod n
    static BigInteger recoverMessage(BigInteger ciphertext, BigInteger d, BigInteger n) {
        return ciphertext.modPow(d, n);
    }
}
