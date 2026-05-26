package wa.d.steel_ball_run;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SteelBallEntity extends ThrownItemEntity {

    // 1. สร้างตัวแปรควบคุมสถานะบินกลับ
    private boolean isReturning = false;

    public SteelBallEntity(EntityType<SteelBallEntity> entityType, World world) {
        super(entityType, world);
    }

    public SteelBallEntity(World world, LivingEntity owner) {
        super(Steel_ball_run.STEEL_BALL_ENTITY, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Steel_ball_run.STEEL_BALL;
    }

    // 2. เขียนฟังก์ชัน tick() เพิ่มเข้ามาเพื่อคำนวณทิศทางบินกลับหาเจ้าของทุกๆ วินาที
    @Override
    public void tick() {
        super.tick();


        // --- 🌀 🔥 ส่วนที่ 1: สร้างเอฟเฟกต์สปิน (Particle Helix) 🔥 🌀 ---
        // เราทำแค่ฝั่ง Client เพราะ Client เป็นคนวาดรูปกราฟิก
        if (this.getWorld().isClient) {
            // ดึงอายุของเอนทิตีมาใช้เป็นมุม (เพื่อให้มันหมุนตามเวลา)
            // 💡 วาสามารถคูณตัวเลขเพื่อปรับความเร็วในการหมุนได้ ยิ่งเยอะยิ่งหมุนเร็ว
            float rotationAngle = (float) this.age * 0.8F;

            // กำหนดรัศมีของวงกลมสปิน
            // 💡 ปรับเลขนี้เพื่อขยายหรือบีบวงสปินให้กว้าง/แคบ (0.35 คือกำลังดี ใหญ่กว่าลูกบอลนิดหน่อย)
            double radius = 0.35;

            // คำนวณพิกัด X และ Z รอบตัวบอล
            double px = Math.cos(rotationAngle) * radius;
            double pz = Math.sin(rotationAngle) * radius;

            // สั่งสร้างสะเก็ด 1 อัน (ใช้ TOTEM_OF_UNDYING เพื่อให้ได้สีทอง SBR)
            // เกิดที่ตำแหน่ง: (ตัวบอลX + x) , (ตัวบอลY + y) , (ตัวบอลZ + z)
            // Y + 0.15F คือให้มันเกิดแถวๆ ช่วงกลาง-บนของลูกบอล
            this.getWorld().addParticle(
                    net.minecraft.particle.ParticleTypes.TOTEM_OF_UNDYING, // ✨ สีทองอร่าม
                    this.getX() + px,
                    this.getY() + 0.15F,
                    this.getZ() + pz,
                    0.0, 0.1F, 0.0
            );

            // 2. [ทริค SE] สร้างอีก 1 อันที่มุมตรงข้าม (เพื่อความนัว)
            // เราบวกมุมเพิ่มไป 180 องศา (หรือ Math.PI ในทาง Java)
            double rotationAngle2 = rotationAngle + Math.PI;
            double px2 = Math.cos(rotationAngle2) * radius;
            double pz2 = Math.sin(rotationAngle2) * radius;

            this.getWorld().addParticle(
                    net.minecraft.particle.ParticleTypes.TOTEM_OF_UNDYING, // ✨ สีทองอร่าม
                    this.getX() + px2,
                    this.getY() + 0.15F,
                    this.getZ() + pz2,
                    0.0, 0.1F, 0.0
            );

            // 3. (Optional) เพิ่มประกายไฟนิดหน่อยตรงใจกลาง (CRIT) เพื่อบอกว่ามันคม
            if (this.age % 2 == 0) { // เกิดแค่วันเว้นวัน (ติ๊กเว้นติ๊ก) จะได้ไม่รกเกินไป
                this.getWorld().addParticle(
                        net.minecraft.particle.ParticleTypes.CRIT,
                        this.getX(),
                        this.getY() + 0.15F,
                        this.getZ(),
                        0.0, 0.0, 0.0
                );
            }
        }
        // --- 🔚 จบส่วนเอฟเฟกต์สปิน ---


        if (this.isReturning) {
            Entity owner = this.getOwner();
            // ถ้าเจ้าของตาย หรือหายไปจากโลก ให้ทำลายตัวเองทิ้ง
            if (owner == null || !owner.isAlive()) {
                this.discard();
                return;
            }

            // คำนวณพิกัดเป้าหมาย (เล็งไปที่ช่วงอกของผู้เล่น)
            Vec3d ownerPos = new Vec3d(owner.getX(), owner.getEyeY() - 0.5, owner.getZ());
            // หาเวกเตอร์ทิศทางจากตำแหน่งบอลปัจจุบันชี้ไปหาผู้เล่น
            Vec3d direction = ownerPos.subtract(this.getPos()).normalize();

            // สั่งให้บอลพุ่งกลับมาด้วยความเร็วค่อนข้างไว (1.2F) และล็อกค่าไว้
            this.setVelocity(direction.multiply(1.2));

            // เช็กถ้าระยะห่างระหว่างบอลกับตัวผู้เล่นเหลือน้อยกว่า 1.2 บล็อก (แปลว่าบินมาถึงมือแล้ว)
            if (this.distanceTo(owner) < 1.2) {
                if (!this.getWorld().isClient) {
                    if (owner instanceof PlayerEntity player) {
                        // คืนไอเทมเข้ากระเป๋า (ถ้าไม่ใช่โหมด Creative)
                        if (!player.getAbilities().creativeMode) {
                            ItemStack stack = new ItemStack(Steel_ball_run.STEEL_BALL);
                            // ถ้ากระเป๋าไม่เต็มให้ยัดเข้าตัว ถ้ากระเป๋าเต็มให้ดรอปที่พื้นข้างตัวแทน
                            if (!player.getInventory().insertStack(stack)) {
                                player.dropItem(stack, false);
                            }
                        }
                    }
                    this.discard(); // ทำลายเอนทิตีบนฟ้าทิ้งเพราะกลับถึงมือเรียบร้อย
                }
            }
        }
    }

    // 3. ปรับตอนชนมอนสเตอร์: สั่งเปิดสวิตช์เด้งกลับแทนการทำลายตัวเอง
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (this.isReturning) return; // ถ้ากำลังบินกลับอยู่ ไม่ต้องทำความเสียหายซ้ำ

        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            if (entityHitResult.getEntity() instanceof LivingEntity target) {
                // ทำดาเมจยัดสถานะหมุนติ้ว (โค้ดเดิม)
                target.damage(this.getDamageSources().thrown(this, this.getOwner()), 4.0F);
                target.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                        Steel_ball_run.SPIN_EFFECT, 100, 0
                ));

                // เปลี่ยนสถานะให้บินกลับ และเปิด noClip เพื่อให้บอลบินทะลุบล็อกกลับมาหาเราได้แบบเท่ๆ
                this.isReturning = true;
                this.noClip = true;
            } else {
                this.discard();
            }
        }
    }

    // 4. ปรับตอนชนบล็อกพื้นผิว: สั่งให้เด้งกลับมาหาเราเช่นกัน
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (this.isReturning) return;

        super.onBlockHit(blockHitResult);
        if (!this.getWorld().isClient) {
            // ปาชนกำแพงหรือพื้นดิน ก็ให้เปิดสวิตช์บินกลับมาหาเราทันที ไม่หายไปดื้อๆ
            this.isReturning = true;
            this.noClip = true;
        }
    }
}