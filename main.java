import java.math.BigInteger;
import java.util.Random;

public class main {

    // Method 1: Brute Force Evaluation
    public static BigInteger bruteForceEvaluation(BigInteger[] coefficients, BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < coefficients.length; i++) {
            BigInteger term = coefficients[i].multiply(power(x, i));
            result = result.add(term);
        }
        return result;
    }

    // Helper method for brute force to calculate x^i without using Math.pow()
    private static BigInteger power(BigInteger x, int exp) {
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < exp; i++) {
            result = result.multiply(x);
        }
        return result;
    }

    // Method 2: Repeated Squaring Evaluation for each monomial
    public static BigInteger repeatedSquaringEvaluation(BigInteger[] coefficients, BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < coefficients.length; i++) {
            BigInteger term = coefficients[i].multiply(repeatedSquaring(x, i));
            result = result.add(term);
        }
        return result;
    }

    // Helper method for repeated squaring
    private static BigInteger repeatedSquaring(BigInteger x, int exp) {
        if (exp == 0) return BigInteger.ONE;
        BigInteger half = repeatedSquaring(x, exp / 2);
        if (exp % 2 == 0) return half.multiply(half);
        else return half.multiply(half).multiply(x);
    }

    // Method 3: Horner's Rule Evaluation
    public static BigInteger hornerEvaluation(BigInteger[] coefficients, BigInteger x) {
        BigInteger result = BigInteger.ZERO;
        for (int i = coefficients.length - 1; i >= 0; i--) {
            result = result.multiply(x).add(coefficients[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        int n = 25; // Degree of the polynomial for overflow test
        BigInteger x = BigInteger.valueOf(123); // Value of x for overflow test

        // Generate coefficients for overflow test
        BigInteger[] coefficients = new BigInteger[n + 1];
        for (int i = 0; i <= n; i++) {
            coefficients[i] = BigInteger.valueOf(i); // Example coefficients
        }

        // Overflow test
        System.out.println("Overflow Test:");
        System.out.println("Brute Force Result (may overflow): " + bruteForceEvaluation(coefficients, x));
        System.out.println("Repeated Squaring Result (may overflow): " + repeatedSquaringEvaluation(coefficients, x));
        System.out.println("Horner's Rule Result (may overflow): " + hornerEvaluation(coefficients, x));

        // Performance test with larger values of n and x
        n = 1000; // Large polynomial degree for performance testing
        int d = 5000; // Number of digits for each coefficient
        x = BigInteger.valueOf(2); // Small x to avoid overflow, but still allows long computation
        coefficients = generateLargeCoefficients(n, d);

        // Brute Force Method
        long startTime = System.currentTimeMillis();
        BigInteger bruteForceResult = bruteForceEvaluation(coefficients, x);
        long endTime = System.currentTimeMillis();
        System.out.println("\nBrute Force Evaluation Time: " + (endTime - startTime) + " ms");

        // Repeated Squaring Method
        startTime = System.currentTimeMillis();
        BigInteger repeatedSquaringResult = repeatedSquaringEvaluation(coefficients, x);
        endTime = System.currentTimeMillis();
        System.out.println("Repeated Squaring Evaluation Time: " + (endTime - startTime) + " ms");

        // Horner's Rule Method
        startTime = System.currentTimeMillis();
        BigInteger hornerResult = hornerEvaluation(coefficients, x);
        endTime = System.currentTimeMillis();
        System.out.println("Horner's Rule Evaluation Time: " + (endTime - startTime) + " ms");

        // Check if all results are the same
        System.out.println("\nAll results are equal: " +
                (bruteForceResult.equals(repeatedSquaringResult) && repeatedSquaringResult.equals(hornerResult)));
    }

    // Method to generate large coefficients with a specific number of digits for performance testing
    private static BigInteger[] generateLargeCoefficients(int n, int d) {
        Random rand = new Random();
        BigInteger[] coefficients = new BigInteger[n + 1];
        BigInteger min = BigInteger.TEN.pow(d - 1); // Smallest number with d digits
        BigInteger range = BigInteger.TEN.pow(d).subtract(min); // Range to ensure d digits

        for (int i = 0; i <= n; i++) {
            BigInteger randomCoefficient = min.add(new BigInteger(d, rand).mod(range));
            coefficients[i] = randomCoefficient;
        }
        return coefficients;
    }
}

