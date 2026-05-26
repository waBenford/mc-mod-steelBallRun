package wa.d.steel_ball_run.client.mixin;

import net.minecraft.client.render.RenderLayer; // 🔥 เพิ่มอิมพอร์ตตัวนี้
import net.minecraft.client.render.VertexConsumer; // 🔥 เพิ่มอิมพอร์ตตัวนี้
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.joml.Matrix4f; // 🔥 เพิ่มอิมพอร์ตตัวนี้
import wa.d.steel_ball_run.Steel_ball_run;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Inject(method = "renderItem", at = @At("HEAD"))
    public void injectSteelBallSpin(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        if (stack.isOf(Steel_ball_run.STEEL_BALL) && entity.isUsingItem() && entity.getActiveItem() == stack) {

            // 1. โลจิกหมุนตัวลูกเหล็กสีเขียวหลัก (โค้ดเดิมของวาที่นิ่งสนิทแล้ว)
            float spinSpeed = 8.0F;
            float angle = (System.currentTimeMillis() % 360000) * spinSpeed;
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));

            // --- 🌀 ✨ ระบบเอฟเฟกต์โกลเด้นสปินแบบเรนเดอร์ล็อกติดโมเดล 100% ✨ 🌀 ---

            // ตั้งความเร็วและคำนวณมุมหมุนของเม็ดพลังงานสีทอง
            float effectSpeed = 6.0F; // 💡 วาปรับความเร็วการวิ่งวนได้ตรงนี้เลยครับ
            float effectAngle = (System.currentTimeMillis() % 360000) * effectSpeed;

            // เรียกใช้เลเยอร์ Lightning: จุดเด่นคือจะเรืองแสงในที่มืด (Unlit) และโปร่งแสงสวยงาม
            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLightning());
            float s = 0.025F; // 💡 ขนาดเม็ดพลังงานสีทอง (เล็ก กระชับ คลีนตา ไม่บังลูกเหล็ก)
            float radius = 0.16F; // 💡 รัศมีวงโคจร (รัดรอบผิวลูกเหล็กพอดีเป๊ะ)

            // 🌟 เม็ดที่ 1: วิ่งทะแยงมุมแนวไขว้ซ้าย
            matrices.push(); // แยกเมทริกซ์ออกไปคำนวณอิสระ
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45)); // เอียงแกน X ทะแยงมุม
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(20)); // เอียงแกน Z
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(effectAngle)); // สั่งวิ่งวนรอบรอบแกน
            matrices.translate(radius, 0, 0); // ยื่นตำแหน่งออกไปตามรัศมีรอบบอล

            Matrix4f mat1 = matrices.peek().getPositionMatrix();
            // วาดสี่เหลี่ยมผืนผ้าเรืองแสงสีทองเม็ดเดี่ยวๆ (RGB: 1.0, 0.85, 0.0)
            buffer.vertex(mat1, -s, -s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            buffer.vertex(mat1, s, -s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            buffer.vertex(mat1, s, s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            buffer.vertex(mat1, -s, s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            matrices.pop(); // คืนค่าเมทริกซ์

            // 🌟 เม็ดที่ 2: วิ่งทะแยงมุมแนวไขว้ขวา (ตัดกันเป็นรูปทรงคล้ายวงแหวนอะตอม/ไจโรสโคป)
            matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-45)); // เอียงสลับฝั่งตรงข้าม
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(effectAngle + 180)); // +180 เพื่อให้อยู่คนละฝั่ง
            matrices.translate(radius, 0, 0);

            Matrix4f mat2 = matrices.peek().getPositionMatrix();
            buffer.vertex(mat2, -s, -s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            buffer.vertex(mat2, s, -s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            buffer.vertex(mat2, s, s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            buffer.vertex(mat2, -s, s, 0).color(1.0F, 0.85F, 0.0F, 0.9F);
            matrices.pop();

            // --- 🔚 จบระบบเอฟเฟกต์สปินแบบใหม่ ---
        }
    }
}