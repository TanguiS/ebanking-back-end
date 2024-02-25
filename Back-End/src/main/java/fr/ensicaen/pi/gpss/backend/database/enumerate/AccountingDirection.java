package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum AccountingDirection implements DBEnumerateFormat {
    DEBIT(0, "DEBIT", "Transaction is a DEBIT toward the specified PAN or IBAN"),
    CREDIT(1, "CREDIT", "Transaction is a CREDIT toward the specified PAN or IBAN");

    private final int id;
    private final String name;
    private final String description;

    AccountingDirection(int id, String name, String description) {
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
