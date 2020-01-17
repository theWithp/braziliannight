package braziliannight.api.lychanitesmobs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;

import braziliannight.BN;
import braziliannight.entity.BNEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
public class ModelCreatureObj extends ModelCreatureBase implements IAnimationModel
{
  // Global:
  /**
   * An initial x rotation applied to make Blender models match Minecraft. TODO:
   * look into better blender export options to make this redundant.
   **/
  public static float modelXRotOffset = 180F;
  /**
   * An initial y offset applied to make Blender models match Minecraft. TODO:
   * look into better blender export options to make this redundant.
   **/
  public static float modelYPosOffset = -1.5F;

  // Model:
  /**
   * An INSTANCE of the model, the model should only be set once and not during
   * every tick or things will get very laggy!
   **/
  public TessellatorModel wavefrontObject;

  /** A list of all parts that belong to this model's wavefront obj. **/
  public List<ObjObject> wavefrontParts;

  /** A list of all part definitions that this model will use when animating. **/
  public Map<String, ModelObjPart> animationParts = new HashMap<>();

  // Looking and Head:
  /**
   * Used to scale how far the head part will turn based on the looking X angle.
   **/
  public float lookHeadScaleX = 1;
  /**
   * Used to scale how far the head part will turn based on the looking Y angle.
   **/
  public float lookHeadScaleY = 1;
  /**
   * Used to scale how far the neck part will turn based on the looking X angle.
   **/
  public float lookNeckScaleX = 0;
  /**
   * Used to scale how far the neck part will turn based on the looking Y angle.
   **/
  public float lookNeckScaleY = 0;
  /**
   * Used to scale how far the head part will turn based on the looking X angle.
   **/
  public float lookBodyScaleX = 0;
  /**
   * Used to scale how far the head part will turn based on the looking Y angle.
   **/
  public float lookBodyScaleY = 0;

  // Animating:
  /**
   * The animator INSTANCE, this is a helper class that performs actual GL11
   * functions, etc.
   **/
  protected Animator animator;
  /**
   * The current animation part that is having an animation frame generated for.
   **/
  protected ModelObjPart currentAnimationPart;
  /** The animation data for this model. **/
  protected ModelAnimation animation;
  /**
   * A list of models states that hold unique render/animation data for a specific
   * entity INSTANCE.
   **/
  protected Map<Entity, ModelObjState> modelStates = new HashMap<>();
  /**
   * The current model state for the entity that is being animated and rendered.
   **/
  protected ModelObjState currentModelState;

  // ==================================================
  // Constructors
  // ==================================================
  public ModelCreatureObj()
    {
      this(1.0F);
    }

  public ModelCreatureObj(float shadowSize)
    {
      // Here a model should get its model, collect its parts into a list and then
      // create ModelObjPart objects for each part.
    }

  // ==================================================
  // Init Model
  // ==================================================
  public ModelCreatureObj initModel (String path)
    {

      // Load Obj Model:
      this.wavefrontObject = new TessellatorModel(new ResourceLocation(BN.MODID, "models/" + path + ".obj"));
      this.wavefrontParts = this.wavefrontObject.objObjects;

      // Create Animator:
      this.animator = new Animator();

      // Load Model Parts:
      ResourceLocation modelPartsLocation = new ResourceLocation(BN.MODID, "models/" + path + "_parts.json");
      try {
			Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
          InputStream in = Minecraft.getInstance().getResourceManager().getResource(modelPartsLocation).getInputStream();
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          try {
				JsonArray jsonArray = JSONUtils.fromJson(gson, reader, JsonArray.class, false);
              Iterator<JsonElement> jsonIterator = jsonArray.iterator();
              while (jsonIterator.hasNext()) {
                  JsonObject partJson = jsonIterator.next().getAsJsonObject();
					ModelObjPart animationPart = new ModelObjPart();
					animationPart.loadFromJson(partJson);
					this.addAnimationPart(animationPart);
              }
          }
          finally {
              IOUtils.closeQuietly(reader);
          }
      }
      catch (Exception e) {
			// TODO: Logging
      }

      // Assign Model Part Children:
      for (ModelObjPart part : this.animationParts.values())
        {
          part.addChildren(this.animationParts.values().toArray(new ModelObjPart[this.animationParts.size()]));
        }

      // Load Animations:
      ResourceLocation animationLocation = new ResourceLocation(BN.MODID,
          "models/" + path + "_animation.json");
		try {
			Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
			InputStream in = Minecraft.getInstance().getResourceManager().getResource(animationLocation).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			try {
				JsonObject json = JSONUtils.fromJson(gson, reader, JsonObject.class, false);
				this.animation = new ModelAnimation();
				this.animation.loadFromJson(json);
			}
			finally {
				IOUtils.closeQuietly(reader);
			}
		}
		catch (Exception e) {
			//TODO: Logging
		}


      return this;
    }

