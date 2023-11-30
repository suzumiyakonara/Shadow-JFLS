package moe.konara.shadow;

import moe.konara.shadow.sql.ColumnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SignItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class Event {
    protected static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult){
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
                Shadow.SQL.insertIfNotExists("Sign_List", List.of(
                        new ColumnValue("world",world.getRegistryKey().getValue().toString()),
                        new ColumnValue("x",pos.getX()),
                        new ColumnValue("y",pos.getY()),
                        new ColumnValue("z",pos.getZ())
                ));
            }
        }
        return ActionResult.PASS;
    }

    protected static boolean onPlayerBreakBlock(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
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
