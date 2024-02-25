package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum CountryCode implements DBEnumerateFormat {
    FR(0, "FR", "Code associated for France"),
    US(1, "US", "Code associated for United States"),
    GB(2, "GB", "Code associated for Great Britten");

    private final int id;
    private final String name;
    private final String description;

    CountryCode(int id, String name, String description) {
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
