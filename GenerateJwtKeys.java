import java.io.FileWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class GenerateJwtKeys {
    public static void main(String[] args) throws Exception {
        // Gerar par de chaves RSA 2048 bits
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        // Salvar chave pública em formato PEM
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(publicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----\n";
        
        FileWriter publicWriter = new FileWriter("src/main/resources/META-INF/resources/publicKey.pem");
        publicWriter.write(publicKeyPEM);
        publicWriter.close();
        
        // Salvar chave privada em formato PEM
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(privateKey.getEncoded()) +
                "\n-----END PRIVATE KEY-----\n";
        
        FileWriter privateWriter = new FileWriter("src/main/resources/privateKey.pem");
        privateWriter.write(privateKeyPEM);
        privateWriter.close();
        
        System.out.println("✅ Chaves RSA geradas com sucesso!");
        System.out.println("📄 Chave pública: src/main/resources/META-INF/resources/publicKey.pem");
        System.out.println("🔐 Chave privada: src/main/resources/privateKey.pem");
    }
}
