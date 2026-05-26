package wa.d.steel_ball_run;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityType;

public class Steel_ball_run implements ModInitializer {
    public static final String MOD_ID = "steel_ball_run";


    // 1. ประกาศสร้างไอเทม Steel Ball ขึ้นมาในระบบ
    public static final Item STEEL_BALL = new SteelBallItem(new FabricItemSettings().maxCount(16));

    public static final SpinEffect SPIN_EFFECT = new SpinEffect();

    public static final EntityType<SteelBallEntity> STEEL_BALL_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "steel_ball"),
            net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder.<SteelBallEntity>create(net.minecraft.entity.SpawnGroup.MISC, SteelBallEntity::new)
                    .dimensions(net.minecraft.entity.EntityDimensions.fixed(0.25F, 0.25F)) // ขนาดฮิตบ็อกซ์ตอนลอยบนฟ้า
                    .build()
    );

    @Override
    public void onInitialize() {
        // 2. ลงทะเบียนไอเทมเข้ากับ Registry ของตัวเกม Minecraft
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, "steel_ball"), STEEL_BALL);

        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "spin"), SPIN_EFFECT);

        System.out.println("Steel Ball Run Mod Initialized! Steel Ball is Ready!");
    }
}