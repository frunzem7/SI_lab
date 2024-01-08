package com.example.sirsa.dsaLab;

import java.math.BigInteger;

public class DSA {

    // Example using small numbers for simplicity
    static BigInteger p = new BigInteger("23"); // Replace with a large prime in a real scenario
    static BigInteger q = new BigInteger("11"); // Replace with a large prime in a real scenario
    static BigInteger g = new BigInteger("4");  // Replace with a proper base in a real scenario

    public static void main(String[] args) {
        // Generate private and public keys
        BigInteger x = new BigInteger("6"); // Private key (randomly chosen)
        BigInteger y = g.modPow(x, p);      // Public key

        // Sign a message
        String message = "Hello, World!";
        BigInteger hashOfMessage = new BigInteger(message.getBytes());

        // Random k for signing (should be different each time)
        BigInteger k = new BigInteger("3"); // Randomly chosen for this example

        BigInteger r = g.modPow(k, p).mod(q);
        BigInteger s = (k.modInverse(q).multiply(hashOfMessage.add(x.multiply(r)))).mod(q);

        // Signature is (r, s)
        System.out.println("Signature: (r=" + r + ", s=" + s + ")");

        // Verification
        BigInteger w = s.modInverse(q);
        BigInteger u1 = hashOfMessage.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = ((g.modPow(u1, p).multiply(y.modPow(u2, p))).mod(p)).mod(q);

        // Check if v equals r
        boolean isVerified = v.equals(r);
        System.out.println("Verified: " + isVerified);
    }
}
