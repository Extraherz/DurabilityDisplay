package de.oceanblocks.durabilitydisplay.config;

import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.common.ModConfigSpec;

public class DurabilityConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    // GUI Display Settings
    public static final ModConfigSpec.BooleanValue SHOW_DURABILITY_NUMBERS;
    public static final ModConfigSpec.BooleanValue SHOW_TOOLTIP_INFO;

    // Color Settings (as hex strings)
    public static final ModConfigSpec.ConfigValue<String> COLOR_EXCELLENT; // >75%
    public static final ModConfigSpec.ConfigValue<String> COLOR_GOOD;      // 50-75%
    public static final ModConfigSpec.ConfigValue<String> COLOR_WARN;      // 25-50%
    public static final ModConfigSpec.ConfigValue<String> COLOR_CRITICAL;  // <25%

    static {
        BUILDER.push("General Settings");
        BUILDER.comment("Configure when and how durability information is displayed");

        SHOW_DURABILITY_NUMBERS = BUILDER
                .comment("Show durability numbers above the durability bar",
                        "This displays a compact number (e.g. '1.5k') above damaged items")
                .define("showDurabilityNumbers", true);

        SHOW_TOOLTIP_INFO = BUILDER
                .comment("Show detailed durability information in item tooltips",
                        "Adds a line showing exact durability and percentage when hovering over items")
                .define("showTooltipInfo", true);

        BUILDER.pop();

        BUILDER.push("Color Settings");
        BUILDER.comment("Customize colors for different durability levels",
                "Colors must be in hexadecimal format:",
                "• With 0x prefix: '0xFF5555', '0x55FF55', '0x0099FF'",
                "• With # prefix: '#FF5555', '#55FF55', '#0099FF'",
                "• Without prefix: 'FF5555', '55FF55', '0099FF'",
                "Use online color pickers to find hex values easily!");

        COLOR_EXCELLENT = BUILDER
                .comment("Color for excellent durability (>75%)",
                        "Default: '0x55FF55' (Bright Green)")
                .define("colorExcellent", "0x55FF55");

        COLOR_GOOD = BUILDER
                .comment("Color for good durability (50-75%)",
                        "Default: '0xFFFF55' (Bright Yellow)")
                .define("colorGood", "0xFFFF55");

        COLOR_WARN = BUILDER
                .comment("Color for warning durability (25-50%)",
                        "Default: '0xFFAA00' (Orange)")
                .define("colorWarn", "0xFFAA00");

        COLOR_CRITICAL = BUILDER
                .comment("Color for critical durability (<25%)",
                        "Default: '0xFF5555' (Bright Red)")
                .define("colorCritical", "0xFF5555");

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    /**
     * Parse hex color string to integer
     * Supports formats: "0xFF5555", "#FF5555", "FF5555"
     */
    private static int parseHexColor(String hexColor, int fallback) {
        try {
            String cleanHex = hexColor.trim();

            // Remove common prefixes
            if (cleanHex.startsWith("0x") || cleanHex.startsWith("0X")) {
                cleanHex = cleanHex.substring(2);
            } else if (cleanHex.startsWith("#")) {
                cleanHex = cleanHex.substring(1);
            }

            // Validate hex string (should be 6 characters for RGB)
            if (cleanHex.length() != 6) {
                throw new IllegalArgumentException("Hex color must be 6 characters long");
            }

            // Parse as hex integer
            return Integer.parseInt(cleanHex, 16);

        } catch (Exception e) {
            // Log error and return fallback
            System.err.println("DurabilityMod: Invalid hex color '" + hexColor + "', using fallback color");
            return fallback;
        }
    }

    /**
     * Get color based on durability percentage
     */
    public static int getDurabilityColor(double percentage) {
        if (percentage > 75) {
            return parseHexColor(COLOR_EXCELLENT.get(), 0x55FF55);
        } else if (percentage > 50) {
            return parseHexColor(COLOR_GOOD.get(), 0xFFFF55);
        } else if (percentage > 25) {
            return parseHexColor(COLOR_WARN.get(), 0xFFAA00);
        } else {
            return parseHexColor(COLOR_CRITICAL.get(), 0xFF5555);
        }
    }

    /**
     * Get ChatFormatting for tooltips based on durability percentage
     */
    public static ChatFormatting getTooltipColor(double percentage) {
        // For tooltips, we still use the predefined ChatFormatting colors
        // since they're limited to Minecraft's color palette
        if (percentage > 75) {
            return ChatFormatting.GREEN;
        } else if (percentage > 50) {
            return ChatFormatting.YELLOW;
        } else if (percentage > 25) {
            return ChatFormatting.GOLD;
        } else {
            return ChatFormatting.RED;
        }
    }
}
