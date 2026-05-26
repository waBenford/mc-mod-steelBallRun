package wa.d.steel_ball_run.client;

import net.fabricmc.api.ClientModInitializer;
import wa.d.steel_ball_run.Steel_ball_run;

public class Steel_ball_runClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
                Steel_ball_run.STEEL_BALL_ENTITY,
                SteelBallEntityRenderer::new
        );
    }
}
