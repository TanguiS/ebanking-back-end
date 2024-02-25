package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum AuthorizationType implements DBEnumerateFormat {
    OFF_LINE(0, "OFF_LINE", "Type of card Authorization is Off-Line"),
    ON_LINE(1, "ON_LINE", "Type of card Authorization is On-Line");

    private final int id;
    private final String name;
    private final String description;

    AuthorizationType(int id, String name, String description){
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
