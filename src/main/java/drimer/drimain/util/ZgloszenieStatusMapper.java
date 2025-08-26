package drimer.drimain.util;

import drimer.drimain.model.enums.ZgloszenieStatus;

/**
 * Mapper for ZgloszenieStatus enum to handle legacy and new status values.
 */
public final class ZgloszenieStatusMapper {
    private ZgloszenieStatusMapper(){}

    public static ZgloszenieStatus map(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String v = raw.trim().toUpperCase();
        switch (v) {
            case "NOWE": case "OPEN": return ZgloszenieStatus.NEW;
            case "W_TOKU": case "WTOKU": case "IN_PROGRESS": case "ACCEPTED": return ZgloszenieStatus.ACCEPTED;
            case "OCZEKUJE": case "HOLD": case "ON_HOLD": case "REJECTED": return ZgloszenieStatus.REJECTED;
            case "ZAKONCZONE": case "DONE": case "CLOSED": return ZgloszenieStatus.CLOSED;
            case "ODRZUCONE": return ZgloszenieStatus.REJECTED;
            default:
                try { return ZgloszenieStatus.valueOf(v); } catch (Exception e) { return null; }
        }
    }
    
    public static String toString(ZgloszenieStatus status) {
        return status != null ? status.name() : "NEW";
    }
}