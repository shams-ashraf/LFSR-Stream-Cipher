import java.util.*;

public class LFSR-Stream-Cipher {

    static int computeFeedback(List<Integer> register, List<Integer> poly) {
        int feedback = 0;
        for (int i = 0; i < register.size(); i++) {
            if (poly.get(i) == 1) {
                feedback = (feedback + register.get(i)) % 2;
            }
        }
        return feedback;
    }

    static int nextBit(List<Integer> register, List<Integer> poly) {
        int output = register.get(0);
        int feedback = computeFeedback(register, poly);
        register.remove(0);
        register.add(feedback);
        return output;
    }

    static byte[] xorLFSR(byte[] messageBytes, List<Integer> poly, List<Integer> initialVector, int warmupBits, boolean showTable) {
        List<Integer> register = new ArrayList<>(initialVector);
        Collections.reverse(register);

        byte[] result = new byte[messageBytes.length];

        System.out.print("\nWarm-up bits (discarded): ");
        for (int i = 0; i < warmupBits; i++) {
            int warmBit = nextBit(register, poly);
            System.out.print(warmBit);
        }
        System.out.println("\n");

        if (showTable) {
            System.out.println("Clock | State |Key Bit| Message Bit | XOR Result");
            System.out.println("----------------------------------------------------------");
        }

        int clock = 1;
        for (int i = 0; i < messageBytes.length; i++) {
            byte currentByte = messageBytes[i];
            byte newByte = 0;
            for (int j = 7; j >= 0; j--) {
                int messageBit = (currentByte >> j) & 1;
                int keyBit = nextBit(register, poly);
                int encryptedBit = (messageBit + keyBit) % 2;
                newByte = (byte) ((newByte << 1) + encryptedBit);

                if (showTable) {
                    String stateStr = register.toString().replaceAll("[\\[\\], ]", "");
                    System.out.printf("%5d | %s | %7d | %11d | %10d\n", clock, stateStr, keyBit, messageBit, encryptedBit);
                }
                clock++;
            }
            result[i] = newByte;
        }
        return result;
    }

    static List<Integer> parsePolynomial(String polyStr, int m) {
        List<Integer> poly = new ArrayList<>(Collections.nCopies(m, 0));
        String[] terms = polyStr.replace(" ", "").toLowerCase().split("\\+");
        for (String term : terms) {
            int power;
            if (term.equals("1")) power = 0;
            else if (term.equals("x")) power = 1;
            else if (term.startsWith("x^")) power = Integer.parseInt(term.substring(2));
            else continue;
            if (power < m) poly.set(power , 1);
        }
        return poly;
    }

    static List<Integer> parseBinaryVector(String binaryStr, int m) {
        List<Integer> vector = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            vector.add(binaryStr.charAt(i) - '0');
        }
        return vector;
    }

    static int getSequenceLength(List<Integer> poly, List<Integer> initialVector) {
        List<Integer> register = new ArrayList<>(initialVector);
        Collections.reverse(register);

        Set<List<Integer>> seen = new HashSet<>();
        while (!seen.contains(register)) {
            seen.add(new ArrayList<>(register));
            nextBit(register, poly);
        }
        return seen.size();
    }

    static String classifyPolynomial(List<Integer> poly, int m) {
        int maxLength = (int) Math.pow(2, m) - 1;
        int firstLength = -1;
        boolean reducible = false;

for (int start = 1; start <= maxLength; start++) {
            List<Integer> initialVector = new ArrayList<>(Collections.nCopies(m, 0));
            for (int i = 0; i < m; i++) {
                if (((start >> (m - 1 - i)) & 1) == 1) initialVector.set(i, 1);
            }
            int length = getSequenceLength(poly, initialVector);
            if (firstLength == -1) firstLength = length;
            else if (length != firstLength) {
                reducible = true;
                break;
            }
        }

        if (reducible) return "Reducible Polynomial (length depends on initial vector)";
        else if (firstLength == maxLength) return "Primitive Polynomial (Maximum-length LFSR)";
         else return "Irreducible Polynomial (Fixed shorter length)";
    }


    static String toBinaryString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) sb.append((b >> i) & 1);
            sb.append(' ');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("\nEnter message (e.g., your name): ");
        String message = input.nextLine();

        System.out.print("Enter m (max=9): ");
        int m = input.nextInt();
        input.nextLine();

        System.out.print("Enter polynomial p(x) (e.g., x^4+x+1): ");
        String polynomialStr = input.nextLine();

        System.out.print("Enter initial vector as binary (e.g., 1101): ");
        String initialVectorStr = input.nextLine();

        System.out.print("Enter warm-up bits count (discarded): ");
        int warmupBits = input.nextInt();

        List<Integer> poly = parsePolynomial(polynomialStr, m);
        List<Integer> initialVector = parseBinaryVector(initialVectorStr, m);

        System.out.print("\nOriginal Message (binary): ");
        System.out.println(toBinaryString(message.getBytes()));

        String type = classifyPolynomial(poly, m);
        System.out.println("\nPolynomial Type → " + type);

        byte[] encrypted = xorLFSR(message.getBytes(), poly, initialVector, warmupBits, true);

        System.out.println("\nEncrypted Message (text): " + new String(encrypted));
        System.out.print("Encrypted Message (binary): ");
        System.out.println(toBinaryString(encrypted));

        byte[] decrypted = xorLFSR(encrypted, poly, initialVector, warmupBits, true);

        System.out.println("\nDecrypted Message (text): " + new String(decrypted));
        System.out.print("Decrypted Message (binary): ");
        System.out.println(toBinaryString(decrypted));
    }
}
