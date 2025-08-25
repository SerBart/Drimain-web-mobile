package drimer.drimain.api.dto;

import lombok.Data;

@Data
public class PartQuantityPatch {
    private Integer delta; // Change in quantity (positive or negative)
    private Integer value; // Absolute value to set
}