package io.github.brandonitaly.miningspeedtooltips.client;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
//?} else if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
*///?}

public class MiningSpeedTooltipsClient /*? if fabric {*/ implements ClientModInitializer /*?}*/ {

    //? if fabric {
    @Override
    public void onInitializeClient() {
    }
    //?}

    //? if neoforge {
    /*public static void init(IEventBus modBus, ModContainer modContainer) {
    }
    *///?}
}
