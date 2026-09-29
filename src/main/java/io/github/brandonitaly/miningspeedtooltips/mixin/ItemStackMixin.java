package io.github.brandonitaly.miningspeedtooltips.mixin;

import io.github.brandonitaly.miningspeedtooltips.MiningSpeedTooltips;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//? if >1.21.8 {
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriConsumer;
//?} else {
/*import java.util.function.BiConsumer;
*///?}

@Mixin(ItemStack.class)
public class ItemStackMixin {
    // Replace only the modifiers passed to tooltip rendering, without changing the stack or gameplay attributes.
    //? if >1.21.8 {
    @Redirect(method = "addAttributeTooltips", at = @At(value = "INVOKE", target =
        "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V"))
    private void miningspeedtooltips$combinedEfficiency(ItemStack stack, EquipmentSlotGroup group,
        TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> consumer) {
        Float baseSpeed = MiningSpeedTooltips.getMiningSpeed(stack);
        if (baseSpeed == null || !group.test(EquipmentSlot.MAINHAND)) {
            stack.forEachModifier(group, consumer);
            return;
        }
        stack.forEachModifier(group, (attribute, modifier, display) -> {
            if (!attribute.equals(Attributes.MINING_EFFICIENCY)) {
                consumer.accept(attribute, modifier, display);
            }
        });
        if (group == EquipmentSlotGroup.MAINHAND) {
            consumer.accept(Attributes.MINING_EFFICIENCY, MiningSpeedTooltips.getCombinedMiningModifier(stack, baseSpeed),
                ItemAttributeModifiers.Display.attributeModifiers());
        }
    }
    //?} else {
    /*@Redirect(method = "addAttributeTooltips", at = @At(value = "INVOKE", target =
        "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V"))
    private void miningspeedtooltips$combinedEfficiency(ItemStack stack, EquipmentSlotGroup group,
        BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        Float baseSpeed = MiningSpeedTooltips.getMiningSpeed(stack);
        if (baseSpeed == null || !group.test(EquipmentSlot.MAINHAND)) {
            stack.forEachModifier(group, consumer);
            return;
        }
        stack.forEachModifier(group, (attribute, modifier) -> {
            if (!attribute.equals(Attributes.MINING_EFFICIENCY)) {
                consumer.accept(attribute, modifier);
            }
        });
        if (group == EquipmentSlotGroup.MAINHAND) {
            consumer.accept(Attributes.MINING_EFFICIENCY, MiningSpeedTooltips.getCombinedMiningModifier(stack, baseSpeed));
        }
    }
    *///?}

}
