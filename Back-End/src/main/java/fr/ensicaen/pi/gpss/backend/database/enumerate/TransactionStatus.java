package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum TransactionStatus implements DBEnumerateFormat {
    PENDING(0, "PENDING", "Transaction is not cleared yet"),
    APPROVED(1, "APPROVED", "Transaction is cleared"),
    REJECTED(2, "REJECTED", "Transaction is refused");

    private final int id;
    private final String name;
    private final String description;

    TransactionStatus(int id, String name, String description) {
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
