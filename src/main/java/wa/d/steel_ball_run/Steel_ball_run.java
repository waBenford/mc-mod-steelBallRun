package wa.d.steel_ball_run;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityType;

import net.minecraft.sound.SoundEvent;

public class Steel_ball_run implements ModInitializer {
    public static final String MOD_ID = "steel_ball_run";

    public static final RegistryKey<ItemGroup> SRB_ITEM_GROUP = RegistryKey.of(
            RegistryKeys.ITEM_GROUP,
            new Identifier("steel_ball_run","srb_group") //(id mod(ดูที่ fabric.mod.json) & path(ตั้งเองได้))
    );

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

    //soundEffect
    public static final Identifier CHARGE_ID = new Identifier("steel_ball_run", "steel_ball_charge");
    public static final SoundEvent STEEL_BALL_CHARGE = SoundEvent.of(CHARGE_ID);

    public static final Identifier RETURN_ID = new Identifier("steel_ball_run", "steel_ball_return");
    public static final SoundEvent STEEL_BALL_RETURN = SoundEvent.of(RETURN_ID);

    public static final Identifier THROW_ID = new Identifier("steel_ball_run", "steel_ball_throw");
    public static final SoundEvent STEEL_BALL_THROW = SoundEvent.of(THROW_ID);

    @Override
    public void onInitialize() {
        // 2. ลงทะเบียนไอเทมเข้ากับ Registry ของตัวเกม Minecraft
        Registry.register(Registries.ITEM, new Identifier("steel_ball_run", "steel_ball"), STEEL_BALL);

        Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "spin"), SPIN_EFFECT);

        Registry.register(Registries.ITEM_GROUP, SRB_ITEM_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(Steel_ball_run.STEEL_BALL))
                .displayName(Text.translatable("itemGroup.steel_ball_run.sbr_group"))
                .entries((context, entries) -> {
                    entries.add((Steel_ball_run.STEEL_BALL));
                })
                .build()
        );

        Registry.register(Registries.SOUND_EVENT, CHARGE_ID, STEEL_BALL_CHARGE);
        Registry.register(Registries.SOUND_EVENT, RETURN_ID, STEEL_BALL_RETURN);
        Registry.register(Registries.SOUND_EVENT, THROW_ID, STEEL_BALL_THROW);
    }
}