package bn.entity.boss;

import static bn.BNConstants.WHITE_WITCHD;
import static bn.BNConstants.WHITE_WITCHF;
import static bn.BNConstants.WHITE_WITCHI;

import java.util.List;

import bn.entity.projectile.Whirlwind;
import bn.state.BNEntityState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;

public class WhiteWitch extends BNBossEntity {
    private boolean useLeftArm = false;
    private float spinRotation = 0, orbitRotation = 0;
    private int whirlwindCooldown = 0, hurricaneCooldown = 0;

    public WhiteWitch(World w) {
	super(w);
	this.setSize(WHITE_WITCHF.get("WIDTH"), WHITE_WITCHF.get("HEIGHT"));
    }

    @Override
    protected void initEntityAI() {
	tasks.addTask(1, new EntityAIWander(this, WHITE_WITCHD.get("MOVE_SPEED")));
	tasks.addTask(0, new EntityAIAttackRanged(this, WHITE_WITCHD.get("MOVE_SPEED"), WHITE_WITCHI.get("SHOT_CLOCK"),
		WHITE_WITCHF.get("ATTACK_RANGE")));
	targetTasks.addTask(0, new EntityAIHurtByTarget(this, false, new Class[0]));
	targetTasks.addTask(1,
		new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, false, false)); // we'll
													    // want this
													    // to be
													    // more
													    // specific
													    // at some
													    // point
    }

    @Override
    protected void applyEntityAttributes() {
	super.applyEntityAttributes(WHITE_WITCHD.get("MAX_HEALTH"));
    }

    @Override
    public int getTotalArmorValue() {
	return WHITE_WITCHI.get("ARMOR");
    }

    @Override
    protected float modifyDamageAmount(DamageSource source, float amnt) {
	if (source.isMagicDamage())
	    amnt *= WHITE_WITCHF.get("MAGIC_DAMAGE_MULT");
	return amnt;
    }

    @Override
    public void setState(BNEntityState state) {
	super.setState(state);
	spinRotation = 0;
	if (getState() == BNEntityState.CASTING)
	    useLeftArm = usingLeftArm() ^ true;
    }

    @Override
    public void onUpdate() {
	orbitRotation = getOrbitRotation() + 2;
	orbitRotation = getOrbitRotation() % 360;
	if (getState() == BNEntityState.SPINNING) {
	    if (getTicksInCurrentAction() > 160)
		setState(BNEntityState.IDLE);
	    else
		spinRotation = (getSpinRotation() - 40) % 360;
	}
	if (motionY < 0)
	    motionY *= 0.89999f; // feather fall effect
	super.onUpdate();
    }

    @Override
    public boolean canBePushed() {
	// you cannot push spinning airman
	return getState() != BNEntityState.SPINNING;
    }

    // you cannot fall damage air man
    @Override
    public void fall(float damage, float mult) {
    }

    // the old mechanism was basically that if anything was in hurricane range we
    // hurricaned else we whirlwinded
    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
	if (whirlwindCooldown-- <= 0) {
	    // if we had a whirlwind attack noise we'd play it here (note that they do their
	    // own target lock on thing)
	    Whirlwind shot = new Whirlwind(world, getPositionVector());
	    world.spawnEntity(shot);
	    whirlwindCooldown = WHITE_WITCHI.get("WHIRLWIND_RATE");
	    setState(BNEntityState.CASTING);
	    if (getTicksInCurrentAction() > 23)
		setState(BNEntityState.IDLE);
	} else if (hurricaneCooldown-- <= 0) {
	    hurricaneCooldown = WHITE_WITCHI.get("HURRICANE_RATE");
	    boolean successful = hurricaneAttack(target);
	    if (successful) {
		setState(BNEntityState.SPINNING);
		whirlwindCooldown = WHITE_WITCHI.get("WHIRLWIND_RATE");
	    }
	}
    }

    // this is basically an energy field that also causes things to get thrown in
    // various directions (it should probably have a visual indicator of some sort)
    private boolean hurricaneAttack(EntityLivingBase target) {
	boolean hadATarget = false;
	double expansion = WHITE_WITCHD.get("HURRICANE_SIZE");
	List<EntityLivingBase> inField = world.getEntitiesWithinAABB(EntityLivingBase.class,
		getEntityBoundingBox().expand(expansion, expansion, expansion));
	for (EntityLivingBase e : inField) {
	    if (e instanceof WhiteWitch)
		continue;
	    hadATarget = true;
	    Vec3d movement = getPositionVector().subtract(e.getPositionVector()).normalize();
	    float factor = WHITE_WITCHF.get("HURRICANE_FACTOR");
	    e.addVelocity(movement.x * factor, movement.y * factor, movement.z * factor);
	    // packet to force player motion went here. see if really needed
	    e.fallDistance = 0; // they've suffered enough.
	}
	return hadATarget;
    }

    // TODO
    @Override
    public void dropFewItems(boolean wasHit, int lootingMod) {
    }

    @Override
    protected Color getBarColor() {
	return Color.WHITE;
    }

    // I don't think we do anything here.
    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    public float getSpinRotation() {
	return spinRotation;
    }

    public boolean usingLeftArm() {
	return useLeftArm;
    }

    public float getOrbitRotation() {
	return orbitRotation;
    }

}
