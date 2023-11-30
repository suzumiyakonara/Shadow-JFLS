package moe.konara.shadow;

import moe.konara.shadow.sql.ColumnSetting;
import moe.konara.shadow.sql.ColumnValue;
import moe.konara.shadow.sql.Sqlite;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SignItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shadow implements ModInitializer, ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Shadow");
    public static boolean CYCLERUNNING = true;
    public static MinecraftServer SERVER;
    public static Sqlite SQL;
    public static Map<String,String> MAPPING;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        UseBlockCallback.EVENT.register(this::onUseBlock);
        PlayerBlockBreakEvents.BEFORE.register(this::onPlayerBreakBlock);
        ServerLifecycleEvents.SERVER_STARTING.register(this::onInitializeServer);
        MAPPING=new HashMap<>();
    }

    @Override
    public void onInitializeClient(){
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

    private ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult){
        if(!world.isClient()) {
            BlockPos pos = null;
            if(world.getBlockEntity(hitResult.getBlockPos()) instanceof SignBlockEntity){
                pos = hitResult.getBlockPos();
            }
            if(player.getStackInHand(hand).getItem() instanceof SignItem){
                BlockEntity be = world.getBlockEntity(hitResult.getBlockPos());
                if(be==null) {
                    BlockPos tmp = hitResult.getBlockPos().add(hitResult.getSide().getVector());
                    if(world.canPlace(Blocks.OAK_SIGN.getDefaultState(),tmp, ShapeContext.of(player)))
                        pos = tmp;
                }else {
                    if(player.isSneaking()){
                        BlockPos tmp = hitResult.getBlockPos().add(hitResult.getSide().getVector());
                        if(world.canPlace(Blocks.OAK_SIGN.getDefaultState(),tmp, ShapeContext.of(player)))
                            pos = tmp;
                    }
                }
            }

            if(pos!=null){
                Shadow.SQL.insertIfNotExists("Sign_List",List.of(
                        new ColumnValue("world",world.getRegistryKey().getValue().toString()),
                        new ColumnValue("x",pos.getX()),
                        new ColumnValue("y",pos.getY()),
                        new ColumnValue("z",pos.getZ())
                ));
            }
        }
        return ActionResult.PASS;
    }

    private boolean onPlayerBreakBlock(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if(blockEntity instanceof SignBlockEntity){
            Shadow.SQL.deleteIfExists("Sign_List",List.of(
                    new ColumnValue("world",world.getRegistryKey().getValue().toString()),
                    new ColumnValue("x", blockPos.getX()),
                    new ColumnValue("y", blockPos.getY()),
                    new ColumnValue("z", blockPos.getZ())
            ));
        }
        return true;
    }
}
