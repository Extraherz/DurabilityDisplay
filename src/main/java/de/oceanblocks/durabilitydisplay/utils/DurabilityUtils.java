package de.oceanblocks.durabilitydisplay.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.text.DecimalFormat;

public class DurabilityUtils {

    /**
     * Format numbers in a compact way
     * @param number
     * @return Formatted decimal as String
     */
    public static String format(float number) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");

        if (number >= 1000000000) return decimalFormat.format(number / 1000000000) + "b";
        if (number >= 1000000) return decimalFormat.format(number / 1000000) + "m";
        if (number >= 1000) return decimalFormat.format(number / 1000) + "k";

        return Float.toString(number).replaceAll("\\.?0*$", "");
    }

    /**
     * Get enchantment level
     * @param item
     * @param enchantment
     * @return Enchantment Level as an Integer of given enchantment
     */
    public static int getEnchantmentLevel(ItemStack item, ResourceKey<Enchantment> enchantment) {
        return item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getKey().compareTo(enchantment) == 0)
                .findAny()
                .map(entry -> entry.getIntValue())
                .orElse(0);
    }

}
