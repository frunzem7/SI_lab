package com.example.sirsa.desLab;

public class SDES {
    // Permutarea inițială
    public static int[] IP = {1, 5, 2, 0, 3, 7, 4, 6};

    // Permutarea inversă inițială
    public static int[] IPI = {3, 0, 2, 4, 6, 1, 7, 5};

    // Permutarea de expansiune
    public static int[] EP = {3, 0, 1, 2, 1, 2, 3, 0};

    // Permutarea de 10 biți
    public static int[] P10 = {2, 4, 1, 6, 3, 9, 0, 8, 7, 5};

    // Permutarea de 8 biți
    public static int[] P8 = {5, 2, 6, 3, 7, 4, 9, 8};

    // Permutarea de 4 biți
    public static int[] P4 = {1, 3, 2, 0};

    // Definirea S-box-ului S0
    public static int[][] S0 = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2}
    };

    // Definirea S-box-ului S1
    public static int[][] S1 = {
            {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}
    };

    // Metodă utilitară pentru permutare
    public static int permute(int[] sequence, int number, int bitSize) {
        int result = 0;
        for (int i = 0; i < sequence.length; i++) {
            result |= ((number >> bitSize - 1 - sequence[i]) & 1) << bitSize - 1 - i;
        }
        return result;
    }

    // Metodă pentru generarea cheilor
    public static int[] keyGeneration(int key) {
        int permutedKey = permute(P10, key, 10); // Permută cheia cu P10
        int left = (permutedKey >> 5) & 0x1F; // Extrage jumătatea stângă
        int right = permutedKey & 0x1F; // Extrage jumătatea dreaptă

        // Deplasări la stânga
        left = ((left << 1) & 0x1F) | (left >> 4);
        right = ((right << 1) & 0x1F) | (right >> 4);

        int firstKey = permute(P8, (left << 5) | right, 10); // Prima cheie generată

        // A doua deplasare la stânga
        left = ((left << 2) & 0x1F) | (left >> 3);
        right = ((right << 2) & 0x1F) | (right >> 3);

        int secondKey = permute(P8, (left << 5) | right, 10); // A doua cheie generată

        return new int[]{firstKey, secondKey};
    }

    // Funcția fk utilizată în structura Feistel
    public static int fk(int input, int key) {
        int left = input >> 4; // Extrage jumătatea stângă
        int right = input & 0xF; // Extrage jumătatea dreaptă
        return (left ^ f(right, key)) << 4 | right; // Aplică XOR între stânga și f(right, key), apoi combină cu partea dreaptă
    }

    // Implementarea funcției f
    public static int f(int input, int key) {
        input = permute(EP, input, 4); // Aplică permutarea de expansiune
        input ^= key; // Aplică XOR cu cheia

        int left = (input >> 4) & 0xF; // Extrage jumătatea stângă
        int right = input & 0xF; // Extrage jumătatea dreaptă

        int row, col;
        row = ((left & 1) << 1) | (left >> 3);
        col = ((left >> 1) & 0x3);
        int s0Val = S0[row][col]; // Valoare din S0

        row = ((right & 1) << 1) | (right >> 3);
        col = ((right >> 1) & 0x3);
        int s1Val = S1[row][col]; // Valoare din S1

        return permute(P4, (s0Val << 2) | s1Val, 4); // Aplică permutarea P4
    }

    // Metoda de criptare
    public static int encrypt(int input, int key) {
        int[] keys = keyGeneration(key); // Generează cheile
        input = permute(IP, input, 8); // Aplică permutarea inițială

        // Prima aplicare a funcției fk cu prima cheie
        input = fk(input, keys[0]);

        // Interschimbă jumătățile stânga și dreapta
        input = ((input & 0xF) << 4) | (input >> 4);

        // A doua aplicare a funcției fk cu a doua cheie
        input = fk(input, keys[1]);

        return permute(IPI, input, 8); // Aplică permutarea inversă inițială
    }

    // Metoda de decriptare
    public static int decrypt(int input, int key) {
        int[] keys = keyGeneration(key); // Generează cheile
        input = permute(IP, input, 8); // Aplică permutarea inițială

        // Prima aplicare a funcției fk cu a doua cheie
        input = fk(input, keys[1]);

        // Interschimbă jumătățile stânga și dreapta
        input = ((input & 0xF) << 4) | (input >> 4);

        // A doua aplicare a funcției fk cu prima cheie
        input = fk(input, keys[0]);

        return permute(IPI, input, 8); // Aplică permutarea inversă inițială
    }

    public static void main(String[] args) {
        int key = 0b1010000010; // Definește o cheie de 10 biți ca exemplu
        int plaintext = 0b11010111; // Definește un text clar de 8 biți ca exemplu

        // Afișează textul clar
        System.out.println("Plaintext (Input): " + Integer.toBinaryString(plaintext));

        // Realizează criptarea
        int ciphertext = encrypt(plaintext, key);
        System.out.println("Encrypted (Ciphertext): " + Integer.toBinaryString(ciphertext));

        // Realizează decriptarea
        int decryptedtext = decrypt(ciphertext, key);
        System.out.println("Decrypted (Output): " + Integer.toBinaryString(decryptedtext));
    }
}
