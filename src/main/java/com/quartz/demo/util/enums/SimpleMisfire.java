package com.quartz.demo.util.enums;
public enum SimpleMisfire {
    IGNORE_MISFIRES,
    FIRE_NOW,
    NOW_WITH_EXISTING_COUNT,
    NOW_WITH_REMAINING_COUNT,
    NEXT_WITH_REMAINING_COUNT,
    NEXT_WITH_EXISTING_COUNT
}