package de.oceanblocks.durabilitydisplay.mixin;

import de.oceanblocks.durabilitydisplay.config.DurabilityConfig;
import de.oceanblocks.durabilitydisplay.utils.DurabilityUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class HideDurabilityBarMixin {

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("HEAD"),
            cancellable = true)
    private void hideDurabilityBar(Font font, ItemStack itemStack, int x, int y, String text, CallbackInfo ci) {
        // Check if we should hide the durability bar
        if (DurabilityConfig.HIDE_DURABILITY_BAR.get() &&
                DurabilityConfig.SHOW_DURABILITY_NUMBERS.get() &&
                itemStack.isDamageableItem() &&
                itemStack.isDamaged()) {

            GuiGraphics guiGraphics = (GuiGraphics) (Object) this;

            // Render only our custom durability number, skip the vanilla bar
            renderCustomDurabilityOnly(guiGraphics, font, itemStack, x, y, text);

            // Cancel the original method to prevent vanilla durability bar rendering
            ci.cancel();
        }
    }

    @Unique
    private void renderCustomDurabilityOnly(GuiGraphics guiGraphics, Font font, ItemStack item, int x, int y, String text) {
        // Render stack count (if any) - we still want this
        if (text != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
            guiGraphics.drawString(font, text, x + 19 - 2 - font.width(text), y + 6 + 3, 16777215, true);
            guiGraphics.pose().popPose();
        }

        int unbreakingLevel = DurabilityUtils.getEnchantmentLevel(item, Enchantments.UNBREAKING);
        int maxDamage = item.getMaxDamage();
        int currentDamage = item.getDamageValue();
        int durabilityLeft = maxDamage - currentDamage;

        // Calculate percentage for color
        double percentage = ((double) durabilityLeft / maxDamage) * 100;

        // Get color from config
        int color = DurabilityConfig.getDurabilityColor(percentage);

        // Format the durability text with compact number formatting
        String string = DurabilityUtils.format(((maxDamage - currentDamage) * (unbreakingLevel + 1)));

        // Push matrix to render on top
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 200.0F); // Bring to front

        // Scale down the font for smaller text (0.5 = half size)
        guiGraphics.pose().scale(0.5F, 0.5F, 1.0F);

        // Calculate position for smaller text - better centering
        int stringWidth = font.width(string);
        int xScaled = ((x + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
        int yScaled = (y + 12) * 2;

        // Render text with shadow for better visibility
        guiGraphics.drawString(font, string, xScaled, yScaled, color, true);

        // Pop matrix to restore original state
        guiGraphics.pose().popPose();
    }
}
