package com.alphadevelopmentsolutions.frcscout.Interfaces;

public interface StatsKeys
{
    String MIN = "MIN";
    String MAX = "MAX";
    String AVG = "AVG";

    String AUTO_EXIT_HAB = "AUTO_EXIT_HAB";
    String AUTO_HATCH = "AUTO_HATCH";
    String AUTO_HATCH_DROPPED = "AUTO_HATCH_DROPPED";
    String AUTO_CARGO = "AUTO_CARGO";
    String AUTO_CARGO_DROPPED = "AUTO_CARGO_DROPPED";
    String TELEOP_HATCH = "TELEOP_HATCH";
    String TELEOP_HATCH_DROPPED = "TELEOP_HATCH_DROPPED";
    String TELEOP_CARGO = "TELEOP_CARGO";
    String TELEOP_CARGO_DROPPED = "TELEOP_CARGO_DROPPED";
    String RETURNED_HAB = "RETURNED_HAB";
    String RETURNED_HAB_FAILS = "RETURNED_HAB_FAILS";
    String DEFENSE_RATING = "DEFENSE_RATING";
    String OFFENSE_RATING = "OFFENSE_RATING";
    String DRIVE_RATING = "DRIVE_RATING";

    String[] STATS_CATEGORY_ORDER_KEYS =
            {
                    MIN,
                    AVG,
                    MAX
            };

    String[] STATS_ORDER_KEYS =
            {
                    AUTO_EXIT_HAB,
                    AUTO_HATCH,
                    AUTO_HATCH_DROPPED,
                    AUTO_CARGO,
                    AUTO_CARGO_DROPPED,
                    TELEOP_HATCH,
                    TELEOP_HATCH_DROPPED,
                    TELEOP_CARGO,
                    TELEOP_CARGO_DROPPED,
                    RETURNED_HAB,
                    RETURNED_HAB_FAILS,
                    DEFENSE_RATING,
                    OFFENSE_RATING,
                    DRIVE_RATING
            };
}
