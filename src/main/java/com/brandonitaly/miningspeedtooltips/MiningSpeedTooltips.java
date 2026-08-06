package com.brandonitaly.miningspeedtooltips;

import com.brandonitaly.miningspeedtooltips.client.MiningSpeedTooltipsConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;

import net.minecraft.world.level.block.Blocks;

import java.util.List;

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
    public static final String MOD_ID = "miningspeedtooltips";

    //? if fabric {
    @Override
    public void onInitialize() {
    }
    //?} else if neoforge {
    /*public MiningSpeedTooltips(IEventBus modEventBus) {
    }
    *///?}

    public static Float getMiningSpeed(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;

        if (stack.is(ItemTags.SWORDS)) {
            return null;
        }

        float baseSpeed = 1.0f;

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
                if (rule.speed().isPresent()) {
                    float ruleSpeed = rule.speed().get();
                    if (ruleSpeed > baseSpeed) {
                        baseSpeed = ruleSpeed;
                    }
                }
            }
        }

        // Fallback for custom tool items overriding getDestroySpeed()
        if (baseSpeed <= 1.0f) {
            if (stack.is(ItemTags.PICKAXES)) {
                baseSpeed = stack.getDestroySpeed(Blocks.STONE.defaultBlockState());
            } else if (stack.is(ItemTags.AXES)) {
                baseSpeed = stack.getDestroySpeed(Blocks.OAK_LOG.defaultBlockState());
            } else if (stack.is(ItemTags.SHOVELS)) {
                baseSpeed = stack.getDestroySpeed(Blocks.DIRT.defaultBlockState());
            } else if (stack.is(ItemTags.HOES)) {
                baseSpeed = stack.getDestroySpeed(Blocks.NETHER_WART_BLOCK.defaultBlockState());
            }
        }

        if (baseSpeed <= 1.0f) return null;
        return baseSpeed;
    }

    public static void addTooltip(ItemStack stack, List<Component> tooltip) {
        if (tooltip == null || !MiningSpeedTooltipsConfig.isEnableMiningSpeedTooltipEnabled()) return;

        Float baseSpeed = getMiningSpeed(stack);
        if (baseSpeed == null) return;

        // Check if tooltip already contains Mining Speed to avoid duplicates
        for (Component c : tooltip) {
            String str = c.getString();
            if (str != null && str.contains("Mining Speed")) return;
        }

        String speedStr = (baseSpeed % 1.0f == 0.0f)
            ? String.valueOf((int) (float) baseSpeed)
            : String.format("%.1f", baseSpeed);

        Component speedText = Component.translatable("miningspeedtooltips.tooltip.mining_speed", speedStr)
            .withStyle(ChatFormatting.DARK_GREEN);

        int attackSpeedIdx = -1;
        int mainhandHeaderIdx = -1;

        String mainHandTrans = Component.translatable("item.modifiers.mainhand").getString();
        String attackSpeedTrans = Component.translatable(Attributes.ATTACK_SPEED.value().getDescriptionId()).getString();

        for (int i = 0; i < tooltip.size(); i++) {
            Component c = tooltip.get(i);
            String str = c.getString();

            if (attackSpeedTrans != null && !attackSpeedTrans.isEmpty() && str.contains(attackSpeedTrans)) {
                attackSpeedIdx = i;
            }
            if (mainHandTrans != null && !mainHandTrans.isEmpty() && str.contains(mainHandTrans)) {
                mainhandHeaderIdx = i;
            }
        }

        if (attackSpeedIdx != -1) {
            tooltip.add(attackSpeedIdx + 1, speedText);
        } else if (mainhandHeaderIdx != -1) {
            tooltip.add(mainhandHeaderIdx + 1, speedText);
        }
    }
}
