package fr.ensicaen.pi.gpss.backend.security.jwt;

public enum TokenFormat {
    BEARER("Token", "Bearer ");

    TokenFormat(String key, String prefix) {
        this.key = key;
        this.prefix = prefix;
    }

    private final String key;
    private final String prefix;

    public String key() {
        return key;
    }

    public String prefix() {
        return prefix;
    }
}
