import java.util.Scanner;

public class ExtendedEuclidean {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Input x and y
        int x = sc.nextInt();
        int y = sc.nextInt();

        int alphaX, alphaY;
        int betaX, betaY;

        // Let αy ← 0, βx ← 0
        alphaY = 0;
        betaX = 0;

        // if x >= 0 then
        if (x >= 0) {
            // x ← x
            x = x;

            // αx ← 1
            alphaX = 1;
        }
        // else
        else {
            // x ← -x
            x = -x;

            // αx ← -1
            alphaX = -1;
        }

        // if y >= 0 then
        if (y >= 0) {
            // y ← y
            y = y;

            // βy ← 1
            betaY = 1;
        }
        // else
        else {
            // y ← -y
            y = -y;

            // βy ← -1
            betaY = -1;
        }

        // if x > y
        if (x > y) {

            // swap x, y
            int temp = x;
            x = y;
            y = temp;

            // swap αx, αy
            temp = alphaX;
            alphaX = alphaY;
            alphaY = temp;

            // swap βx, βy
            temp = betaX;
            betaX = betaY;
            betaY = temp;
        }

        // while x > 0 do
        while (x > 0) {

            // q ← floor(y / x)
            int q = y / x;

            // x ← y - q
            // In the algorithm this means the remainder:
            // r = y - q*x
            int r = y - q * x;

            // αx ← αy - q*αx
            int alphaR = alphaY - q * alphaX;

            // βx ← βy - q*βx
            int betaR = betaY - q * betaX;

            // y ← x
            y = x;

            // αy ← αx
            alphaY = alphaX;

            // βy ← βx
            betaY = betaX;

            // x ← r
            x = r;

            // αx ← αr
            alphaX = alphaR;

            // βx ← βr
            betaX = betaR;
        }

        // output y and (αy, βy)
        System.out.println("gcd = " + y);
        System.out.println("α = " + alphaY);
        System.out.println("β = " + betaY);

        sc.close();
    }
}