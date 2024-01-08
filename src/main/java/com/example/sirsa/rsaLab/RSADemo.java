package com.example.sirsa.rsaLab;

public class RSADemo {
    // Calculate Euler's Totient function for two given prime numbers
    public static int calculateEulersTotient(int p, int q) {
        return (p - 1) * (q - 1);
    }

    // Calculate the Greatest Common Divisor using the Euclidean algorithm
    public static int calculateGCD(int a, int b) {
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }

    // Modular Exponentiation (to handle large powers in modular arithmetic)
    public static int modularExponentiation(int base, int exponent, int modulus) {
        int result = 1;
        base = base % modulus;
        while (exponent > 0) {
            if (exponent % 2 != 0) {
                result = (result * base) % modulus;
            }
            exponent = exponent >> 1;
            base = (base * base) % modulus;
        }
        return result;
    }

    // Find Modular Multiplicative Inverse
    public static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1; // A trivial case, though not practical for RSA
    }

    public static void main(String[] args) {
        int primeP = 61;
        int primeQ = 53;
        int modulusN = primeP * primeQ;
        int totientN = calculateEulersTotient(primeP, primeQ);

        // Choose a public key e
        int publicKeyE = 17; // A small prime number
        while (calculateGCD(publicKeyE, totientN) != 1) {
            publicKeyE++;
        }

        // Calculate private key d
        int privateKeyD = modInverse(publicKeyE, totientN);

        System.out.println("Public key: {" + publicKeyE + ", " + modulusN + "}");
        System.out.println("Private key: {" + privateKeyD + ", " + modulusN + "}");

        int originalMessage = 123;
        int encryptedMessage = modularExponentiation(originalMessage, publicKeyE, modulusN);
        int decryptedMessage = modularExponentiation(encryptedMessage, privateKeyD, modulusN);

        System.out.println("Original message: " + originalMessage);
        System.out.println("Encrypted message: " + encryptedMessage);
        System.out.println("Decrypted message: " + decryptedMessage);
    }
}
