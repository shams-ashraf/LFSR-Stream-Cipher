# LFSR-Stream-Cipher

A simple Java implementation of LFSR (Linear Feedback Shift Register) for encrypting and decrypting messages using a stream cipher approach.

## Features

- Encrypt and decrypt messages of any length using LFSR.
- Supports warm-up bits (first X bits are discarded but displayed in console).
- Shows flip-flop states and output bits for each clock cycle.
- Classifies the polynomial used: Primitive, Irreducible, or Reducible.
- Encryption output is kept as raw bytes (may include symbols), not converted to Base64.

## Usage

1. Clone or download the repository:

```bash
git clone https://github.com/shams-ashraf/LFSR-Stream-Cipher.git
cd LFSR-Stream-Cipher

Follow the prompts:

Enter your message.

Enter m (maximum 9) for the register size.

Enter the polynomial as binary (e.g., 11101).

Enter the initial vector as binary (e.g., 1101).

Enter the number of warm-up bits (discarded).

The program will display:

Warm-up bits.

Clock number, flip-flop states, and output bit for each step.

Encrypted message (as raw bytes/string, may include symbols).

Decrypted message (original message).

Example
Enter message (e.g., your name): Hello
Enter m (max=9): 4
Enter polynomial p(x) as binary (e.g., 11101): 11101
Enter initial vector as binary (e.g., 1101): 1101
Enter warm-up bits count (discarded): 3

Polynomial Type → Reducible Polynomial (Length depends on initial vector)

Warm-up bits (discarded): 110

Clock | Flip-Flops State | Output Bit
-------------------------------------
1     | 1010             | 1
2     | 0101             | 0
...
Encrypted Message (as bytes/string, may contain symbols): ¢¢±
Decrypted Message: Hello
