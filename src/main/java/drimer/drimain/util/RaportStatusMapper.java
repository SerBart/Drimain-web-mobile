package drimer.drimain.util;

import drimer.drimain.model.enums.RaportStatus;

public final class RaportStatusMapper {

    private RaportStatusMapper() {}

    /**
     * Mapuje różne warianty (stare ciągi tekstowe, małe litery, PL) na enum.
     * Zwraca null jeśli nie uda się rozpoznać.
     */
    public static RaportStatus map(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String v = raw.trim().toUpperCase()
                .replace('Ł','L')        // defensywnie
                .replace('Ś','S');

        // Słownik starych nazw -> enum
        switch (v) {
            case "OTWARTY":
            case "NOWY":
            case "NEW":
                return RaportStatus.NOWY;
            case "W TOKU":
            case "WTOKU":
            case "IN_PROGRESS":
            case "W_TOKU":
                return RaportStatus.W_TOKU;
            case "OCZEKUJE":
            case "OCZEKUJE_CZESCI":
            case "OCZEKUJE- CZESCI":
            case "OCZEKUJE NA CZESCI":
            case "WAITING_PARTS":
                return RaportStatus.OCZEKUJE_CZESCI;
            case "ZAKONCZONE":
            case "ZAKONCZONY":
            case "DONE":
            case "CLOSED":
                return RaportStatus.ZAKONCZONE;
            case "ANULOWANE":
            case "ANULOWANY":
            case "CANCELED":
            case "CANCELLED":
                return RaportStatus.ANULOWANE;
            default:
                // jeśli ktoś poda dokładnie nazwę enum
                try {
                    return RaportStatus.valueOf(v);
                } catch (Exception ignored) {
                    return null;
                }
        }
    }
}