  // ==================================================
  // Parts
  // ==================================================
  // ========== Add Animation Part ==========
  public void addAnimationPart (ModelObjPart animationPart)
    {
      if (this.animationParts.containsKey(animationPart.name))
        {
          return;
        }
      if (animationPart.parentName != null)
        {
          if (animationPart.parentName.equals(animationPart.name))
            animationPart.parentName = null;
        }
      this.animationParts.put(animationPart.name, animationPart);
    }

  // ==================================================
  // Render Model
  // ==================================================
  /**
   * Renders this model. Can be rendered as a trophy (just head, mouth, etc) too,
   * use scale for this.
   * 
   * @param entity   Can't be null but can be any entity. If the mob's exact
   *                 entity or an EntityCreatureBase is used more animations will
   *                 be used.
   * @param time     How long the model has been displayed for? This is currently
   *                 unused.
   * @param distance Used for movement animations, this should just count up form
   *                 0 every tick and stop back at 0 when not moving.
   * @param loop     A continuous loop counting every tick, used for constant idle
   *                 animations, etc.
   * @param lookY    A y looking rotation used by the head, etc.
   * @param lookX    An x looking rotation used by the head, etc.
   * @param scale    Use to scale this mob. The default scale is 0.0625 (not sure
   *                 why)! For a trophy/head-only model, set the scale to a
   *                 negative amount, -1 will return a head similar in size to
   *                 that of a Zombie head.
   * @param animate  If true, animation frames will be generated and cleared after
   *                 each render tick, if false, they must be generated and
   *                 cleared manually.
   */
  @Override
	public void render(BNEntity entity, float time, float distance, float loop, float lookY, float lookX, float scale, boolean animate) {
				scale *= 16;
				scale *= entity.getRenderScale();

		// GUI Render:
		if(entity != null) {
			if(entity.onlyRenderTicks >= 0) {
				loop = entity.onlyRenderTicks;
			}
		}

		// Animation States:
      this.currentModelState = this.getModelState(entity);
			this.updateAttackProgress(entity);

      // Generate Animation Frames:
		if(animate) {
			this.generateAnimationFrames(entity, time, distance, loop, lookY, lookX, scale);
		}

		// Render Parts:
      for(ObjObject part : this.wavefrontParts) {
          String partName = part.getName().toLowerCase();
          if(!this.canRenderPart(partName, entity))
              continue;
          this.currentAnimationPart = this.animationParts.get(partName);
          if(this.currentAnimationPart == null) {
          	continue;
			}

          // Begin Rendering Part:
          GlStateManager.pushMatrix();

          // Apply Initial Offsets: (To Match Blender OBJ Export)
          this.animator.doAngle(modelXRotOffset, 1F, 0F, 0F);
          this.animator.doTranslate(0F, modelYPosOffset, 0F);

          // Apply Entity Scaling:
          this.animator.doScale(scale, scale, scale);

          // Apply Animation Frames:
          this.currentAnimationPart.applyAnimationFrames(this.animator);

          // Render Part:
			this.onRenderStart(entity);
          this.wavefrontObject.renderGroup(part, this.getPartColor(partName, entity, loop), this.getPartTextureOffset(partName, entity, loop));
			this.onRenderFinish(entity);
			GlStateManager.popMatrix();
		}

		// Clear Animation Frames:
		if(animate) {
			this.clearAnimationFrames();
		}
  }

