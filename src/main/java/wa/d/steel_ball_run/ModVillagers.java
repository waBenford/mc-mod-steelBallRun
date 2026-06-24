package wa.d.steel_ball_run;

import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagers {
    public static final String MOD_ID = "steel_ball_run";

    // 1. ลงทะเบียน POI (บอกเกมว่า โต๊ะ SPIN_TABLE คือบล็อกทำงานของอาชีพนี้)
    public static final RegistryKey<PointOfInterestType> SPIN_TABLE_POI_KEY = RegistryKey.of(
            RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(MOD_ID, "spin_table_poi"));

    public static final PointOfInterestType SPIN_TABLE_POI = PointOfInterestHelper.register(
            new Identifier(MOD_ID, "spin_table_poi"), 1, 1, ModBlocks.SPIN_TABLE);

    // 2. สร้างอาชีพ Spin Master และผูกเข้ากับโต๊ะทำงานข้างบน
    public static final VillagerProfession SPIN_MASTER = Registry.register(Registries.VILLAGER_PROFESSION,
            new Identifier(MOD_ID, "spin_master"),
            VillagerProfessionBuilder.create()
                    .id(new Identifier(MOD_ID, "spin_master"))
                    .workstation(SPIN_TABLE_POI_KEY)
                    .workSound(SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH) // เสียงเคาะตอนทำงาน
                    .build());

    public static void registerVillagers() {
        // เมธอดว่างสำหรับเรียกโหลดระบบอาชีพในคลาสหลัก
    }
}