package net.ghyakima.emptyserverstopper;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class EmptyServerStopper implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        CommonClass.init();

        ServerLifecycleEvents.SERVER_STARTED.register(CommonClass::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register((server -> CommonClass.timer.cancel()));
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> CommonClass.onPlayerJoin()));
        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> CommonClass.CheckPlayerNumber()));
    }
}
