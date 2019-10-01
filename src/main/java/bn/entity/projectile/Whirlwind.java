package bn.entity.projectile;

import java.util.HashMap;
import java.util.Map;

import bn.BNConstants;
import bn.entity.boss.BNBossEntity;
import bn.magic.PlayerEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Whirlwind extends EntityFireball
{
  // instance variables
  private Map<Entity, Integer> hitCooldown = new HashMap<>();
  private Vec3d target;
  private int ticksAlive = 0;

  // constants
  private static final double RANGE = BNConstants.WHIRLWIND_TARGET_RANGE;
  private static final int COOLDOWN = BNConstants.WHIRLWIND_COOLDOWN;
  private static final int PROBABILITY = BNConstants.WHIRLWIND_PROBABILITY;
  private static final double SPEED = BNConstants.WHIRLWIND_SPEED;
  private static final int LIFETIME = BNConstants.WHIRLWIND_LIFETIME;

  // constructors
  public Whirlwind(World w)
    {
      super(w);
    }

  public Whirlwind(World w, Vec3d posVec)
    {
      this(w);
      setLocationAndAngles(posVec.x, posVec.y, posVec.z, rotationYaw, rotationPitch);
      setPosition(posVec.x, posVec.y, posVec.z);
    }

  // state transition and action methods
  private void tick ()
    {
      ticksAlive++;
      for (Entity e : hitCooldown.keySet())
        {
          int current = hitCooldown.get(e) - 1;
          if (current < 0)
            current = 0;
          hitCooldown.put(e, current);
        }
    }

  @Override
  public void onUpdate ()
    {
      tick();
      if (target == null || getPositionVector().squareDistanceTo(target) < 2)
        retarget();
      if (target == null)
        return;
      Vec3d travelDirection = getPositionVector().subtract(target).normalize().scale(SPEED);
      motionX += travelDirection.x;
      motionY += travelDirection.y;
      motionZ += travelDirection.z;
      super.onUpdate();
      if (ticksAlive > LIFETIME)
        setDead();
    }

  private void retarget ()
    {
      Entity closest = null;
      for (Entity p : world.getEntitiesWithinAABB(EntityLivingBase.class,
          getEntityBoundingBox().expand(RANGE, RANGE, RANGE)))
        {
          if (p instanceof BNBossEntity)
            continue; // note that projectiles are not subtyped from
                      // EntityLivingBase so we don't have
          // to check for those
          if (closest == null || getPositionVector().squareDistanceTo(target) > getPositionVector()
              .squareDistanceTo(p.getPositionVector()))
            {
              target = p.getPositionVector();
              closest = p;
            }
        }
    }

  @Override
  public boolean attackEntityFrom (DamageSource source, float amnt)
    {
      return false;
    }

  @Override
  protected void onImpact (RayTraceResult result)
    {
      if (world.isRemote)
        return;
      if (result.entityHit == null)
        return;
      if (!hitCooldown.containsKey(result.entityHit))
        {
          hitCooldown.put(result.entityHit, 0);
        } else if (hitCooldown.get(result.entityHit) > 0)
        return;

      if (result.entityHit instanceof EntityPlayer)
        {
          if (rand.nextInt(100) < PROBABILITY)
            PlayerEffects.knockOffArmor((EntityPlayer) result.entityHit, world);
        }

      // if we wanted to we could attribute the damage to our master here (if we
      // even
      // retained that info)
      result.entityHit.attackEntityFrom(DamageSource.MAGIC, 2);
      float x = rand.nextFloat() * 0.2f;
      float z = rand.nextFloat() * 0.2f;
      result.entityHit.addVelocity(x, 0.8, z);
      result.entityHit.fallDistance = 0;
      hitCooldown.put(result.entityHit, COOLDOWN);
    }

}
