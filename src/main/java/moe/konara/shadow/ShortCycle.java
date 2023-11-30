package moe.konara.shadow;

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
