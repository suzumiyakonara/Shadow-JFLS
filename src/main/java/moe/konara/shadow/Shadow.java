package moe.konara.shadow;

import moe.konara.shadow.sql.ColumnSetting;
import moe.konara.shadow.sql.Sqlite;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shadow implements ModInitializer{
    public static final Logger LOGGER = LoggerFactory.getLogger("Shadow");
    public static boolean CYCLERUNNING = true;
    public static MinecraftServer SERVER;
    public static Sqlite SQL;
    public static Map<String,String> MAPPING;

    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register(Event::onUseBlock);
        PlayerBlockBreakEvents.BEFORE.register(Event::onPlayerBreakBlock);
        ServerLifecycleEvents.SERVER_STARTING.register(this::onInitializeServer);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onStopServer);
        MAPPING=new HashMap<>();
    }

    private void onStopServer(MinecraftServer minecraftServer) {
        Shadow.CYCLERUNNING = false;
    }
    public void onInitializeServer(MinecraftServer minecraftServer) {
        Shadow.SQL = new Sqlite("./config/Shadow.db",LOGGER);
        Shadow.SERVER = minecraftServer;
        Shadow.CYCLERUNNING = true;
        new ShortCycle().start();
        Shadow.SQL.createNewTableIfNotExists("Sign_List", List.of(
                new ColumnSetting("world", ColumnSetting.ColumnType.TEXT),
                new ColumnSetting("x", ColumnSetting.ColumnType.INT),
                new ColumnSetting("y", ColumnSetting.ColumnType.INT),
                new ColumnSetting("z", ColumnSetting.ColumnType.INT)
        ));
    }


}
