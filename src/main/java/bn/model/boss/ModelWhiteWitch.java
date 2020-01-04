package bn.model.boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import bn.entity.boss.WhiteWitch;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWhiteWitch extends ModelBase
{
  // instance variables
  private Map<String, ModelRenderer> parts = new HashMap<>();
  private Map<String, List<Float>> restRots = new HashMap<>();

  // constants
  private static final String[] PART_NAMES = { "Ball1", "Ball2", "LowerTorso", "Torso", "RightShoulder", "LeftShoulder",
      "RightArm", "LeftArm", "Head" };
  private static final int[][] OFFSETS = { { 21, 0 }, { 21, 0 }, { 0, 21 }, { 0, 32 }, { 0, 47 }, { 0, 56 }, { 17, 55 },
      { 17, 55 }, { 17, 17 } };
  private static final float[][] BOXES = { { -3, -3, -3, 6, 6, 6 }, { -3, -3, -3, 6, 6, 6 }, { -2, -6, -2, 4, 6, 4 },
      { -3, -8, -3, 6, 8, 6 }, { -4, -2, -2, 4, 4, 4 }, { 0, -2, -2, 4, 4, 4 }, { -3.5f, 2, -1.5f, 3, 6, 3 },
      { -0.5f, 2, -1.5f, 3, 6, 3 }, { -3.5f, -7, -3.5f, 7, 7, 7 } };
  private static final float[][] ROTATION_POINTS = { { 0, 10, 0 }, { 0, 10, 0 }, { 0, 5, 0 }, { 0, -1, 0 },
      { -3, -7, 0 }, { 3, -7, 0 }, { -3, -7, 0 }, { 3, -7, 0 }, { 0, -9, 0 } };
  private static final int SIDE_LENGTH = 64;

  // constructor
  public ModelWhiteWitch()
    {
      textureWidth = SIDE_LENGTH;
      textureHeight = SIDE_LENGTH;
      for (int i = 0; i < PART_NAMES.length; i++)
        {
          ModelRenderer part = new ModelRenderer(this, OFFSETS[i][0], OFFSETS[i][1]);
          part.addBox(BOXES[i][0], BOXES[i][1], BOXES[i][2], (int) BOXES[i][3], (int) BOXES[i][4], (int) BOXES[i][5]);
          part.setRotationPoint(ROTATION_POINTS[i][0], ROTATION_POINTS[i][1], ROTATION_POINTS[i][2]);
          part.setTextureSize(textureWidth, textureHeight);
          part.mirror = true; // this was in the original but not in Gaia code. necessary?
          List<Float> lst = new ArrayList<>();
          lst.add(part.rotateAngleX);
          lst.add(part.rotateAngleY);
          restRots.put(PART_NAMES[i], lst);
          parts.put(PART_NAMES[i], part);
        }
    }

  // TODO split this into multiple methods and get the math into constants
  @Override
  public void render (Entity e, float limbSwng, float limbSwingAmnt, float tickAge, float headYaw, float headPitch,
      float scale)
    {
      super.render(e, limbSwng, limbSwingAmnt, tickAge, headYaw, headPitch, scale);
      GL11.glPushMatrix();
      parts.get("Head").rotateAngleX = (float) Math.toRadians(headYaw);
      parts.get("Head").rotateAngleY = (float) Math.toRadians(headPitch);

      WhiteWitch witch = (WhiteWitch) e;
      if (witch.isSpinning())
        GL11.glRotatef(witch.getSpinRotation() - 40 * (tickAge - witch.ticksExisted), 0, 1, 0);

      // update rotations
      float ticksInAction = witch.getTicksInCurrentAction() + tickAge - witch.ticksExisted;
      float rot = (float) Math.toRadians(witch.getOrbitRotation() + 2 * (tickAge - witch.ticksExisted));

      for (String s : parts.keySet())
        if (s.contains("Ball"))
          parts.get(s).rotateAngleY = restRots.get(s).get(1) + rot;
      parts.get("Ball2").rotateAngleX = (float) (restRots.get("Ball2").get(0) + rot - Math.PI / 4);

      float rightArmRot = 0; // 0 happens to be the value we want if we're in the 3rd state, idle.
      float leftArmRot = 0;
      if (witch.isCasting())
        {
          if (ticksInAction < 10)
            rightArmRot = (float) Math.toRadians(-160 * ticksInAction / 10);
          else if (ticksInAction < 13)
            rightArmRot = (float) Math.toRadians(-160 + 80 * (ticksInAction - 10) / 3);
          else
            rightArmRot = 80;
          if (witch.usingLeftArm())
            {
              leftArmRot = rightArmRot;
              rightArmRot = 0;
            }
        } else if (witch.isSpinning())
        {
          float degrees = 180;
          if (ticksInAction < 20)
            degrees *= ticksInAction / 20;
          rightArmRot = (float) Math.toRadians(degrees);
          leftArmRot = rightArmRot;
        }

      for (String s : parts.keySet())
        {
          if (s.contains("Right"))
            parts.get(s).rotateAngleX = rightArmRot;
          else if (s.contains("Left"))
            parts.get(s).rotateAngleX = leftArmRot;

        }
      
      // Sine function governing boss' and balls' initial y axis motion
      GL11.glPushMatrix();
      GL11.glTranslatef(0, (float) (Math.sin(tickAge / 10) / 17), 0);
      for (String s : parts.keySet())
        if (s.contains("Ball"))
          parts.get(s).render(scale);

      // Sine function governing boss' y axis motion
      GL11.glPushMatrix();
      GL11.glTranslatef(0, 0.25f * (float) Math.sin(tickAge / 10 - 2), 0);

      for (String s : parts.keySet())
        {
          if (!s.contains("Ball"))
            parts.get(s).render(scale);
        }

      for (int i = 0; i < 3; i++)
        GL11.glPopMatrix();
    }
}
