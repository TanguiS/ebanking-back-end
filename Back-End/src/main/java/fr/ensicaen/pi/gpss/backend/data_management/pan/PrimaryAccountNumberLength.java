package fr.ensicaen.pi.gpss.backend.data_management.pan;

public enum PrimaryAccountNumberLength {
    BIN_CARD_SCHEME(1),
    BIN(6),
    LUHN(1),
    ACCOUNT_IDENTIFIER(9),
    MIN_PAN(14),
    MAX_PAN(19);

    private final int length;

    PrimaryAccountNumberLength(int length) {
        this.length = length;
    }

    public int length() {
        return length;
    }
}
