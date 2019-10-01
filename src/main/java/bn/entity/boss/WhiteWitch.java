package bn.entity.boss;

import java.util.List;
import java.util.Map;

import bn.BNConstants;
import bn.entity.projectile.Whirlwind;
import bn.state.BNEntityState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;

public class WhiteWitch extends BNBossEntity
{
  // instance variables used for animation
  private boolean useLeftArm = false;
  private float spinRotation = 0;
  private float orbitRotation = 0;
  // instance variables used for decision making
  private int whirlwindCooldown = 0;
  private int hurricaneCooldown = 0;

  // constant declarations
  private static final Map<String, Integer> WHITE_WITCHI = BNConstants.WHITE_WITCHI;
  private static final Map<String, Double> WHITE_WITCHD = BNConstants.WHITE_WITCHD;
  private static final Map<String, Float> WHITE_WITCHF = BNConstants.WHITE_WITCHF;

  private static final double MOVE_SPEED = WHITE_WITCHD.get("MOVE_SPEED");
  private static final int SHOT_CLOCK = WHITE_WITCHI.get("SHOT_CLOCK");
  private static final float ATTACK_RANGE = WHITE_WITCHF.get("ATTACK_RANGE");
  private static final double MAX_HEALTH = WHITE_WITCHD.get("MAX_HEALTH");
  private static final float WIDTH = WHITE_WITCHF.get("WIDTH");
  private static final float HEIGHT = WHITE_WITCHF.get("HEIGHT");
  private static final int ARMOR = WHITE_WITCHI.get("ARMOR");
  private static final float MAGIC_DAMAGE_MULT = WHITE_WITCHF.get("MAGIC_DAMAGE_MULT");
  private static final int WHIRLWIND_RATE = WHITE_WITCHI.get("WHIRLWIND_RATE");
  private static final int HURRICANE_RATE = WHITE_WITCHI.get("HURRICANE_RATE");
  private static final float HURRICANE_FACTOR = WHITE_WITCHF.get("HURRICANE_FACTOR");
  private static final double HURRICANE_SIZE = WHITE_WITCHD.get("HURRICANE_SIZE");

  // constructors
  public WhiteWitch(World w)
    {
      super(w);
      this.setSize(WIDTH, HEIGHT);
    }

  // initialization methods
  @Override
  protected void initEntityAI ()
    {
      super.initEntityAI();
      tasks.addTask(2, new EntityAILookIdle(this));
      tasks.addTask(1, new EntityAIWanderAvoidWater(this, MOVE_SPEED));
      tasks.addTask(0, new EntityAIAttackRanged(this, MOVE_SPEED, SHOT_CLOCK, ATTACK_RANGE));
    }

  // query methods
  @Override
  protected void applyEntityAttributes ()
    {
      super.applyEntityAttributes();
      getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
    }

  @Override
  public int getTotalArmorValue ()
    {
      return ARMOR;
    }

  @Override
  public boolean canBePushed ()
    {
      // you cannot push spinning airman
      return getState() != BNEntityState.SPINNING;
    }

  @Override
  protected Color getBarColor ()
    {
      return Color.WHITE;
    }

  public float getSpinRotation ()
    {
      return spinRotation;
    }

  public boolean usingLeftArm ()
    {
      return useLeftArm;
    }

  public float getOrbitRotation ()
    {
      return orbitRotation;
    }

  @Override
  protected float modifyDamageAmount (DamageSource source, float amnt)
    {
      if (source.isMagicDamage())
        return amnt * MAGIC_DAMAGE_MULT;
      return amnt;
    }

  // state transition and action methods
  @Override
  public void attackEntityWithRangedAttack (EntityLivingBase target, float distanceFactor)
    {
      getLookHelper().setLookPositionWithEntity(target, 30, 30);

      if (whirlwindCooldown-- <= 0)
        {
          // if we had a whirlwind attack noise we'd play it here (note that
          // they do their
          // own target lock on thing)
          Whirlwind shot = new Whirlwind(world, getPositionVector());
          world.spawnEntity(shot);
          whirlwindCooldown = WHIRLWIND_RATE;
          setState(BNEntityState.CASTING);
          if (getTicksInCurrentAction() > 23)
            setState(BNEntityState.IDLE);
        } else if (hurricaneCooldown-- <= 0)
        {
          hurricaneCooldown = HURRICANE_RATE;
          boolean successful = hurricaneAttack(target);
          if (successful)
            {
              setState(BNEntityState.SPINNING);
              whirlwindCooldown = WHIRLWIND_RATE;
            }
        }
    }

  @Override
  public void setState (BNEntityState state)
    {
      super.setState(state);
      spinRotation = 0;
      if (getState() == BNEntityState.CASTING)
        useLeftArm = usingLeftArm() ^ true;
    }

  /*
   * think this will need to be refactored to put the orbit and spin into the
   * model code as it is my understanding that onUpdate is server only
   */
  @Override
  public void onUpdate ()
    {
      orbitRotation = (orbitRotation + 2) % 360;
      if (getState() == BNEntityState.SPINNING)
        {
          if (getTicksInCurrentAction() > 160)
            setState(BNEntityState.IDLE);
          else
            spinRotation = (spinRotation - 40) % 360;
        }
      if (motionY < 0)
        motionY *= 0.89999f; // feather fall effect
      super.onUpdate();
    }

  // you cannot fall damage air man
  @Override
  public void fall (float damage, float mult)
    {}

  // this is basically an energy field that also causes things to get thrown in
  // various directions (it should probably have a visual indicator of some
  // sort)
  private boolean hurricaneAttack (EntityLivingBase target)
    {
      boolean hadATarget = false;

      AxisAlignedBB range = getEntityBoundingBox();
      range = range.expand(HURRICANE_SIZE, HURRICANE_SIZE, HURRICANE_SIZE);

      List<EntityLivingBase> inField = world.getEntitiesWithinAABB(EntityLivingBase.class, range);

      for (EntityLivingBase e : inField)
        {
          if (e instanceof WhiteWitch)
            continue;
          hadATarget = true;
          Vec3d movement = getPositionVector().subtract(e.getPositionVector()).normalize();
          float factor = HURRICANE_FACTOR;
          e.addVelocity(movement.x * factor, movement.y * factor, movement.z * factor);
          // packet to force player motion went here. see if really needed
          e.fallDistance = 0; // they've suffered enough.
        }

      return hadATarget;
    }

  // TODO
  @Override
  public void dropFewItems (boolean wasHit, int lootingMod)
    {}

  // This method is required but does not do anything.
  @Override
  public void setSwingingArms (boolean swingingArms)
    {}
}
