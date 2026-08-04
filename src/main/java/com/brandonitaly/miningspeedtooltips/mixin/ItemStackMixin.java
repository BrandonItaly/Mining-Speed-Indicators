package com.brandonitaly.miningspeedtooltips.mixin;

import com.brandonitaly.miningspeedtooltips.MiningSpeedTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltipLines", at = @At("RETURN"), cancellable = true)
    private void miningspeedtooltips$addMiningSpeedTooltip(
        Item.TooltipContext context,
        Player player,
        TooltipFlag flag,
        CallbackInfoReturnable<List<Component>> cir
    ) {
        List<Component> originalLines = cir.getReturnValue();
        if (originalLines == null) return;

        ItemStack stack = (ItemStack) (Object) this;
        if (stack.isEmpty()) return;

        List<Component> tooltip = new ArrayList<>(originalLines);
        MiningSpeedTooltips.addTooltip(stack, tooltip);
        cir.setReturnValue(tooltip);
    }
}
