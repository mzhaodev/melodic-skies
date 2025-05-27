package dev.mzhao.melodicskies;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class ModConfig {
    /**
     * Enables/disables the terminal solvers
     */
    boolean ENABLE_TERMINALS_HELPER = true;
}
