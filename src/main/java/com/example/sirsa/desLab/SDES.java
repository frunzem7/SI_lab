package com.example.sirsa.desLab;

public class SDES {
    public static int[] IP = {1, 5, 2, 0, 3, 7, 4, 6}; // Initial Permutation
    public static int[] IPI = {3, 0, 2, 4, 6, 1, 7, 5}; // Inverse Initial Permutation
    public static int[] EP = {3, 0, 1, 2, 1, 2, 3, 0}; // Expansion Permutation
    public static int[] P10 = {2, 4, 1, 6, 3, 9, 0, 8, 7, 5}; // Permutation 10
    public static int[] P8 = {5, 2, 6, 3, 7, 4, 9, 8}; // Permutation 8
    public static int[] P4 = {1, 3, 2, 0}; // Permutation 4

    // S-boxes
    public static int[][] S0 = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 2}
    };

    public static int[][] S1 = {
            {0, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}
    };

    // Utility method for permutation
    public static int permute(int[] sequence, int number, int bitSize) {
        int result = 0;
        for (int i = 0; i < sequence.length; i++) {
            result |= ((number >> bitSize - 1 - sequence[i]) & 1) << bitSize - 1 - i;
        }
        return result;
    }

    // Key generation method
    public static int[] keyGeneration(int key) {
        int permutedKey = permute(P10, key, 10);
        int left = (permutedKey >> 5) & 0x1F;
        int right = permutedKey & 0x1F;

        // Left shifts
        left = ((left << 1) & 0x1F) | (left >> 4);
        right = ((right << 1) & 0x1F) | (right >> 4);

        int firstKey = permute(P8, (left << 5) | right, 10);

        // Second left shifts
        left = ((left << 2) & 0x1F) | (left >> 3);
        right = ((right << 2) & 0x1F) | (right >> 3);

        int secondKey = permute(P8, (left << 5) | right, 10);

        return new int[]{firstKey, secondKey};
    }

    // Function f used in the Feistel structure
    public static int fk(int input, int key) {
        int left = input >> 4;
        int right = input & 0xF;
        return (left ^ f(right, key)) << 4 | right;
    }

    // Function f implementation
    public static int f(int input, int key) {
        input = permute(EP, input, 4);
        input ^= key;

        int left = (input >> 4) & 0xF;
        int right = input & 0xF;

        int row, col;
        row = ((left & 1) << 1) | (left >> 3);
        col = ((left >> 1) & 0x3);
        int s0Val = S0[row][col];

        row = ((right & 1) << 1) | (right >> 3);
        col = ((right >> 1) & 0x3);
        int s1Val = S1[row][col];

        return permute(P4, (s0Val << 2) | s1Val, 4);
    }

    // Encryption method
    public static int encrypt(int input, int key) {
        int[] keys = keyGeneration(key);
        input = permute(IP, input, 8);

        // First fk with first key
        input = fk(input, keys[0]);

        // Swap left and right
        input = ((input & 0xF) << 4) | (input >> 4);

        // Second fk with second key
        input = fk(input, keys[1]);

        return permute(IPI, input, 8); // Inverse IP
    }

    // Decryption method
    public static int decrypt(int input, int key) {
        int[] keys = keyGeneration(key);
        input = permute(IP, input, 8);

        // First fk with second key
        input = fk(input, keys[1]);

        // Swap left and right
        input = ((input & 0xF) << 4) | (input >> 4);

        // Second fk with first key
        input = fk(input, keys[0]);

        return permute(IPI, input, 8); // Inverse IP
    }

    public static void main(String[] args) {
        int key = 0b1010000010; // Example 10-bit key
        int plaintext = 0b11010111; // Example 8-bit plaintext

        // Display the plaintext
        System.out.println("Plaintext (Input): " + Integer.toBinaryString(plaintext));

        // Perform encryption
        int ciphertext = encrypt(plaintext, key);
        System.out.println("Encrypted (Ciphertext): " + Integer.toBinaryString(ciphertext));

        // Perform decryption
        int decryptedtext = decrypt(ciphertext, key);
        System.out.println("Decrypted (Output): " + Integer.toBinaryString(decryptedtext));
    }
}
