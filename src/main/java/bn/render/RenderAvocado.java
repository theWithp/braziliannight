package bn.render;

import bn.BNConstants;
import bn.entity.Avocado;
import bn.model.ModelAvocado;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderAvocado extends RenderLiving<EntityLiving>
{
  private static final ResourceLocation loc = new ResourceLocation(BNConstants.MOD_ID, "textures/avocado.png");

  public RenderAvocado(RenderManager m, float shadowSize)
    {
      super(m, new ModelAvocado(), shadowSize);
    }

  @Override
  protected ResourceLocation getEntityTexture (EntityLiving entity)
    {

      return loc;
    }

  public static void register ()
    {
      RenderingRegistry.registerEntityRenderingHandler(Avocado.class,
          renderManager -> new RenderAvocado(renderManager, 1.0f));
    }
}
