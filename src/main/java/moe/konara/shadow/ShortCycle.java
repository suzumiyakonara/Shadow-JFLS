package moe.konara.shadow;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SignBlock;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShortCycle extends Thread{
    @Override
    public void run(){
        try {
        while(Shadow.CYCLERUNNING) {
            new SignBlockModifier().start();
            Shadow.MAPPING.remove("time");
            Shadow.MAPPING.put("time",new SimpleDateFormat("HH:mm:ss").format(new Date()));
            Thread.sleep(1000);
        }
        } catch (Exception e) {
            Shadow.LOGGER.error(e.getMessage());
            Shadow.LOGGER.error("Error caused in shot cycle thread.");
        }
    }
}
