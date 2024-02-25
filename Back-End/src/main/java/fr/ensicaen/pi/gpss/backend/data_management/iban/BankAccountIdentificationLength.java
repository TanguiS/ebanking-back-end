package fr.ensicaen.pi.gpss.backend.data_management.iban;

public enum BankAccountIdentificationLength {
    BANK_CODE(5),
    BRANCH_CODE(5),
    COUNTRY_CODE(2),
    CHECK_DIGIT(2),
    CHECK_BANK_REFERENCE_DIGIT(2),
    ACCOUNT_NUMBER(11);

    private final int length;

    BankAccountIdentificationLength(int length) {
        this.length = length;
    }

    public int length() {
        return length;
    }
}
