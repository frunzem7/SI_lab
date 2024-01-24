package com.example.sirsa.dsaLab;

import java.math.BigInteger;

public class DSA {

    // Exemplu utilizând numere mici pentru simplitate
    static BigInteger p = new BigInteger("23"); // Într-un scenariu real, p ar trebui să fie un număr prim mare
    static BigInteger q = new BigInteger("11"); // La fel, q ar trebui să fie un număr prim mare într-un scenariu real
    static BigInteger g = new BigInteger("4");  // g ar trebui să fie o bază adecvată într-un scenariu real

    public static void main(String[] args) {
        // Generarea cheilor private și publice
        BigInteger x = new BigInteger("6"); // Cheia privată (aleasă aleatoriu)
        BigInteger y = g.modPow(x, p);      // Cheia publică g la puterea x mod p

        // Semnarea unui mesaj
        String message = "Hello, World!";
        BigInteger hashOfMessage = new BigInteger(message.getBytes()); // Hash-ul mesajului

        // k aleatoriu pentru semnare (ar trebui să fie diferit de fiecare dată)
        BigInteger k = new BigInteger("3"); // Ales aleatoriu pentru acest exemplu

        BigInteger r = g.modPow(k, p).mod(q); // Calculul lui r pentru semnătură
        BigInteger s = (k.modInverse(q).multiply(hashOfMessage.add(x.multiply(r)))).mod(q); // Calculul lui s pentru semnătură

        // Semnătura este (r, s)
        System.out.println("Signature: (r=" + r + ", s=" + s + ")");

        // Verificarea
        BigInteger w = s.modInverse(q); // Calculul lui w pentru verificare
        BigInteger u1 = hashOfMessage.multiply(w).mod(q); // Calculul lui u1 pentru verificare
        BigInteger u2 = r.multiply(w).mod(q); // Calculul lui u2 pentru verificare
        BigInteger v = ((g.modPow(u1, p).multiply(y.modPow(u2, p))).mod(p)).mod(q); // Calculul lui v pentru verificare

        // Verifică dacă v este egal cu r
        boolean isVerified = v.equals(r); // Verifică dacă semnătura este validă
        System.out.println("Verified: " + isVerified);
    }
}
