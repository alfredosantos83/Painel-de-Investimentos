import at.favre.lib.crypto.bcrypt.BCrypt;

public class GeneratePassword {
    public static void main(String[] args) {
        String password = "password123";
        String hash = BCrypt.withDefaults().hashToString(10, password.toCharArray());
        System.out.println("Password: " + password);
        System.out.println("Hash: " + hash);
        
        // Verificar
        boolean matches = BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
        System.out.println("Matches: " + matches);
    }
}
