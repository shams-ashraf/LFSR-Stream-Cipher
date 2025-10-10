import java.util.*;

public class LFSR_Assignment {

    static int getParity(int num) {
        int parity = 0;
        while (num > 0) {
            parity ^= (num & 1);
            num >>= 1;
        }
        return parity;
    }

    static int getSequenceLength(int m, int poly, int initialVector) {
        int register = initialVector;
        int max = (1 << m) - 1;
        Set<Integer> seen = new HashSet<>();
        while (!seen.contains(register)) {
            seen.add(register);
            int feedbackBit = getParity(register & poly);
            register = ((register << 1) | feedbackBit) & max;
        }
        return seen.size();
    }

    static String classifyPolynomial(int m, int poly) {
        int maxLen = (1 << m) - 1;
        Set<Integer> lengths = new HashSet<>();
        for (int initialVector = 1; initialVector < (1 << m); initialVector++) {
            int len = getSequenceLength(m, poly, initialVector);
            lengths.add(len);
        }
        if (lengths.size() == 1) {
            int seqLen = lengths.iterator().next();
            if (seqLen == maxLen)
                return "Primitive Polynomial (Maximum-length LFSR)";
            else
                return "Irreducible Polynomial (Fixed shorter length)";
        } else {
            return "Reducible Polynomial (Length depends on initial vector)";
        }
    }

    static int nextBit(int[] register, int poly, int m) {
        int outputBit = (register[0] >> (m - 1)) & 1;
        int feedbackBit = getParity(register[0] & poly);
        register[0] = ((register[0] << 1) | feedbackBit) & ((1 << m) - 1);
        return outputBit;
    }

    static byte[] xorWithLFSR(byte[] messageBytes, int m, int poly, int initialVector, int warmupBits, boolean showTable) {
        int[] register = new int[]{initialVector};
        byte[] result = new byte[messageBytes.length];

        System.out.print("\nWarm-up bits (discarded): ");
        for (int i = 0; i < warmupBits; i++) {
            int warmBit = nextBit(register, poly, m);
            System.out.print(warmBit);
        }
        System.out.println("\n");

        if (showTable) {
            System.out.println("Clock | Flip-Flops State | Output Bit");
            System.out.println("-------------------------------------");
        }

        int clock = 1;
        for (int i = 0; i < messageBytes.length; i++) {
            byte currentByte = messageBytes[i];
            byte newByte = 0;
            for (int j = 0; j < 8; j++) {
                int keyBit = nextBit(register, poly, m);
                int messageBit = (currentByte >> (7 - j)) & 1;
                int encryptedBit = messageBit ^ keyBit;
                newByte = (byte) ((newByte << 1) | encryptedBit);

                if (showTable) {
                    String state = String.format("%" + m + "s", Integer.toBinaryString(register[0] | (1 << m)).substring(1)).replace(' ', '0');
                    System.out.printf("%5d | %s | %d\n", clock, state, keyBit);
                }
                clock++;
            }
            result[i] = newByte;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter message (e.g., your name): ");
        String message = input.nextLine();

        System.out.print("Enter m (max=9): ");
        int m = input.nextInt();

        System.out.print("Enter polynomial p(x) as binary (e.g., 11101): ");
        String polynomialBinary = input.next();

        System.out.print("Enter initial vector as binary (e.g., 1101): ");
        String initialVectorBinary = input.next();

        System.out.print("Enter warm-up bits count (discarded): ");
        int warmupBits = input.nextInt();

        int polynomial = Integer.parseInt(polynomialBinary, 2);
        int initialVector = Integer.parseInt(initialVectorBinary, 2);

        String type = classifyPolynomial(m, polynomial);
        System.out.println("\nPolynomial Type → " + type);

        byte[] encrypted = xorWithLFSR(message.getBytes(), m, polynomial, initialVector, warmupBits, true);
        System.out.println("\nEncrypted Message (as bytes/string, may contain symbols): " + new String(encrypted) + "\n");

        System.out.println("=== Decryption Phase ===");
        byte[] decrypted = xorWithLFSR(encrypted, m, polynomial, initialVector, warmupBits, true);
        System.out.println("\nDecrypted Message: " + new String(decrypted));
    }
}
