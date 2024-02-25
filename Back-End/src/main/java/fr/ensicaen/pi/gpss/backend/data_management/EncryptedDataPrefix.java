package fr.ensicaen.pi.gpss.backend.data_management;

import jakarta.validation.constraints.NotBlank;

public enum EncryptedDataPrefix {
    IBAN("IBAN "),
    RIB("RIB "),
    PAN("PAN ");

    private final String name;

    EncryptedDataPrefix(String name) {
        this.name = name;
    }

    public String prefix() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static boolean isEncryptedIban(@NotBlank String text) {
        return text.startsWith(EncryptedDataPrefix.IBAN.prefix());
    }

    public static boolean isEncryptedPan(@NotBlank String text) {
        return text.startsWith(EncryptedDataPrefix.PAN.prefix());
    }
}
