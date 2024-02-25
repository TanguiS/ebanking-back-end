package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum CardType implements DBEnumerateFormat {
    DEBIT_CARD(0, "DEBIT_CARD", "A debit card"),
    CREDIT_CARD(1, "CREDIT_CARD", "A credit Card");

    private final int id;
    private final String name;
    private final String description;

    CardType(int id, String name, String description) {
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
