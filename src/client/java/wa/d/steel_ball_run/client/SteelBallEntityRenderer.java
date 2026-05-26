package wa.d.steel_ball_run.client;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import wa.d.steel_ball_run.SteelBallEntity;
import wa.d.steel_ball_run.Steel_ball_run;

public class SteelBallEntityRenderer extends EntityRenderer<SteelBallEntity> {
    private final ItemRenderer itemRenderer;

    public SteelBallEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SteelBallEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // คำนวณมุมหมุนตามอายุเวลาจริงในเกม (entity.age + tickDelta ช่วยให้หมุนสมูทเนียนตาตาม FPS จริง)
        float age = entity.age + tickDelta;
        float angle = age * 45.0F; // 💡 วาปรับตัวเลขตรงนี้ได้ ยิ่งเยอะบอลยิ่งปั่นรัวสะใจตอนบิน

        // สั่งหมุนเบิ้ล 2 แกน (Y และ X) เพื่อให้เกิดเอฟเฟกต์ควงสว่าน 3 มิติสมจริงตอนพุ่งแหวกอากาศ
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angle * 0.5F));

        // สั่งวาดตัวโมเดลไอเทม Steel Ball สีเขียวของวาลงบนพิกัดความเร็วนี้
        this.itemRenderer.renderItem(
                new ItemStack(Steel_ball_run.STEEL_BALL),
                ModelTransformationMode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(SteelBallEntity entity) {
        // ดึง Texture จากระบบ Atlas มาตรฐานเพื่อใช้ประมวลผลแสงเงาบล็อก
        return net.minecraft.client.texture.SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}