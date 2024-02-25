package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum OrderStatus implements DBEnumerateFormat {
    NOT_ORDERED(0, "NOT_ORDERED", "Request to order a card is received, but it is not ordered yet"),
    RECEIVED(2, "RECEIVED", "The card has been received to the Bank."),
    ORDERED(1, "ORDERED", "The card is ordered to the personification facility");

    private final int id;
    private final String name;
    private final String description;

    OrderStatus(int id, String name, String description) {
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
