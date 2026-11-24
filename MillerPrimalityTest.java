import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

// Java Program for implementation of Miller-Rabin Primality Test
public class MillerPrimalityTest {

    // Fast modular exponentiation: (x^y) % p
    public static long power(long x, long y, long p) {
        long res = 1;
        x = x % p;

        while (y > 0) {
            // If y is odd, multiply x with result
            if ((y & 1) == 1) {
                res = (res * x) % p;
            }
            // y must be even now
            y >>= 1;
            x = (x * x) % p;
        }
        return res;
    }

    // Single iteration of Miller primality test
    public static boolean millerTest(long d, long n) {
        // Pick a random number 'a' in [2, n - 2]
        long a = ThreadLocalRandom.current().nextLong(2, n - 1);

        // Compute x = (a^d) % n
        long x = power(a, d, n);

        if (x == 1 || x == n - 1) {
            return true;
        }

        // Keep squaring x while d does not reach n - 1
        while (d != n - 1) {
            x = (x * x) % n;
            d *= 2;

            if (x == 1) {
                return false;
            }
            if (x == n - 1) {
                return true;
            }
        }

        return false;
    }

    // Returns true if n is prime, false if composite.
    // k is the number of iterations (higher k means higher accuracy)
    public static boolean isPrime(long n, int k) {
        // Corner cases
        if (n <= 1 || n == 4) {
            return false;
        }
        if (n <= 3) {
            return true;
        }

        // Find odd d such that n - 1 = d * 2^s
        long d = n - 1;
        while (d % 2 == 0) {
            d /= 2;
        }

        // Run Miller test k times
        for (int i = 0; i < k; i++) {
            if (!millerTest(d, n)) {
                return false;
            }
        }

        return true;
    }

    private static void runSimulation() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMiller Primality Test Simulation");
            System.out.println("1. Test a single number");
            System.out.println("2. Find primes in a range");
            System.out.println("3. Run default demonstration (1000000 to 1001000)");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter number to test: ");
                    long n = scanner.nextLong();
                    System.out.print("Enter number of iterations (k): ");
                    int k = scanner.nextInt();
                    scanner.nextLine();

                    if (isPrime(n, k)) {
                        System.out.println(n + " is probably prime.");
                    } else {
                        System.out.println(n + " is composite.");
                    }
                    break;

                case "2":
                    System.out.print("Enter lower bound: ");
                    long low = scanner.nextLong();
                    System.out.print("Enter upper bound: ");
                    long high = scanner.nextLong();
                    System.out.print("Enter number of iterations (k): ");
                    k = scanner.nextInt();
                    scanner.nextLine();

                    int count = 0;
                    System.out.println("Primes found:");
                    for (long num = low; num <= high; num++) {
                        if (isPrime(num, k)) {
                            System.out.print(num + " ");
                            count++;
                        }
                    }
                    System.out.println("\nTotal primes: " + count);
                    break;

                case "3":
                    System.out.println("Testing range 1000000 to 1001000 with k = 10:");
                    count = 0;
                    for (long num = 1000000; num <= 1001000; num++) {
                        if (isPrime(num, 10)) {
                            System.out.print(num + " ");
                            count++;
                        }
                    }
                    System.out.println("\nTotal primes: " + count);
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
