package wa.d.steel_ball_run;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final String MOD_ID = "steel_ball_run"; // ปรับ MOD_ID ให้ตรงกับมอดของวานะครับ

    // สร้างบล็อก Spin Table (ก็อปปี้คุณสมบัติความแข็งแรงมาจาก Smithing Table)
    public static final Block SPIN_TABLE = registerBlock("spin_table",
            new Block(AbstractBlock.Settings.copy(Blocks.SMITHING_TABLE)));

    // เมธอดช่วยลงทะเบียนทั้งตัวบล็อก และไอเทมบล็อกไปพร้อมกัน
    private static Block registerBlock(String name, Block block) {
        Identifier id = Identifier.of(MOD_ID, name);

        // ลงทะเบียน BlockItem เพื่อให้สามารถกดเสกมาวางในโหมด Creative ได้ (แต่ไม่มีสูตรคราฟต์ใน Survival)
        Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));

        return Registry.register(Registries.BLOCK, id, block);
    }

    public static void registerModBlocks() {
        // เมธอดว่างเอาไว้เรียกใน Main Initializer เพื่อเปิดตัวคลาสนี้
    }
}