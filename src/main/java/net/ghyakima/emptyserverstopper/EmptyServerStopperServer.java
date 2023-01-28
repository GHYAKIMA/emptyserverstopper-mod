package net.ghyakima.emptyserverstopper;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;

import java.util.Timer;
import java.util.TimerTask;

public class EmptyServerStopperServer implements DedicatedServerModInitializer {
	public static final String MOD_ID = "emptyserverstopper";

	private MinecraftServer server;
	private final Timer timer = new Timer();

	private final boolean m_ShutdownAtStart = true;
	private final int m_ShutdownTimeInMinutes = 15;
	@Override
	public void onInitializeServer() {
		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
		ServerLifecycleEvents.SERVER_STOPPING.register((server1 -> timer.cancel()));

		ServerPlayConnectionEvents.DISCONNECT.register(((handler, server1) -> CheckPlayerNumber()));
	}

	private void onServerStarted(MinecraftServer server) {
		this.server = server;
		if (m_ShutdownAtStart) {
			CheckPlayerNumber();
		}
	}

	private void CheckPlayerNumber() {
		if (this.server.getCurrentPlayerCount() <= 1) {
			System.out.println(String.format("Server empty -> Shutdown in %d minute(s)", m_ShutdownTimeInMinutes));
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					CheckAndStop();
				}
			}, m_ShutdownTimeInMinutes * 60000L);
		}
	}

	private void CheckAndStop() {
		int players = this.server.getCurrentPlayerCount();

		if (players <= 0) {
			System.out.println("Server empty -> Shutting down");
			this.server.stop(true);
		}
		else {
			System.out.println(String.format("Server shutdown cancelled. %d connected player(s)", players));
		}
	}
}
