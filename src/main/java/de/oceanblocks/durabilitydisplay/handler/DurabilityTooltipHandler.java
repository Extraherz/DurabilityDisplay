package de.oceanblocks.durabilitydisplay.handler;

import de.oceanblocks.durabilitydisplay.config.DurabilityConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class DurabilityTooltipHandler {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        // Check if tooltip info is enabled
        if (!DurabilityConfig.SHOW_TOOLTIP_INFO.get()) {
            return;
        }

        ItemStack itemStack = event.getItemStack();

        // Check if item has durability
        if (itemStack.isDamageableItem()) {
            int maxDamage = itemStack.getMaxDamage();
            int currentDamage = itemStack.getDamageValue();
            int durabilityLeft = maxDamage - currentDamage;

            // Calculate percentage
            double percentage = ((double) durabilityLeft / maxDamage) * 100;

            // Get color from config
            ChatFormatting color = DurabilityConfig.getTooltipColor(percentage);

            // Create tooltip text
            Component durabilityText = Component.literal(
                    String.format("Durability: %d/%d (%.1f%%)",
                            durabilityLeft, maxDamage, percentage)
            ).withStyle(color);

            // Add to tooltip (insert at position 1 to appear near the top)
            if (event.getToolTip().size() > 1) {
                event.getToolTip().add(1, durabilityText);
            } else {
                event.getToolTip().add(durabilityText);
            }
        }
    }
}
