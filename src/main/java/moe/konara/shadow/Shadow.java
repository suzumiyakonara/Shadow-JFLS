package moe.konara.shadow;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shadow implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Shadow");
    public static String time="";
    public static boolean CYCLERUNNING = true;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        UseBlockCallback.EVENT.register(this::onUseBlock);
        new ShortCycle().start();
    }

    private ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult){
        if(!world.isClient() && player instanceof ServerPlayerEntity) {
            BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
            if (blockEntity instanceof SignBlockEntity) {
                player.openEditSignScreen(((SignBlockEntity) blockEntity));
            }

        }
        return ActionResult.PASS;
    }
}
