package com.brandonitaly.miningspeedtooltips.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public class MiningSpeedTooltipsConfig {
    //? if fabric {
    private static final Path CONFIG_PATH = net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir().resolve("miningspeedtooltips.json");
    //?} else {
    /*private static final Path CONFIG_PATH = net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get().resolve("miningspeedtooltips.json");
    *///?}

    private static volatile boolean enableMiningSpeedTooltip;

    private record ConfigData(boolean enableMiningSpeedTooltip) {}

    private static final ConfigData DEFAULTS = new ConfigData(true);

    private static final Codec<ConfigData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("enableMiningSpeedTooltip", DEFAULTS.enableMiningSpeedTooltip()).forGetter(ConfigData::enableMiningSpeedTooltip)
    ).apply(instance, ConfigData::new));

    static { load(); }

    public static final OptionInstance<Boolean> ENABLE_TOOLTIP = OptionInstance.createBoolean(
        "miningspeedtooltips.option.enable_tooltip", value -> Tooltip.create(Component.translatable("miningspeedtooltips.option.enable_tooltip.tooltip")),
        isEnableMiningSpeedTooltipEnabled(), MiningSpeedTooltipsConfig::setEnableMiningSpeedTooltip
    );

    // Getters & Setters
    public static boolean isEnableMiningSpeedTooltipEnabled() { return enableMiningSpeedTooltip; }
    public static void setEnableMiningSpeedTooltip(boolean enabled) { if (enableMiningSpeedTooltip != enabled) { enableMiningSpeedTooltip = enabled; save(); } }

    public static OptionInstance<?>[] asOptions() {
        return new OptionInstance<?>[] { ENABLE_TOOLTIP };
    }

    private static void load() {
        ConfigData data = JsonCodecFileStore.read(CONFIG_PATH, CODEC, DEFAULTS, "MiningSpeedTooltipsConfig");
        enableMiningSpeedTooltip = data.enableMiningSpeedTooltip();
    }

    private static void save() {
        JsonCodecFileStore.write(CONFIG_PATH, CODEC, new ConfigData(enableMiningSpeedTooltip), "MiningSpeedTooltipsConfig");
    }

    public static void resetToDefault() {
        ENABLE_TOOLTIP.set(DEFAULTS.enableMiningSpeedTooltip());
        save();
    }
}
