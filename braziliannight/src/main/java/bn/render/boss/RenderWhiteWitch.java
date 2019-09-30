package bn.render.boss;

import bn.BNConstants;
import bn.entity.boss.WhiteWitch;
import bn.model.boss.ModelWhiteWitch;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderWhiteWitch extends RenderLiving<EntityLiving> {
    private static final ResourceLocation loc = new ResourceLocation(BNConstants.MOD_ID,
	    "textures/entity/boss/whitewitch.png");

    public RenderWhiteWitch(RenderManager m, float shadowSize) {
	super(m, new ModelWhiteWitch(), shadowSize);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLiving entity) {
	return loc;
    }

    public static void register() {
	RenderingRegistry.registerEntityRenderingHandler(WhiteWitch.class,
		renderManager -> new RenderWhiteWitch(renderManager, 0.5f));
    }
}
