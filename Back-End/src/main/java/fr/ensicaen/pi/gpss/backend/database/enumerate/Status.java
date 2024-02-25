package fr.ensicaen.pi.gpss.backend.database.enumerate;

public enum Status implements DBEnumerateFormat {
    PENDING(0, "PENDING", "User registration is currently under verification - Pending status"),
    AUTHORIZED(1, "AUTHORIZED", "User is accepted and can connect to Back Office resources"),
    UNAUTHORIZED(2, "UNAUTHORIZED", "User is forbidden to access Back Office resources");

    private final int id;
    private final String name;
    private final String description;

    Status(int id, String name, String description) {
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
