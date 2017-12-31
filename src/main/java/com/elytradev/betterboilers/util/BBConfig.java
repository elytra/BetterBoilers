package com.elytradev.betterboilers.util;

import net.minecraftforge.common.config.Configuration;

public class BBConfig {

    private static Configuration config;

    public static int ticksToBoil;
    public static int steamPerBoil;

    public static int defaultMaxMultiblock;
    public static int defaultMinMultiblock;

    private static final ConfigKey<Integer> TICKS_TO_BOIL = ConfigKey.create(
            "BoilerUsage", "ticksToBoil", 200,

            "The amount of ticks needed for one boiler cycle.");
    private static final ConfigKey<Integer> STEAM_PER_BOIL = ConfigKey.create(
            "BoilerUsage", "steamPerBoil", 50,

            "The amount of steam produced per boiler cycle.",
            "Water cost will always be 2x the resulting steam.");


    private static final ConfigKey<Integer> DEFAULT_MAX_MULTIBLOCK = ConfigKey.create(
            "BoilerMultiblock", "defaultMaxMultiblock", 1000,

            "The maximumamount of blocks that can be added to a standard boiler multiblock.",
            "Some controllers may have different maxima.",
            "Includes fireboxes, boiler blocks, and all components.");
    private static final ConfigKey<Integer> DEFAULT_MIN_MULTIBLOCK = ConfigKey.create(
            "BoilerMultiblock", "defaultMinMultiblock", 36,

            "The minimum amount of blocks that can be added to a standard boiler multiblock.",
            "Some controllers may have different minima, or no minima at all.",
            "Includes fireboxes, boiler blocks, and all components.");
    public static void setConfig(Configuration config) {
        BBConfig.config = config;
    }

    public static void load() {
        config.load();

        ticksToBoil = TICKS_TO_BOIL.get(config);
        steamPerBoil = STEAM_PER_BOIL.get(config);

        defaultMaxMultiblock = DEFAULT_MAX_MULTIBLOCK.get(config);
        defaultMinMultiblock = DEFAULT_MIN_MULTIBLOCK.get(config);
    }

    public static void save() {
        TICKS_TO_BOIL.set(config, ticksToBoil);
        STEAM_PER_BOIL.set(config, steamPerBoil);

        DEFAULT_MAX_MULTIBLOCK.set(config, defaultMaxMultiblock);
        DEFAULT_MIN_MULTIBLOCK.set(config, defaultMinMultiblock);

        config.save();
    }
}
