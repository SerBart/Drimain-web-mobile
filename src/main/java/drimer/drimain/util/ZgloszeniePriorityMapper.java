package drimer.drimain.util;

import drimer.drimain.model.enums.ZgloszeniePriorytet;

public final class ZgloszeniePriorityMapper {
    private ZgloszeniePriorityMapper(){}

    public static ZgloszeniePriorytet map(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String v = raw.trim().toUpperCase();
        switch (v) {
            case "NISKI": case "LOW": return ZgloszeniePriorytet.LOW;
            case "SREDNI": case "ŚREDNI": case "MEDIUM": return ZgloszeniePriorytet.MEDIUM;
            case "WYSOKI": case "HIGH": return ZgloszeniePriorytet.HIGH;
            case "KRYTYCZNY": case "CRITICAL": return ZgloszeniePriorytet.CRITICAL;
            default:
                try { return ZgloszeniePriorytet.valueOf(v); } catch (Exception e) { return null; }
        }
    }
}