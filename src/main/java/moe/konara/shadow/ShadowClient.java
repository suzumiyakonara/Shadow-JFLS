package moe.konara.shadow;

import net.fabricmc.api.ClientModInitializer;

public class ShadowClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(){
        Shadow.CYCLERUNNING = false;
    }
}
