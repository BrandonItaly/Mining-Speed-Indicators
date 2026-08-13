package com.brandonitaly.miningspeedtooltips.compat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

//? if >26 {
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
//?} else if >1.21.8 {
/*import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
*///?} else {
/*import net.minecraft.resources.ResourceLocation;
import com.samsthenerd.inline.utils.TextureSprite;
import com.samsthenerd.inline.api.data.SpriteInlineData;
import com.samsthenerd.inline.impl.InlineStyle;
*///?}

public class InlineTooltipsCompat {

    public static boolean isLoaded() {
        try {
            //? if fabric {
            return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("inline")
                || net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("inline_tooltips");
            //?} else if neoforge {
            /*return net.neoforged.fml.ModList.get().isLoaded("inline")
                || net.neoforged.fml.ModList.get().isLoaded("inline_tooltips");
            *///?}
        } catch (Throwable ignored) {
            return false;
        }
    }

    //? if >26 {
    public static MutableComponent getMiningSpeedIcon() {
        if (!isLoaded()) return null;
        try {
            Identifier iconLocation = Identifier.fromNamespaceAndPath("minecraft", "inline_tooltip_icons/block_break_speed");
            return Component.object(new AtlasSprite(AtlasSprite.DEFAULT_ATLAS, iconLocation));
        } catch (Throwable ignored) {}
        return null;
    }
    //?} else if >1.21.8 {
    /*public static MutableComponent getMiningSpeedIcon() {
        if (!isLoaded()) return null;
        try {
            ResourceLocation iconLocation = ResourceLocation.fromNamespaceAndPath("minecraft", "inline_tooltip_icons/block_break_speed");
            return Component.object(new AtlasSprite(AtlasSprite.DEFAULT_ATLAS, iconLocation));
        } catch (Throwable ignored) {}
        return null;
    }
    *///?} else {
    /*public static MutableComponent getMiningSpeedIcon() {
        if (!isLoaded()) return null;
        try {
            return InlineHolder.getMiningSpeedIcon();
        } catch (Throwable ignored) {}
        return null;
    }
    *///?}

    public static boolean containsIconComponent(Component component) {
        if (component == null || !isLoaded()) return false;
        try {
            String contentsStr = component.toString();
            if (contentsStr.contains("AtlasSprite") || contentsStr.contains("inline_tooltip_icons")) {
                return true;
            }
            //? if <=1.21.8 {
            /*if (InlineHolder.hasInlineData(component)) {
                return true;
            }
            *///?}
        } catch (Throwable ignored) {}
        for (Component sibling : component.getSiblings()) {
            if (containsIconComponent(sibling)) return true;
        }
        return false;
    }

    public static boolean isAttackSpeedComponent(Component component) {
        if (component == null || !isLoaded()) return false;
        try {
            String str = component.toString();
            if (str.contains("attack_speed")) {
                return true;
            }
            //? if <=1.21.8 {
            /*if (InlineHolder.isAttackSpeed(component)) {
                return true;
            }
            *///?}
        } catch (Throwable ignored) {}
        for (Component sibling : component.getSiblings()) {
            if (isAttackSpeedComponent(sibling)) return true;
        }
        return false;
    }

    public static boolean isAttackDamageComponent(Component component) {
        if (component == null || !isLoaded()) return false;
        try {
            String str = component.toString();
            if (str.contains("attack_damage")) {
                return true;
            }
            //? if <=1.21.8 {
            /*if (InlineHolder.isAttackDamage(component)) {
                return true;
            }
            *///?}
        } catch (Throwable ignored) {}
        for (Component sibling : component.getSiblings()) {
            if (isAttackDamageComponent(sibling)) return true;
        }
        return false;
    }

    public static boolean isNonAttributeIcon(Component component) {
        if (component == null || !isLoaded()) return false;
        try {
            String contentsStr = component.toString();
            if (contentsStr.contains("inline_tooltips") || contentsStr.contains("fuel") || contentsStr.contains("food") || contentsStr.contains("bees") || contentsStr.contains("honey") || contentsStr.contains("light") || contentsStr.contains("saturation")) {
                return true;
            }
            //? if <=1.21.8 {
            /*if (InlineHolder.isNonAttributeIcon(component)) {
                return true;
            }
            *///?}
        } catch (Throwable ignored) {}
        for (Component sibling : component.getSiblings()) {
            if (isNonAttributeIcon(sibling)) return true;
        }
        return false;
    }

    //? if <=1.21.8 {
    /*private static class InlineHolder {
        public static MutableComponent getMiningSpeedIcon() {
            try {
                ResourceLocation icon = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/inline_tooltip_icons/block_break_speed.png");
                TextureSprite textureSprite = new TextureSprite(icon);
                SpriteInlineData spriteInlineData = new SpriteInlineData(textureSprite);
                net.minecraft.network.chat.Style style = InlineStyle.fromInlineData(spriteInlineData);
                return Component.empty().append(Component.literal(".").setStyle(style));
            } catch (Throwable ignored) {}
            return null;
        }

        public static boolean hasInlineData(Component component) {
            try {
                if (component.getStyle() instanceof InlineStyle inlineStyle) {
                    return inlineStyle.getInlineData() != null;
                }
            } catch (Throwable ignored) {}
            return false;
        }

        public static boolean isAttackSpeed(Component component) {
            try {
                if (component.getStyle() instanceof InlineStyle inlineStyle) {
                    Object data = inlineStyle.getInlineData();
                    if (data != null && data.toString().contains("attack_speed")) return true;
                }
            } catch (Throwable ignored) {}
            return false;
        }

        public static boolean isAttackDamage(Component component) {
            try {
                if (component.getStyle() instanceof InlineStyle inlineStyle) {
                    Object data = inlineStyle.getInlineData();
                    if (data != null && data.toString().contains("attack_damage")) return true;
                }
            } catch (Throwable ignored) {}
            return false;
        }

        public static boolean isNonAttributeIcon(Component component) {
            try {
                if (component.getStyle() instanceof InlineStyle inlineStyle) {
                    Object data = inlineStyle.getInlineData();
                    if (data != null) {
                        String dataStr = data.toString();
                        if (dataStr.contains("inline_tooltips") || dataStr.contains("fuel") || dataStr.contains("food") || dataStr.contains("bees") || dataStr.contains("honey") || dataStr.contains("light") || dataStr.contains("saturation")) {
                            return true;
                        }
                    }
                }
            } catch (Throwable ignored) {}
            return false;
        }
    }
    *///?}
}

