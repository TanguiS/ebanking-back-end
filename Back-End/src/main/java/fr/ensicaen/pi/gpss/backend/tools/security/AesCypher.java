package fr.ensicaen.pi.gpss.backend.tools.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AesCypher {
    private final TextEncryptor cypher;

    public AesCypher(@NotBlank String password, @NotBlank String salt) {
        cypher = Encryptors.text(password, salt);
    }

    public String encrypt(@NotBlank String plaintext, @NotBlank String nonCypheredPrefix) {
        String encryptedText = cypher.encrypt(plaintext);
        return nonCypheredPrefix + encryptedText;
    }

    public String encrypt(@NotBlank String plaintext) {
        return encrypt(plaintext, "");
    }

    public String decrypt(@NotBlank String encryptedMessage, @NotBlank String nonCypheredPrefix) {
        String ciphertext = StringUtils.delete(encryptedMessage, nonCypheredPrefix);
        return cypher.decrypt(ciphertext);
    }

    public String decrypt(@NotBlank String encryptedMessage) {
        return decrypt(encryptedMessage, "");
    }

    public List<String> decryptAll(@NotEmpty List<String> encryptedMessages, @NotBlank String nonCypheredPrefix) {
        List<String> decrypted = new ArrayList<>();
        for (String encryptedMessage : encryptedMessages) {
            decrypted.add(decrypt(encryptedMessage, nonCypheredPrefix));
        }
        return decrypted;
    }

    public List<String> decryptAll(@NotEmpty List<String> encryptedMessages) {
        return decryptAll(encryptedMessages, "");
    }
}
