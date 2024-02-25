package fr.ensicaen.pi.gpss.backend.data_management.iban;

public enum BankAccountIdentificationShift {
    MODULUS_CHECK_DIGIT(1),
    MODULUS(97),
    BANK_CODE(89),
    BRANCH_CODE(15),
    ACCOUNT_NUMBER(3);

    private final int shift;

    BankAccountIdentificationShift(int shift) {
        this.shift = shift;
    }

    public int shift() {
        return shift;
    }
}
