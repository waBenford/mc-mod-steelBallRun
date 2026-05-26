package wa.d.steel_ball_run;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class SteelBallItem extends Item {
    public SteelBallItem(Settings settings) {
        super(settings);
    }

    // 1. บอกระบบว่าให้ทำท่าทาง "ชาร์จของ" เวลาขวารับค้างไว้
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW; // หรือ UseAction.SPEAR (ท่าถือหอก Trident)
    }

    // 2. กำหนดเวลาที่สามารถกดค้างได้สูงสุด (เช่น 72000 ติ๊ก)
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    // 3. ฟังก์ชันตอนที่ผู้เล่น "คลิกขวา" เริ่มใช้งาน
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    // 4. ฟังก์ชันสำคัญ: ทำงานตอนที่ผู้เล่น "ปล่อยนิ้ว" จากคลิกขวา
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseDuration) {
        if (user instanceof PlayerEntity player) {
            int chargeDuration = this.getMaxUseTime(stack) - remainingUseDuration;

            // เช็กว่าชาร์จนาพอหรือยัง (เช่น เกิน 10 ติ๊ก หรือประมาณครึ่งวินาที)
            if (chargeDuration >= 10) {
                if (!world.isClient) {
                    // 1. สร้างอ็อบเจกต์ลูกบอลเหล็กในโลกจำลอง
                    SteelBallEntity steelBall = new SteelBallEntity(world, user);

                    // 2. สั่งให้พุ่งออกจากตัวผู้เล่น (อิงตามมุมมองหัว Pitch/Yaw, ความเร็วแรงส่ง 1.5F, ความแม่นยำความส่าย 1.0F)
                    steelBall.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);

                    // 3. เสกเข้าไปในโลกเกมจริง
                    world.spawnEntity(steelBall);
                }

                // หักจำนวนไอเทมออก 1 ชิ้นหลังปา (ถ้าไม่ใช่โหมด Creative)
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
        }
    }

}