package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum TransactionType implements DBEnumerateFormat {
    CREDIT_CARD(0, "CREDIT_CARD", "Transaction is based on a credit card"),
    DEBIT_CARD(1, "DEBIT_CARD", "Transaction is based on a debit card"),
    DEPOSIT(2, "DEPOSIT", "Transaction is based on a deposit"),
    TRANSFER(3, "TRANSFER", "Transaction is based on a transfer"),
    MOBILE_PAYMENT(4, "MOBILE_PAYMENT", "Transaction is based on a mobile payment NFC"),
    CHEQUE(5, "CHEQUE", "Transaction is based on a cheque");

    private final int id;
    private final String name;
    private final String description;

    TransactionType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String label() {
        return name;
    }

    @Override
    public String description() {
        return description;
    }
}
