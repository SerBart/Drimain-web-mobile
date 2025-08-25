package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class PartQuantityPatch {
    private Integer delta;  // For relative changes
    private Integer value;  // For absolute value (oneOf with delta)
}