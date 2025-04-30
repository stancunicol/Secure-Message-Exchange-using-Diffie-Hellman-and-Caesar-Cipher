import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 12345;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("The Server is waiting for connection on port " + port);

        try (Socket clientSocket = serverSocket.accept();
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            BigInteger q = new BigInteger(in.readLine());
            BigInteger a = new BigInteger(in.readLine());
            System.out.println("Received parameters: q=" + q + ", a=" + a);

            BigInteger xServer;
            do {
                xServer = new BigInteger(q.bitLength(), new java.util.Random())
                        .mod(q.subtract(BigInteger.ONE))
                        .add(BigInteger.ONE);
            } while (xServer.equals(BigInteger.ZERO));
            System.out.println("Server private key: " + xServer);

            BigInteger yServer = a.modPow(xServer, q);
            System.out.println("Server public key: " + yServer);

            out.println(yServer.toString());

            BigInteger yClient = new BigInteger(in.readLine());
            System.out.println("Client public key: " + yClient);

            BigInteger k;
            do {
                k = yClient.modPow(xServer, q);
                if (k.equals(BigInteger.ZERO)) {
                    // Regenerare xServer dacă k == 0
                    xServer = new BigInteger(q.bitLength(), new java.util.Random())
                            .mod(q.subtract(BigInteger.ONE))
                            .add(BigInteger.ONE);
                    yServer = a.modPow(xServer, q);
                    out.println(yServer.toString());
                }
            } while (k.equals(BigInteger.ZERO));
            System.out.println("Calculated common key: " + k);

            String encryptedMessage = in.readLine();
            System.out.println("Received encrypted message: " + encryptedMessage);

            String decryptedMessage = decryptCaesar(encryptedMessage, k.intValue());
            System.out.println("Decrypted message: " + decryptedMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String decryptCaesar(String encrypted, int key) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : encrypted.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int original = c - base;
                int shifted = (original - key) % 26;
                if (shifted < 0) shifted += 26;
                decrypted.append((char) (base + shifted));
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }
}