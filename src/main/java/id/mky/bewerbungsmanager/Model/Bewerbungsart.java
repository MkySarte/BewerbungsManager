package id.mky.bewerbungsmanager.Model;

public enum Bewerbungsart
{
    ONLINE,
    EMAIL,
    PERSÖNLICH,
    POST;

    public static Bewerbungsart fromString(String value) {
        return switch (value.toLowerCase()) {
            case "online"      -> ONLINE;
            case "e-mail", "email" -> EMAIL;
            case "persönlich"  -> PERSÖNLICH;
            case "post"        -> POST;
            default -> throw new IllegalArgumentException("Unbekannte Bewerbungsart: " + value);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ONLINE     -> "Online";
            case EMAIL      -> "E-Mail";
            case PERSÖNLICH -> "Persönlich";
            case POST       -> "Post";
        };
    }
}
