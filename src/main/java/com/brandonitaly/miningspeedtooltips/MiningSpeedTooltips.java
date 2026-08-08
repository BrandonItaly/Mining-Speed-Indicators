package com.brandonitaly.miningspeedtooltips;

import com.brandonitaly.miningspeedtooltips.compat.InlineTooltipsCompat;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

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

    private static boolean isShiftPressed() {
        try {
            //? if >1.21.8 {
            var window = Minecraft.getInstance().getWindow();
            return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                   InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
            //?} else {
            /*long window = Minecraft.getInstance().getWindow().getWindow();
            return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                   InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT);
            *///?}
        } catch (Throwable ignored) {}
        return false;
    }

    private static boolean matchesTranslationKey(Component component, String keySubstring) {
        if (component == null) return false;
        if (translatableContentsMatch(component, keySubstring)) return true;
        for (Component sibling : component.getSiblings()) {
            if (matchesTranslationKey(sibling, keySubstring)) return true;
        }
        return false;
    }

    private static boolean translatableContentsMatch(Component component, String keySubstring) {
        if (!(component.getContents() instanceof TranslatableContents trans)) return false;
        if (trans.getKey().contains(keySubstring)) return true;
        for (Object arg : trans.getArgs()) {
            if (arg instanceof Component argComp) {
                if (matchesTranslationKey(argComp, keySubstring)) return true;
            } else if (arg != null && arg.toString().contains(keySubstring)) {
                return true;
            }
        }
        return false;
    }

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

    private record Anchors(int attackSpeedIdx, int attackDamageIdx, int mainhandHeaderIdx) {
    }

    private static Anchors findAnchors(List<Component> tooltip) {
        int attackSpeedIdx = -1, attackDamageIdx = -1, mainhandHeaderIdx = -1;
        for (int i = 0; i < tooltip.size(); i++) {
            Component c = tooltip.get(i);
            if (matchesTranslationKey(c, "attack_speed")) attackSpeedIdx = i;
            if (matchesTranslationKey(c, "attack_damage")) attackDamageIdx = i;
            if (matchesTranslationKey(c, "mainhand")) mainhandHeaderIdx = i;
        }
        return new Anchors(attackSpeedIdx, attackDamageIdx, mainhandHeaderIdx);
    }

    /** Inserts {@code toInsert} as a new line right after the best available anchor. */
    private static void insertAfterAnchor(List<Component> tooltip, Anchors anchors, Component toInsert, Runnable noAnchorFallback) {
        if (anchors.attackSpeedIdx() != -1) {
            tooltip.add(anchors.attackSpeedIdx() + 1, toInsert);
        } else if (anchors.attackDamageIdx() != -1) {
            tooltip.add(anchors.attackDamageIdx() + 1, toInsert);
        } else if (anchors.mainhandHeaderIdx() != -1) {
            tooltip.add(anchors.mainhandHeaderIdx() + 1, toInsert);
        } else {
            noAnchorFallback.run();
        }
    }

    private static boolean tooltipAlreadyHasMiningSpeed(List<Component> tooltip) {
        for (Component c : tooltip) {
            String str = c.getString();
            if (str != null && str.contains("Mining Speed")) return true;
        }
        return false;
    }

    public static void addTooltip(ItemStack stack, List<Component> tooltip) {
        Float baseSpeed = getMiningSpeed(stack);
        if (baseSpeed == null || tooltipAlreadyHasMiningSpeed(tooltip)) return;

        String speedStr = (baseSpeed % 1.0f == 0.0f)
            ? String.valueOf((int) (float) baseSpeed)
            : String.format("%.1f", baseSpeed);

        MutableComponent iconComponent = InlineTooltipsCompat.getMiningSpeedIcon();

        if (iconComponent == null) {
            addPlainTextTooltip(tooltip, speedStr);
        } else if (isShiftPressed()) {
            addExpandedTooltip(tooltip, iconComponent, speedStr);
        } else {
            addCompactTooltip(tooltip, iconComponent, speedStr);
        }
    }

    /** Shift held: full "Mining Speed" line, matching Inline Tooltips' expanded format. */
    private static void addExpandedTooltip(List<Component> tooltip, MutableComponent iconComponent, String speedStr) {
        MutableComponent speedLine = iconComponent.copy()
            .append(Component.translatable("miningspeedtooltips.tooltip.mining_speed", speedStr).withStyle(ChatFormatting.DARK_GREEN));

        Anchors anchors = findAnchors(tooltip);
        insertAfterAnchor(tooltip, anchors, speedLine, () -> {
            // Item has no Main Hand section (e.g. Shears) -> create "When in Main Hand:" header
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("item.modifiers.mainhand").withStyle(ChatFormatting.GRAY));
            tooltip.add(speedLine);
        });
    }

    /** Shift not held: compact icon-row format, matching Inline Tooltips' collapsed attribute row. */
    private static void addCompactTooltip(List<Component> tooltip, MutableComponent iconComponent, String speedStr) {
        MutableComponent miningEntry = iconComponent.copy().append(speedStr + " ");

        int iconRowIndex = -1;
        for (int i = 0; i < tooltip.size(); i++) {
            if (InlineTooltipsCompat.containsIconComponent(tooltip.get(i))) {
                iconRowIndex = i;
            }
        }

        if (iconRowIndex != -1) {
            insertIntoIconRow(tooltip, iconRowIndex, miningEntry);
        } else {
            appendToNearestAttributeLine(tooltip, miningEntry);
        }
    }

    /** Inline Tooltips already built a combined icon row — splice our entry into it at the right spot. */
    private static void insertIntoIconRow(List<Component> tooltip, int iconRowIndex, MutableComponent miningEntry) {
        MutableComponent rowComponent = tooltip.get(iconRowIndex).copy();
        List<Component> siblings = rowComponent.getSiblings();

        int insertIndex = findInsertIndexAfter(siblings, InlineTooltipsCompat::isAttackSpeedComponent);
        if (insertIndex == -1) {
            insertIndex = findInsertIndexAfter(siblings, InlineTooltipsCompat::isAttackDamageComponent);
        }
        if (insertIndex == -1) {
            // Fallback: insert before the first non-attribute icon
            for (int i = 0; i < siblings.size(); i++) {
                if (InlineTooltipsCompat.isNonAttributeIcon(siblings.get(i))) {
                    insertIndex = i;
                    break;
                }
            }
        }

        if (insertIndex != -1 && insertIndex <= siblings.size()) {
            siblings.add(insertIndex, miningEntry);
        } else {
            rowComponent.append(miningEntry);
        }

        tooltip.set(iconRowIndex, rowComponent);
    }

    /** Finds the sibling index to insert after a match, skipping past its trailing value if one immediately follows. */
    private static int findInsertIndexAfter(List<Component> siblings, Predicate<Component> matcher) {
        for (int i = 0; i < siblings.size(); i++) {
            if (matcher.test(siblings.get(i))) {
                boolean nextIsNonIcon = i + 1 < siblings.size() && !InlineTooltipsCompat.containsIconComponent(siblings.get(i + 1));
                return nextIsNonIcon ? i + 2 : i + 1;
            }
        }
        return -1;
    }

    /** No combined icon row yet — attach directly to the Attack Speed/Damage/Main Hand line so it stays inline. */
    private static void appendToNearestAttributeLine(List<Component> tooltip, MutableComponent miningEntry) {
        Anchors anchors = findAnchors(tooltip);
        int targetIdx = anchors.attackSpeedIdx() != -1 ? anchors.attackSpeedIdx()
            : anchors.attackDamageIdx() != -1 ? anchors.attackDamageIdx()
            : anchors.mainhandHeaderIdx();

        if (targetIdx != -1) {
            MutableComponent line = tooltip.get(targetIdx).copy().append("  ").append(miningEntry);
            tooltip.set(targetIdx, line);
        } else {
            tooltip.add(miningEntry);
        }
    }

    /** No Inline Tooltips (or no icon available) — plain text line. */
    private static void addPlainTextTooltip(List<Component> tooltip, String speedStr) {
        Component speedText = Component.translatable("miningspeedtooltips.tooltip.mining_speed", speedStr)
            .withStyle(ChatFormatting.DARK_GREEN);

        Anchors anchors = findAnchors(tooltip);
        insertAfterAnchor(tooltip, anchors, speedText, () -> tooltip.add(speedText));
    }
}