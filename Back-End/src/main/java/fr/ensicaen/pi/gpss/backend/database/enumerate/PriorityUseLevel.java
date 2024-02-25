package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum PriorityUseLevel implements DBEnumerateFormat {
    FIRST(0, "FIRST", "Software priority use : first priority"),
    SECOND(1, "SECOND", "Software priority use : second priority"),
    THIRD(2, "THIRD", "Software priority use : third priority");

    private final int id;
    private final String name;
    private final String description;

    PriorityUseLevel(int id, String name, String description) {
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
