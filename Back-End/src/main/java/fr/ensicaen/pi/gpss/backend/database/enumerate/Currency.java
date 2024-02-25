package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum Currency implements DBEnumerateFormat {
    EUR(0, "EUR", "Euro"),
    CNY(1, "CNY", "Yuan"),
    USD(2, "USD", "Dollar"),
    GBP(3, "GBP", "Pound");

    private final int id;
    private final String name;
    private final String description;

    Currency(int id, String name, String description) {
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
