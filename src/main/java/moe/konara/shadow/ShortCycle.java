package moe.konara.shadow;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShortCycle extends Thread{
    @Override
    public void run(){
        while(Shadow.CYCLERUNNING) {
            Shadow.time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            try {
                Thread.sleep(1000);
                ServerPlayNetworking.send()
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
