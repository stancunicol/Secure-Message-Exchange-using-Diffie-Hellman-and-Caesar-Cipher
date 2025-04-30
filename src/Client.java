import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String hostName = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostName, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Enter q (prime number): ");
            BigInteger q = new BigInteger(scanner.nextLine());
            System.out.print("Enter a (primitive root): ");
            BigInteger a = new BigInteger(scanner.nextLine());

            out.println(q.toString());
            out.println(a.toString());

            BigInteger xClient;
            do {
                xClient = new BigInteger(q.bitLength(), new java.util.Random())
                        .mod(q.subtract(BigInteger.ONE))
                        .add(BigInteger.ONE);
            } while (xClient.equals(BigInteger.ZERO));
            System.out.println("Client private key: " + xClient);

            BigInteger yClient = a.modPow(xClient, q);
            System.out.println("Client public key: " + yClient);

            BigInteger yServer = new BigInteger(in.readLine());
            System.out.println("Server public key: " + yServer);

            out.println(yClient.toString());

            BigInteger k;
            do {
                k = yServer.modPow(xClient, q);
                if (k.equals(BigInteger.ZERO)) {
                    // Regenerare xClient dacă k == 0
                    xClient = new BigInteger(q.bitLength(), new java.util.Random())
                            .mod(q.subtract(BigInteger.ONE))
                            .add(BigInteger.ONE);
                    yClient = a.modPow(xClient, q);
                    out.println(yClient.toString());
                }
            } while (k.equals(BigInteger.ZERO));
            System.out.println("Calculated common key: " + k);

            System.out.print("Enter the message: ");
            String message = scanner.nextLine();

            String encryptedMessage = encryptCaesar(message, k.intValue());
            System.out.println("Encrypted message: " + encryptedMessage);

            out.println(encryptedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encryptCaesar(String message, int key) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int original = c - base;
                int shifted = (original + key) % 26;
                encrypted.append((char) (base + shifted));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }
}