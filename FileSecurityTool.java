import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.util.Scanner;

public class FileSecurityTool {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("----------------------------------------------");
        System.out.println("         File Encryption and Decryption");
        System.out.println("----------------------------------------------");

        boolean exit = false;
        while (!exit) {
            System.out.println("\nAvailable Commands:");
            System.out.println("1. encrypt   - Encrypt a file");
            System.out.println("2. decrypt   - Decrypt an encrypted file");
            System.out.println("3. exit      - Exit the program");
            System.out.print("\nEnter a command: ");

            String command = scanner.nextLine();

            switch (command) {
                case "encrypt":
                    System.out.print("Enter the path of the file to encrypt: ");
                    String filePath = scanner.nextLine();
                    System.out.print("Enter the encryption key: ");
                    String encryptionKey = scanner.nextLine();

                    try {
                        encryptFile(filePath, encryptionKey);
                        System.out.println("Encryption successful.");
                    } catch (Exception e) {
                        System.out.println("Encryption failed. " + e.getMessage());
                    }
                    break;

                case "decrypt":
                    System.out.print("Enter the path of the encrypted file: ");
                    String encryptedFilePath = scanner.nextLine();
                    System.out.print("Enter the decryption key: ");
                    String decryptionKey = scanner.nextLine();

                    try {
                        decryptFile(encryptedFilePath, decryptionKey);
                        System.out.println("Decryption successful.");
                    } catch (Exception e) {
                        System.out.println("Decryption failed. " + e.getMessage());
                    }
                    break;

                case "exit":
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }

        System.out.println("\nExiting the program...");
        scanner.close();
    }

    private static void encryptFile(String filePath, String encryptionKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, IOException {

        File inputFile = new File(filePath);
        File encryptedFile = new File(inputFile.getAbsolutePath() + ".encrypted");

        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(encryptedFile);
        if (!encryptedFile.exists()) {
            encryptedFile.createNewFile();
        }
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
    }

    private static void decryptFile(String encryptedFilePath, String decryptionKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, IOException {

        File encryptedFile = new File(encryptedFilePath);
        File decryptedFile = new File(encryptedFile.getParent(), "decrypted_" + encryptedFile.getName());

        SecretKeySpec secretKey = new SecretKeySpec(decryptionKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        FileInputStream inputStream = new FileInputStream(encryptedFile);
        byte[] inputBytes = new byte[(int) encryptedFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(decryptedFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
    }
}