package fr.ensicaen.pi.gpss.backend.data_management.pan;

import fr.ensicaen.pi.gpss.backend.data_management.EncryptedDataPrefix;
import fr.ensicaen.pi.gpss.backend.data_management.annotation.encryption.Pan;
import fr.ensicaen.pi.gpss.backend.tools.security.AesCypher;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

import static fr.ensicaen.pi.gpss.backend.data_management.pan.PrimaryAccountNumberLength.*;

@Validated
public class PrimaryAccountNumber {
    private final AesCypher cypher;
    private PrimaryAccountNumberComponent component;
    private String pan;

    protected PrimaryAccountNumber(@NonNull AesCypher aesCypher, @NotBlank String pan) {
        cypher = aesCypher;
        parsePan(pan);
    }

    private void parsePan(@NotBlank String encryptedOrNotPan) throws IllegalArgumentException {
        if (EncryptedDataPrefix.isEncryptedPan(encryptedOrNotPan)) {
            encryptedOrNotPan = cypher.decrypt(encryptedOrNotPan, EncryptedDataPrefix.PAN.prefix());
        }
        pan = encryptedOrNotPan;

        try {
            String postBin = encryptedOrNotPan.substring(BIN_CARD_SCHEME.length(), BIN.length());
            String binCardScheme = encryptedOrNotPan.substring(0, BIN_CARD_SCHEME.length());
            int checkLuhn = Character.getNumericValue(
                    encryptedOrNotPan.charAt(encryptedOrNotPan.length() - LUHN.length())
            );
            component = new PrimaryAccountNumberComponent(binCardScheme, postBin, checkLuhn);
        } catch (Exception e) {
            throw new IllegalArgumentException("(PAN) could not be parsed");
        }

        if (isPanNotConsistent())
            throw new IllegalArgumentException("(PAN) The given Identification could not be verified");
    }

    @Pan
    public String getPan() throws IllegalArgumentException {
        if (isPanNotConsistent()) {
            throw new IllegalArgumentException("CheckLuhn is not  verified, something went wrong");
        }
        return pan;
    }

    @Pan
    public String getEncryptedPan() throws IllegalArgumentException {
        if (isPanNotConsistent()) {
            throw new IllegalArgumentException("CheckLuhn is not  verified, something went wrong");
        }
        return cypher.encrypt(pan, EncryptedDataPrefix.PAN.prefix());
    }

    public boolean isPanNotConsistent() {
        if (component.getCheckLuhn() == null) {
            return true;
        }
        return pan.length() < MIN_PAN.length() || pan.length() > MAX_PAN.length() ||
                component.getCheckLuhn() != PrimaryAccountNumberGeneration.generateCheckLuhn(
                        pan.substring(0, pan.length() - LUHN.length())
                );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimaryAccountNumber that = (PrimaryAccountNumber) o;
        return Objects.equals(pan, that.pan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pan);
    }
}
