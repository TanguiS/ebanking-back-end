package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum CardScheme implements DBEnumerateFormat {
    VISA(4, "VISA", "VISA's BIN"),
    CB(0, "CB", "CB card scheme : no bin"),
    MASTERCARD(5, "MASTERCARD", "MASTERCARD's BIN");

    private final int id;
    private final String name;
    private final String description;

    CardScheme(int id, String name, String description) {
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
