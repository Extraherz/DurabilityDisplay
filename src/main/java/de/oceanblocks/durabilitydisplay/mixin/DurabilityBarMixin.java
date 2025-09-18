package de.oceanblocks.durabilitydisplay.mixin;

import de.oceanblocks.durabilitydisplay.config.DurabilityConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;

@Mixin(GuiGraphics.class)
public class DurabilityBarMixin {

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("TAIL"))

    private void renderDurabilityText(Font font, ItemStack item, int x, int y, String text, CallbackInfo ci) {
        // Check if durability numbers are enabled
        if (!DurabilityConfig.SHOW_DURABILITY_NUMBERS.get()) {
            return;
        }

        GuiGraphics guiGraphics = (GuiGraphics) (Object) this;

        // Check if item has durability and is damaged
        if (item.isDamageableItem() && item.isDamaged()) {
            int unbreakingLevel = durabilityDisplay$getEnchantmentLevel(item, Enchantments.UNBREAKING);
            int maxDamage = item.getMaxDamage();
            int currentDamage = item.getDamageValue();
            int durabilityLeft = maxDamage - currentDamage;

            // Calculate percentage for color
            double percentage = ((double) durabilityLeft / maxDamage) * 100;

            // Get color from config
            int color = DurabilityConfig.getDurabilityColor(percentage);

            // Format the durability text with compact number formatting
            String string = durabilityDisplay$format(((maxDamage - currentDamage) * (unbreakingLevel + 1)));

            // Push matrix to render on top
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F); // Bring to front

            // Scale down the font for smaller text (0.5 = half size)
            guiGraphics.pose().scale(0.5F, 0.5F, 1.0F);

            // Calculate position for smaller text - better centering
            int stringWidth = font.width(string);
            int xScaled = ((x + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
            int yScaled = (y * 2) + 18;

            // Render text with shadow for better visibility
            guiGraphics.drawString(font, string, xScaled, yScaled, color, true);

            // Pop matrix to restore original state
            guiGraphics.pose().popPose();
        }
    }

    @Unique
    public String durabilityDisplay$format(float number) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");

        if (number >= 1000000000) return decimalFormat.format(number / 1000000000) + "b";
        if (number >= 1000000) return decimalFormat.format(number / 1000000) + "m";
        if (number >= 1000) return decimalFormat.format(number / 1000) + "k";

        return Float.toString(number).replaceAll("\\.?0*$", "");
    }

    @Unique
    private static int durabilityDisplay$getEnchantmentLevel(ItemStack item, ResourceKey<Enchantment> enchantment) {
        return item.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getKey().compareTo(enchantment) == 0)
                .findAny()
                .map(entry -> entry.getIntValue())
                .orElse(0);
    }

}
