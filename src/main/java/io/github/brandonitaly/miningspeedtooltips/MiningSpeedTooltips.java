package io.github.brandonitaly.miningspeedtooltips;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;

//? if fabric {
import net.fabricmc.api.ModInitializer;
//?} else if neoforge {
/*import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;
*///?}

//? if neoforge {
/*@Mod(MiningSpeedTooltips.MOD_ID)
*///?}
public class MiningSpeedTooltips /*? if fabric {*/ implements ModInitializer /*?}*/ {
    public static final String MOD_ID = "miningspeedindicators";

    private static final List<Map.Entry<TagKey<Item>, BlockState>> DESTROY_SPEED_PROBES = List.of(
        Map.entry(ItemTags.PICKAXES, Blocks.STONE.defaultBlockState()),
        Map.entry(ItemTags.AXES, Blocks.OAK_LOG.defaultBlockState()),
        Map.entry(ItemTags.SHOVELS, Blocks.DIRT.defaultBlockState()),
        Map.entry(ItemTags.HOES, Blocks.NETHER_WART_BLOCK.defaultBlockState())
    );

    //? if fabric {
    @Override
    public void onInitialize() {
    }
    //?} else if neoforge {
    /*public MiningSpeedTooltips(IEventBus modEventBus) {
    }
    *///?}

    public static Float getMiningSpeed(ItemStack stack) {
        if (stack == null || stack.isEmpty() || stack.is(ItemTags.SWORDS)) return null;

        float baseSpeed = 1.0f;

        try {
            Tool tool = stack.get(DataComponents.TOOL);
            if (tool != null) {
                for (Tool.Rule rule : tool.rules()) {
                    if (rule.blocks().unwrapKey().isPresent() &&
                        rule.blocks().unwrapKey().get().location().getPath().contains("sword")) {
                        return null;
                    }
                }

                baseSpeed = tool.defaultMiningSpeed();

                for (Tool.Rule rule : tool.rules()) {
                    if (rule.speed().isPresent() && rule.speed().get() > baseSpeed) {
                        baseSpeed = rule.speed().get();
                    }
                }
            }
        } catch (Throwable ignored) {}

        // Fallback for custom tool items overriding getDestroySpeed()
        if (baseSpeed <= 1.0f) {
            for (var probe : DESTROY_SPEED_PROBES) {
                if (stack.is(probe.getKey())) {
                    baseSpeed = stack.getDestroySpeed(probe.getValue());
                    break;
                }
            }
        }

        return baseSpeed > 1.0f ? baseSpeed : null;
    }

    public static AttributeModifier getCombinedMiningModifier(ItemStack stack, float baseSpeed) {
        AttributeInstance efficiency = new AttributeInstance(Attributes.MINING_EFFICIENCY, ignored -> {});
        stack.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
            if (attribute.equals(Attributes.MINING_EFFICIENCY)) {
                efficiency.addOrUpdateTransientModifier(modifier);
            }
        });
        return new AttributeModifier(
            Identifier.fromNamespaceAndPath(MiningSpeedTooltips.MOD_ID, "combined_mining_efficiency"),
            baseSpeed + efficiency.getValue(), AttributeModifier.Operation.ADD_VALUE);
    }
}
