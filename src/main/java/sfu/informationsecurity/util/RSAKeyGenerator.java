package sfu.informationsecurity.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeyGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        int keySize = 2048;

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        saveKeyToFile("private_key.pem", privateKey.getEncoded(), "PRIVATE KEY");
        saveKeyToFile("public_key.pem", publicKey.getEncoded(), "PUBLIC KEY");

        System.out.println("🔑 Ключи успешно сгенерированы!");
    }

    private static void saveKeyToFile(String filename, byte[] key, String keyType) throws IOException {
        String pemFormatKey = "-----BEGIN " + keyType + "-----\n" +
                Base64.getMimeEncoder().encodeToString(key) +
                "\n-----END " + keyType + "-----";

        Files.write(Paths.get(filename), pemFormatKey.getBytes());
        System.out.println("✅ " + filename + " создан");
    }
}
