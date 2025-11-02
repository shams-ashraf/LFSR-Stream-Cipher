# LFSR Encryption & Decryption in Java

## Overview
This Java project implements a **stream cipher** using **Linear Feedback Shift Registers (LFSR)**. It allows users to encrypt and decrypt messages bit by bit, using a user-defined polynomial, initial vector, and warm-up bits. The program also analyzes the polynomial to classify it as **primitive**, **irreducible**, or **reducible**.

---

## Features
- Encrypts and decrypts messages using LFSR-based stream cipher.  
- Supports custom polynomial `p(x)` and initial vector.  
- Allows discarding **warm-up bits** to enhance security.  
- Classifies the polynomial type:  
  - **Primitive Polynomial** → generates maximum-length LFSR sequence  
  - **Irreducible Polynomial** → fixed shorter sequence  
  - **Reducible Polynomial** → sequence length depends on initial vector  
- Displays detailed clock-by-clock encryption/decryption table:  
  - LFSR state  
  - Key bit  
  - Message bit  
  - XOR result  
- Outputs message in multiple formats:  
  - Plain text  
  - Binary  

---

## Usage
1. Compile and run the program:
   ```bash
   javac LFSR.java
   java LFSR
Enter the message to encrypt.

Specify the LFSR order m (max 9).

Enter the polynomial in standard form (e.g., x^4 + x + 1).

Enter the initial vector in binary (e.g., 1101).

Enter the number of warm-up bits (bits to discard at the beginning).

The program will display:

Original message in binary

Polynomial classification

Detailed encryption steps (LFSR state, key bits, XOR results)

Encrypted message (text and binary)

Detailed decryption steps

Decrypted message (text and binary)

Example

Enter message (e.g., your name): HELLO

Enter m (max=9): 4

Enter polynomial p(x) (e.g., x^4+x+1): x^4+x+1

Enter initial vector as binary (e.g., 1101): 1011

Enter warm-up bits count (discarded): 4

Original Message (binary): 01001000 01000101 01001100 01001100 01001111 

Polynomial Type → Primitive Polynomial (Maximum-length LFSR)

Warm-up bits (discarded): 1101

Clock | State |Key Bit| Message Bit | XOR Result
----------------------------------------------------------
1     | 1011  | 1     | 0           | 1
2     | 0111  | 1     | 1           | 0
3     | 1110  | 0     | 0           | 0
...
(encryption continues for all bits)

Encrypted Message (text): ÐÕËËÓ

Encrypted Message (binary): 11010000 11010101 11001011 11001011 11010011

Warm-up bits (discarded): 1101

Clock | State |Key Bit| Message Bit | XOR Result
----------------------------------------------------------
1     | 1011  | 1     | 1           | 0
2     | 0111  | 1     | 1           | 0
3     | 1110  | 0     | 0           | 0
...
(decryption continues for all bits)

Decrypted Message (text): HELLO

Decrypted Message (binary): 01001000 01000101 01001100 01001100 01001111

Notes

LFSR order m should not exceed 9 due to practical implementation limits.

Warm-up bits are used to avoid correlation between initial LFSR state and encrypted output.

Polynomial classification helps understand whether the LFSR will produce a maximum-length sequence or not.

The program uses bitwise XOR for encryption/decryption, making it symmetric.