  /** Called just before a layer is rendered. **/
  public void onRenderStart (Entity entity)
    {
      GlStateManager.enableBlend();
    }

  /** Called just after a layer is rendered. **/
  public void onRenderFinish (Entity entity)
    {
      GlStateManager.disableBlend();
    }

  /** Generates all animation frames for a render tick. **/
  public void generateAnimationFrames (Entity entity, float time, float distance, float loop, float lookY, float lookX,
      float scale)
    {
      for (ObjObject part : this.wavefrontParts)
        {
          String partName = part.getName().toLowerCase();
          // if(!this.canRenderPart(partName, entity, layer, renderAsTrophy))
          // continue;
          this.currentAnimationPart = this.animationParts.get(partName);
          if (this.currentAnimationPart == null)
            continue;

          // Animate:
          if (entity instanceof LivingEntity)
            {
              this.animatePart(partName, (LivingEntity) entity, time, distance, loop, -lookY, lookX, scale);
            }
        }
    }

  /** Clears all animation frames that were generated for a render tick. **/
  public void clearAnimationFrames ()
    {
      for (ModelObjPart animationPart : this.animationParts.values())
        {
          animationPart.animationFrames.clear();
        }
    }

  // ==================================================
  // Can Render Part
  // ==================================================
  /**
   * Returns true if the part can be rendered.
   **/
  @Override
  public boolean canRenderPart (String partName, Entity entity)
    {
      if (partName == null)
        return false;
      partName = partName.toLowerCase();

      // Check Animation Part:
      if (!this.animationParts.containsKey(partName))
        return false;

      return true;
    }

  // ==================================================
  // Animate Part
  // ==================================================
  /**
   * Animates the individual part.
   * 
   * @param partName The name of the part (should be made all lowercase).
   * @param entity   Can't be null but can be any entity. If the mob's exact
   *                 entity or an EntityCreatureBase is used more animations will
   *                 be used.
   * @param time     How long the model has been displayed for? This is currently
   *                 unused.
   * @param distance Used for movement animations, this should just count up from
   *                 0 every tick and stop back at 0 when not moving.
   * @param loop     A continuous loop counting every tick, used for constant idle
   *                 animations, etc.
   * @param lookY    A y looking rotation used by the head, etc.
   * @param lookX    An x looking rotation used by the head, etc.
   * @param scale    Used for scale based changes during animation but not to
   *                 actually apply the scale as it is applied in the renderer
   *                 method.
   */
  public void animatePart (String partName, LivingEntity entity, float time, float distance, float loop, float lookY,
      float lookX, float scale)
    {
      float rotX = 0F;
      float rotY = 0F;
      float rotZ = 0F;

      // Looking:
      if (partName.toLowerCase().equals("head"))
        {
          rotX += (Math.toDegrees(lookX / (180F / (float) Math.PI)) * this.lookHeadScaleX);
          rotY += (Math.toDegrees(lookY / (180F / (float) Math.PI))) * this.lookHeadScaleY;
        }
      if (partName.equals("neck"))
        {
          rotX += (Math.toDegrees(lookX / (180F / (float) Math.PI)) * this.lookNeckScaleX);
          rotY += (Math.toDegrees(lookY / (180F / (float) Math.PI))) * this.lookNeckScaleY;
        }

      // Create Animation Frames:
      this.rotate(rotX, rotY, rotZ);
    }

  /** Returns an existing or new model state for the given entity. **/
  public ModelObjState getModelState (Entity entity)
    {
      if (entity == null)
        return null;
      if (this.modelStates.containsKey(entity))
        {
          if (!entity.isAlive())
            {
              this.modelStates.remove(entity);
              return null;
            }
          return this.modelStates.get(entity);
        }
      ModelObjState modelState = new ModelObjState(entity);
      this.modelStates.put(entity, modelState);
      return modelState;
    }

