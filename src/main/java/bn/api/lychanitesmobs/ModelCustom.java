package bn.api.lychanitesmobs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector4f;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class ModelCustom extends ModelBase
{

  // Initial Rotations:
  public Map<ModelRenderer, float[]> initRotations = new HashMap<>();

  // ==================================================
  // Constructors
  // ==================================================
  public ModelCustom()
    {
      this(1.0F);
    }

  public ModelCustom(float shadowSize)
    {
      // Texture:
      textureWidth = 128;
      textureHeight = 128;
    }

  // ==================================================
  // Set Rotation
  // ==================================================
  public void setRotation (ModelRenderer model, float x, float y, float z)
    {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;

      if (!initRotations.containsKey(model))
        initRotations.put(model, new float[] { x, y, z });
    }

  // ==================================================
  // Render Model
  // ==================================================
  @Override
  public void render (Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale)
    {
      this.render(entity, time, distance, loop, lookY, lookX, scale, true);
    }

  public void render (Entity entity, float time, float distance, float loop, float lookY, float lookX, float scale,
      boolean animate)
    {
      float sizeScale = 1F;
      if (entity instanceof EntityLiving)
        {
          // TODO: Implement scale factors
        }
      GL11.glScalef(sizeScale, sizeScale, sizeScale);
      GL11.glTranslatef(0, 0.5f - sizeScale / 2, 0);

      setAngles((EntityLiving) entity, time, distance, loop, lookY, lookX, scale);
      animate((EntityLiving) entity, time, distance, loop, lookY, lookX, scale);
    }

  // ==================================================
  // Can Render Part
  // ==================================================
  /**
   * Returns true if the part can be rendered, this can do various checks such as
   * Yale wool only rendering in the YaleWoolLayer or hiding body parts in place
   * of armor parts, etc.
   **/
  public boolean canRenderPart (String partName, Entity entity)
    {
      if (entity instanceof EntityLiving)
        return true;
      return false;
    }

  /** Returns true if the part can be rendered on the base layer. **/
  public boolean canBaseRenderPart (String partName, Entity entity, boolean trophy)
    {
      return true;
    }

  // ==================================================
  // Get Part Texture Offset - currently only returns (0,0)
  // ==================================================
  /** Returns the texture offset to be used for this part and layer. **/
  public Vector2f getPartTextureOffset (String partName, Entity entity, float loop)
    {
      return this.getBaseTextureOffset(partName, entity, loop);
    }

  /**
   * Returns the texture offset to be used for this part on the base layer (for
   * scrolling, etc).
   **/
  public Vector2f getBaseTextureOffset (String partName, Entity entity, float loop)
    {
      return new Vector2f(0, 0);
    }

  // ==================================================
  // Set Angles
  // ==================================================
  public void setAngles (EntityLiving entity, float time, float distance, float loop, float lookY, float lookX,
      float scale)
    {
      // Set Initial Rotations:
      for (Entry<ModelRenderer, float[]> initRotation : initRotations.entrySet())
        {
          float[] rotations = initRotation.getValue();
          setRotation(initRotation.getKey(), rotations[0], rotations[1], rotations[2]);
        }
    }

  // ==================================================
  // Animate Model
  // ==================================================
  public void animate (EntityLiving entity, float time, float distance, float loop, float lookY, float lookX,
      float scale)
    {
      return;
    }
}
