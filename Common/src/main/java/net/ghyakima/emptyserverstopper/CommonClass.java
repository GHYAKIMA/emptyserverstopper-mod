package net.ghyakima.emptyserverstopper;

import net.ghyakima.emptyserverstopper.platform.Services;
import net.minecraft.server.MinecraftServer;

import java.util.Timer;
import java.util.TimerTask;

public class CommonClass {

    private static final int m_ShutdownTimeInMinutes = 15;
    private static final boolean m_ShutdownAtStart = true;
    private static MinecraftServer server;
    public static Timer timer;

    public static void init() {
        if (Services.PLATFORM.isModLoaded("emptyserverstopper")) {
            Constants.LOG.info("Ready!");
        }
    }

    public static void onServerStarted(MinecraftServer server) {
        CommonClass.server = server;
        if (m_ShutdownAtStart) {
            CheckPlayerNumber();
        }
    }

    public static void onPlayerJoin() {
        if (server.getPlayerCount() <= 1 & timer != null) {
            Constants.LOG.info("Server shutdown cancelled. Player has joined the server");
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public static void CheckPlayerNumber() {
        if (server.getPlayerCount() <= 1) {
            Constants.LOG.info(String.format("Server empty -> Shutdown in %d minute(s)", m_ShutdownTimeInMinutes));

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    CheckAndStop();
                }
            };

            timer = new Timer();
            timer.schedule(task, m_ShutdownTimeInMinutes * 60000L);
        }
    }

    private static void CheckAndStop() {
        int players = server.getPlayerCount();

        if (players <= 0) {
            Constants.LOG.info("Server empty -> Shutting down");
            server.halt(true);
        }
        else {
            Constants.LOG.info(String.format("Server shutdown cancelled. %d connected player(s)", players));
        }
    }
}