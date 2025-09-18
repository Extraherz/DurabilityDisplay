package de.oceanblocks.durabilitydisplay;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import de.oceanblocks.durabilitydisplay.config.DurabilityConfig;
import de.oceanblocks.durabilitydisplay.handler.DurabilityTooltipHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(DurabilityDisplay.MODID)
public class DurabilityDisplay {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "durabilitydisplay";

    public static final Logger LOGGER = LogUtils.getLogger();

    public DurabilityDisplay(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register config
        modContainer.registerConfig(ModConfig.Type.CLIENT, DurabilityConfig.SPEC);

        // Register event handlers
        NeoForge.EVENT_BUS.register(new DurabilityTooltipHandler());
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }
}