  // ==================================================
  // Attack Frame
  // ==================================================
  public void updateAttackProgress (Entity entity)
    {}

  public float getAttackProgress ()
    {
      if (this.currentModelState == null)
        return 0;
      return this.currentModelState.attackAnimationProgress;
    }

  // ==================================================
  // Create Frames
  // ==================================================
  @Override
  public void angle (float rotation, float angleX, float angleY, float angleZ)
    {
      this.currentAnimationPart
          .addAnimationFrame(new ModelObjAnimationFrame("angle", rotation, angleX, angleY, angleZ));
    }

  @Override
  public void rotate (float rotX, float rotY, float rotZ)
    {
      this.currentAnimationPart.addAnimationFrame(new ModelObjAnimationFrame("rotate", 1, rotX, rotY, rotZ));
    }

  @Override
  public void translate (float posX, float posY, float posZ)
    {
      this.currentAnimationPart.addAnimationFrame(new ModelObjAnimationFrame("translate", 1, posX, posY, posZ));
    }

  @Override
  public void scale (float scaleX, float scaleY, float scaleZ)
    {
      this.currentAnimationPart.addAnimationFrame(new ModelObjAnimationFrame("scale", 1, scaleX, scaleY, scaleZ));
    }

  // ==================================================
  // Rotate to Point
  // ==================================================
  @Override
  public double rotateToPoint (double aTarget, double bTarget)
    {
      return rotateToPoint(0, 0, aTarget, bTarget);
    }

  @Override
  public double rotateToPoint (double aCenter, double bCenter, double aTarget, double bTarget)
    {
      if (aTarget - aCenter == 0)
        if (aTarget > aCenter)
          return 0;
        else if (aTarget < aCenter)
          return 180;
      if (bTarget - bCenter == 0)
        if (bTarget > bCenter)
          return 90;
        else if (bTarget < bCenter)
          return -90;
      if (aTarget - aCenter == 0 && bTarget - bCenter == 0)
        return 0;
      return Math.toDegrees(Math.atan2(aCenter - aTarget, bCenter - bTarget) - Math.PI / 2);
    }

  @Override
  public double[] rotateToPoint (double xCenter, double yCenter, double zCenter, double xTarget, double yTarget,
      double zTarget)
    {
      double[] rotations = new double[3];
      rotations[0] = this.rotateToPoint(yCenter, -zCenter, yTarget, -zTarget);
      rotations[1] = this.rotateToPoint(-zCenter, xCenter, -zTarget, xTarget);
      rotations[2] = this.rotateToPoint(yCenter, xCenter, yTarget, xTarget);
      return rotations;
    }

  // ==================================================
  // Shift Origin
  // ==================================================

  /**
   * Moves the animation origin to a different part origin.
   * 
   * @param fromPartName The part name to move the origin from.
   * @param toPartName   The part name to move the origin to.
   */
  public void shiftOrigin (String fromPartName, String toPartName)
    {
      ModelObjPart fromPart = this.animationParts.get(fromPartName);
      ModelObjPart toPart = this.animationParts.get(toPartName);
      float offsetX = toPart.centerX - fromPart.centerX;
      float offsetY = toPart.centerY - fromPart.centerY;
      float offsetZ = toPart.centerZ - fromPart.centerZ;
      this.translate(offsetX, offsetY, offsetZ);
    }

  /**
   * Moves the animation origin back from a different part origin.
   * 
   * @param fromPartName The part name that the origin moved from.
   * @param toPartName   The part name that the origin was moved to.
   */
  public void shiftOriginBack (String fromPartName, String toPartName)
    {
      ModelObjPart fromPart = this.animationParts.get(fromPartName);
      ModelObjPart toPart = this.animationParts.get(toPartName);
      float offsetX = toPart.centerX - fromPart.centerX;
      float offsetY = toPart.centerY - fromPart.centerY;
      float offsetZ = toPart.centerZ - fromPart.centerZ;
      this.translate(-offsetX, -offsetY, -offsetZ);
    }
}
