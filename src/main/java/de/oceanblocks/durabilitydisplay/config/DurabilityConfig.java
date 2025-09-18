package de.oceanblocks.durabilitydisplay.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class DurabilityConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    // GUI Display Settings
    public static final ModConfigSpec.BooleanValue SHOW_DURABILITY_NUMBERS;
    public static final ModConfigSpec.BooleanValue SHOW_TOOLTIP_INFO;

    // Color Settings (as hex integers)
    public static final ModConfigSpec.IntValue COLOR_EXCELLENT; // >75%
    public static final ModConfigSpec.IntValue COLOR_GOOD;      // 50-75%
    public static final ModConfigSpec.IntValue COLOR_WARN;      // 25-50%
    public static final ModConfigSpec.IntValue COLOR_CRITICAL;  // <25%

    static {
        BUILDER.push("General Settings");

        SHOW_DURABILITY_NUMBERS = BUILDER
                .comment("Show durability numbers above the durability bar")
                .define("showDurabilityNumbers", true);

        SHOW_TOOLTIP_INFO = BUILDER
                .comment("Show detailed durability information in item tooltips")
                .define("showTooltipInfo", true);

        BUILDER.pop();

        BUILDER.push("Color Settings");
        BUILDER.comment("Colors are in hexadecimal format (0xRRGGBB)");

        COLOR_EXCELLENT = BUILDER
                .comment("Color for excellent durability (>75%)")
                .defineInRange("colorExcellent", 0x55FF55, 0x000000, 0xFFFFFF);

        COLOR_GOOD = BUILDER
                .comment("Color for good durability (50-75%)")
                .defineInRange("colorGood", 0xFFFF55, 0x000000, 0xFFFFFF);

        COLOR_WARN = BUILDER
                .comment("Color for warning durability (25-50%)")
                .defineInRange("colorWarn", 0xFFAA00, 0x000000, 0xFFFFFF);

        COLOR_CRITICAL = BUILDER
                .comment("Color for critical durability (<25%)")
                .defineInRange("colorCritical", 0xFF5555, 0x000000, 0xFFFFFF);

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    /**
     * Get color based on durability percentage
     */
    public static int getDurabilityColor(double percentage) {
        if (percentage > 75) {
            return COLOR_EXCELLENT.get();
        } else if (percentage > 50) {
            return COLOR_GOOD.get();
        } else if (percentage > 25) {
            return COLOR_WARN.get();
        } else {
            return COLOR_CRITICAL.get();
        }
    }

    /**
     * Get ChatFormatting for tooltips based on durability percentage
     */
    public static net.minecraft.ChatFormatting getTooltipColor(double percentage) {
        int color = getDurabilityColor(percentage);

        // Convert to closest ChatFormatting
        if (color == COLOR_EXCELLENT.get()) {
            return net.minecraft.ChatFormatting.GREEN;
        } else if (color == COLOR_GOOD.get()) {
            return net.minecraft.ChatFormatting.YELLOW;
        } else if (color == COLOR_WARN.get()) {
            return net.minecraft.ChatFormatting.GOLD;
        } else {
            return net.minecraft.ChatFormatting.RED;
        }
    }

}
