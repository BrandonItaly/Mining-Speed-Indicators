package com.brandonitaly.miningspeedtooltips.client;

import com.brandonitaly.miningspeedtooltips.MiningSpeedTooltips;
import com.brandonitaly.miningspeedtooltips.client.gui.MiningSpeedTooltipsConfigScreen;
//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
//?} else if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
*///?}

public class MiningSpeedTooltipsClient /*? if fabric {*/ implements ClientModInitializer /*?}*/ {

    //? if fabric {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            MiningSpeedTooltips.addTooltip(stack, lines);
        });
    }
    //?}

    //? if neoforge {
    /*public static void init(IEventBus modBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, 
            (container, parent) -> new MiningSpeedTooltipsConfigScreen(parent)
        );
    }
    *///?}
}
