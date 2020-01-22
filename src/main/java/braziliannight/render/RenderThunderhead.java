package braziliannight.render;

import javax.annotation.Nonnull;

import braziliannight.BN;
import braziliannight.entity.BNThunderhead;
import braziliannight.model.ModelThunderhead;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderThunderhead extends MobRenderer<BNThunderhead, ModelThunderhead>
{

  public RenderThunderhead(EntityRendererManager renderManager)
    {
      super(renderManager, new ModelThunderhead(), 0.25f);
    }

  @Nonnull
  @Override
  protected ResourceLocation getEntityTexture (@Nonnull BNThunderhead entity)
    {
      return new ResourceLocation(BN.MODID, "textures/mobs/face/face.png");
    }
}
