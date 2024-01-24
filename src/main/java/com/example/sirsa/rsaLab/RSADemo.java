package com.example.sirsa.rsaLab;

public class RSADemo {
    // Calculează funcția lui Euler pentru doi numere prime date
    public static int calculateEulersTotient(int p, int q) {
        return (p - 1) * (q - 1); // Returnează produsul (p - 1) * (q - 1)
    }

    // Calculează Cel Mai Mare Divizor Comun (CMDC) folosind algoritmul lui Euclid
    public static int calculateGCD(int a, int b) {
        while (b != 0) { // Execută bucla cât timp b este diferit de 0
            int temp = a % b; // temp primește restul împărțirii a la b
            a = b; // a primește valoarea lui b
            b = temp; // b primește valoarea lui temp
        }
        return a; // Returnează a, care este CMDC(Cel mai mare divizor comun)
    }

    // Exponențiere modulară (pentru a gestiona puteri mari în aritmetica modulară)
    public static int modularExponentiation(int base, int exponent, int modulus) {
        int result = 1; // Inițializează rezultatul la 1
        base = base % modulus; // Actualizează baza ca fiind restul împărțirii la modul
        while (exponent > 0) { // Execută bucla cât timp exponentul este mai mare decât 0
            if (exponent % 2 != 0) { // Dacă exponentul este impar
                result = (result * base) % modulus; // Actualizează rezultatul
            }
            exponent = exponent >> 1; // Deplasează bitii lui exponent la dreapta cu o poziție (împarte la 2)
            base = (base * base) % modulus; // Actualizează baza
        }
        return result; // Returnează rezultatul
    }

    // Găsește Inversul Modular
    public static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) { // Iterează prin toate valorile posibile pentru x
            if ((a * x) % m == 1) { // Verifică dacă produsul a * x este congruent cu 1 modulo m
                return x; // Returnează x care este inversul modular
            }
        }
        return 1; // În cazul trivial (deși nu practic pentru RSA)
    }

    public static void main(String[] args) {
        int primeP = 61; // Definește primul număr prim, p
        int primeQ = 53; // Definește al doilea număr prim, q
        int modulusN = primeP * primeQ; // Calculează modulul N ca produsul lui p și q
        int totientN = calculateEulersTotient(primeP, primeQ); // Calculează valoarea funcției lui Euler pentru N

        // Alege o cheie publică e
        int publicKeyE = 17; // Inițializează cheia publică e cu un număr prim mic
        while (calculateGCD(publicKeyE, totientN) != 1) { // Verifică dacă CMDC dintre e și funcția lui Euler pentru N este 1
            publicKeyE++; // Incrementează e până când condiția este îndeplinită
        }

        // Calculează cheia privată d
        int privateKeyD = modInverse(publicKeyE, totientN); // Calculează inversul modular al lui e modulo funcția lui Euler pentru N

        // Afișează cheia publică și cheia privată
        System.out.println("Public key: {" + publicKeyE + ", " + modulusN + "}");
        System.out.println("Private key: {" + privateKeyD + ", " + modulusN + "}");

        int originalMessage = 123; // Definește mesajul original
        // Criptează mesajul folosind cheia publică
        int encryptedMessage = modularExponentiation(originalMessage, publicKeyE, modulusN);
        // Decriptează mesajul folosind cheia privat
        int decryptedMessage = modularExponentiation(encryptedMessage, privateKeyD, modulusN);

        System.out.println("Original message: " + originalMessage);
        System.out.println("Encrypted message: " + encryptedMessage);
        System.out.println("Decrypted message: " + decryptedMessage);
    }
}
