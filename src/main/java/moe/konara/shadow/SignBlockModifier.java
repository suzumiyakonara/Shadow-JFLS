package moe.konara.shadow;

import moe.konara.shadow.sql.DataForm;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Map;
import java.util.Objects;

public class SignBlockModifier extends Thread{
    @Override
    public void run(){
        DataForm form = Shadow.SQL.selectData("Sign_List");
        for(Map<String,String> signInfo:form){
            ServerWorld world = getWorld(signInfo.get("world"));
            BlockPos pos = new BlockPos(Integer.parseInt(signInfo.get("x")),Integer.parseInt(signInfo.get("y")),Integer.parseInt(signInfo.get("z")));
            if(world!=null){
                BlockEntity be = world.getWorldChunk(pos).getBlockEntity(pos, WorldChunk.CreationType.IMMEDIATE);
                if(be!=null) {
                    if (!(be instanceof SignBlockEntity)){Shadow.SQL.deleteIfExists("Sign_List", signInfo);break;}
                    BlockEntityUpdateS2CPacket packet = BlockEntityUpdateS2CPacket.create(be,this::processNbt);
                    if(Objects.requireNonNull(packet.getNbt()).getBoolean("modified"))
                        for (ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
                            player.networkHandler.sendPacket(packet);
                        }
                }
            }
        }
    }

    private NbtCompound processNbt(BlockEntity block) {
        NbtCompound nbt = new NbtCompound();
        boolean flag = false;
        ((SignBlockEntity)block).writeNbt(nbt);
        for(String key:Shadow.MAPPING.keySet()){
            for(int i=1;i<=4;i++){
                String json = nbt.getString("Text"+i);
                if(json.contains(key)){
                    nbt.remove("Text"+i);
                    nbt.putString("Text"+i,json.replaceAll(key,Shadow.MAPPING.get(key)));
                    flag = true;
                }
            }
        }
        nbt.putBoolean("modified",flag);
        return nbt;
    }

    private ServerWorld getWorld(String identifier){
        for(RegistryKey<World> registryKey:Shadow.SERVER.getWorldRegistryKeys()){
            if(registryKey.getValue().toString().equals(identifier)){
                return Shadow.SERVER.getWorld(registryKey);
            }
        }
        return null;
    }
}
