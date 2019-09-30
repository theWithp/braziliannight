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

public class ModelWhiteWitch extends ModelBase {
    // this stuff should all go into some kind of file and get loaded by a utility
    // class.
    private static final String[] names = { "Ball1", "Ball2", "LowerTorso", "Torso", "RightShoulder", "LeftShoulder",
	    "RightArm", "LeftArm", "Head" };
    private static final int[][] offsets = { { 21, 0 }, { 21, 0 }, { 0, 21 }, { 0, 32 }, { 0, 47 }, { 0, 56 },
	    { 17, 55 }, { 17, 55 }, { 17, 17 } };
    private static final float[][] boxes = { { -3, -3, -3, 6, 6, 6 }, { -3, -3, -3, 6, 6, 6 }, { -2, -6, -2, 4, 6, 4 },
	    { -3, -8, -3, 6, 8, 6 }, { -4, -2, -2, 4, 4, 4 }, { 0, -2, -2, 4, 4, 4 }, { -3.5f, 2, -1.5f, 3, 6, 3 },
	    { -0.5f, 2, -1.5f, 3, 6, 3 }, { -3.5f, -7, -3.5f, 7, 7, 7 } };
    private static final float[][] rotationPoints = { { 0, 10, 0 }, { 0, 10, 0 }, { 0, 5, 0 }, { 0, -1, 0 },
	    { -3, -7, 0 }, { 3, -7, 0 }, { -3, -7, 0 }, { 3, -7, 0 }, { 0, -9, 0 } };

    private Map<String, ModelRenderer> parts = new HashMap<>();
    private Map<String, List<Float>> restRots = new HashMap<>();

    public ModelWhiteWitch() {
	textureWidth = 64;
	textureHeight = 64;
	for (int i = 0; i < names.length; i++) {
	    ModelRenderer part = new ModelRenderer(this, offsets[i][0], offsets[i][1]);
	    part.addBox(boxes[i][0], boxes[i][1], boxes[i][2], (int) boxes[i][3], (int) boxes[i][4], (int) boxes[i][5]);
	    part.setRotationPoint(rotationPoints[i][0], rotationPoints[i][1], rotationPoints[i][2]);
	    part.setTextureSize(textureWidth, textureHeight);
	    part.mirror = true; // this was in the original but not in Gaia code. necessary?
	    List<Float> lst = new ArrayList<>();
	    lst.add(part.rotateAngleX);
	    lst.add(part.rotateAngleY);
	    restRots.put(names[i], lst);
	    parts.put(names[i], part);
	}
    }

    @Override
    public void render(Entity e, float limbSwng, float limbSwingAmnt, float tickAge, float headYaw, float headPitch,
	    float scale) {
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
	for (String s : parts.keySet()) {
	    if (s.contains("Ball"))
		parts.get(s).rotateAngleY = restRots.get(s).get(1) + rot;
	}
	parts.get("Ball2").rotateAngleX = (float) (restRots.get("Ball2").get(0) + rot - Math.PI / 4);

	float rightArmRot = 0, leftArmRot = 0;
	if (witch.isCasting()) {
	    if (ticksInAction < 10) {
		rightArmRot = (float) Math.toRadians(-160 * ticksInAction / 10);
	    } else if (ticksInAction < 13) {
		rightArmRot = (float) Math.toRadians(-160 + 80 * (ticksInAction - 10) / 3);
	    } else {
		rightArmRot = 80;
	    }
	    if (witch.usingLeftArm()) {
		leftArmRot = rightArmRot;
		rightArmRot = 0;
	    }
	} else if (witch.isSpinning()) {
	    float degrees = 180;
	    if (ticksInAction < 20)
		degrees *= ticksInAction / 20;
	    rightArmRot = (float) Math.toRadians(degrees);
	    leftArmRot = rightArmRot;
	}

	for (String s : parts.keySet()) {
	    if (s.contains("Right")) {
		parts.get(s).rotateAngleX = rightArmRot;
	    } else if (s.contains("Left")) {
		parts.get(s).rotateAngleX = leftArmRot;
	    }
	}
	GL11.glPushMatrix();
	GL11.glTranslatef(0, (float) (Math.sin(tickAge / 10) / 17), 0);
	for (String s : parts.keySet()) {
	    if (s.contains("Ball"))
		parts.get(s).render(scale);
	}
	GL11.glPushMatrix();
	GL11.glTranslatef(0, (float) Math.sin(tickAge / 10 - 2), 0);
	for (String s : parts.keySet()) {
	    if (!s.contains("Ball"))
		parts.get(s).render(scale);
	}
	for (int i = 0; i < 3; i++)
	    GL11.glPopMatrix();
    }
}
