package braziliannight.model;

import braziliannight.entity.BNThunderhead;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;

public class ModelThunderhead extends EntityModel<BNThunderhead>
{

  public RendererModel body;

  public ModelThunderhead()
    {
      textureWidth = 32;
      textureHeight = 32;

      body = new RendererModel(this, 0, 0);
      body.addBox(-7.0F, 0.0F, -0.5F, 14, 14, 1, 0.0F);
      body.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

  @Override
  public void render (BNThunderhead thunderhead, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch, float scale)
    {
      super.render(thunderhead, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      body.render(scale);
    }
}
