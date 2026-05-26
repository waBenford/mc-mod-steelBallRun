package wa.d.steel_ball_run;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SpinEffect extends StatusEffect {

    // กำหนดให้เป็นเอฟเฟกต์ประเภทส่งผลเสีย (HARMFUL) และใช้สีเขียวสปิน
    public SpinEffect() {
        super(net.minecraft.entity.effect.StatusEffectCategory.HARMFUL, 3329330);
    }

    // สั่งให้ระบบทำงานทุกๆ ติ๊กในเกม (ไม่ต้องรอดีเลย์)
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    // บรรทัดทำงานหลัก: ลอจิกสั่งมอนสเตอร์หมุนติ้ว
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // เช็กว่าเป็น Mob ตัวเล็กตามที่วากำหนด (เช่น สูงไม่เกิน 1.5 บล็อก หรือปรับตามชอบ)
        if (entity.getHeight() <= 3.5F) {
            // ดึงค่าการหันหน้าปัจจุบัน แล้วบวกเพิ่มเข้าไปรอบละ 30 องศา เพื่อให้หมุนรัวๆ
            float currentYaw = entity.getYaw();
            float newYaw = currentYaw + 30.0F;

            // สั่งอัปเดตมุมหันหน้าของตัว Mob ทั้งตัวและหัวให้หมุนตามกัน
            entity.setYaw(newYaw);
            entity.setHeadYaw(newYaw);
            entity.setBodyYaw(newYaw);

            // 2. ล็อกให้อยู่กับที่ขยับไม่ได้ (Zero X and Z Velocity)
            // ดึงค่าความเร็วปัจจุบันมา แล้วเซฟค่าแกน X กับ Z ให้เป็น 0 (เหลือ Y ไว้ให้ตกลงพื้นตามแรงโน้มถ่วงปกติ)
            net.minecraft.util.math.Vec3d currentVelocity = entity.getVelocity();
            entity.setVelocity(0, currentVelocity.y, 0);

            // 3. บล็อกแรงเดินจากสมอง AI ไม่ให้เขียนทับฟิสิกส์
            entity.forwardSpeed = 0.0F;
            entity.sidewaysSpeed = 0.0F;

            // 4. สั่งให้ระบบเดินเรือ (Navigation) ของม็อบหยุดคิดหาทางเดินในติ๊กนี้
            if (entity instanceof net.minecraft.entity.mob.MobEntity mob) {
                mob.getNavigation().stop();
            }
            // สั่งอัปเดตส่งข้อมูลฟิสิกส์ใหม่ไปบอกตัวเกมฝั่ง Client ด้วย ขาจะได้ไม่ twitch
            entity.velocityModified = true;
        }
    }
}