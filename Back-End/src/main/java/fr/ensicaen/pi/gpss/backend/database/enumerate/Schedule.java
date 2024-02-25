package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum Schedule  implements DBEnumerateFormat {
    NOT_RECURRENT(0, "NOT_RECURRENT", "No scheduling"),
    EVERY_DAY(1, "EVERY_DAY", "Scheduling every day"),
    EVERY_MONTH(2, "EVERY_MONTH", "Scheduling every month"),
    EVERY_YEAR(3, "EVERY_YEAR", "Scheduling every year");

    private final int id;
    private final String name;
    private final String description;

    Schedule(int id, String name, String description) {
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
