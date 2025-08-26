package drimer.drimain.model.enums;

public enum ZgloszenieStatus {
    // Old values (kept for backward compatibility)
    OPEN,
    IN_PROGRESS,
    ON_HOLD,
    DONE,
    REJECTED,
    
    // New values (as per requirements)
    NEW,
    ACCEPTED,
    CLOSED
}