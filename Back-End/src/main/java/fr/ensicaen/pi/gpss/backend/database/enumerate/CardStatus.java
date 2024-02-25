package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum CardStatus implements DBEnumerateFormat {
    BLOCKED(0, "BLOCKED", "The card is Blocked"),
    DISABLE(1, "DISABLE", "The card has not been activated yet, card id Disabled"),
    ACTIVATED(2, "ACTIVATED", "Card is Activated");

    private final int id;
    private final String name;
    private final String description;

    CardStatus(int id, String name, String description) {
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
