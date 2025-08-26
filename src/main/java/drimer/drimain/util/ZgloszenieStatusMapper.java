package drimer.drimain.util;

import drimer.drimain.model.enums.ZgloszenieStatus;

public final class ZgloszenieStatusMapper {
    private ZgloszenieStatusMapper(){}

    public static ZgloszenieStatus map(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String v = raw.trim().toUpperCase();
        switch (v) {
            case "NOWE": return ZgloszenieStatus.NEW;
            case "W_TOKU": case "WTOKU": return ZgloszenieStatus.ACCEPTED;
            case "OCZEKUJE": case "HOLD": return ZgloszenieStatus.NEW; // Map to NEW for now
            case "ZAKONCZONE": case "DONE": return ZgloszenieStatus.CLOSED;
            case "ODRZUCONE": case "REJECTED": return ZgloszenieStatus.REJECTED;
            default:
                try { return ZgloszenieStatus.valueOf(v); } catch (Exception e) { return null; }
        }
    }
}