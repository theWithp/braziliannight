package braziliannight.entity;

import static net.minecraft.entity.SharedMonsterAttributes.KNOCKBACK_RESISTANCE;

import braziliannight.BN;
import braziliannight.BNRegistration;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BNThunderhead extends CreatureEntity implements BNEntity
{
  public BNThunderhead(EntityType<BNThunderhead> bully, World worldIn)
    {
      super(bully, worldIn);

    }

  public BNThunderhead(World w)
    {
      this(BNRegistration.THUNDERHEAD, w);
      BN.LOG.trace("BNThunderhead::new, OK!");

    }

  @Override
  public void tick ()
    {
      super.tick();
    }

  @Override
  protected void registerGoals ()
    {
      this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
      this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
      this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
      this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
      this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
      this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
      this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

  @Override
  public boolean attackEntityAsMob (Entity target)
    {
      if (super.attackEntityAsMob(target))
        {
          if (target instanceof LivingEntity)
            {
              double resist = 1;
              IAttributeInstance attrib = ((LivingEntity) target).getAttribute(KNOCKBACK_RESISTANCE);

              if (attrib != null)
                resist -= attrib.getValue();

              Vec3d dir = target.getPositionVector().subtract(this.getPositionVector()).normalize().mul(2 * resist,
                  2 * resist, 2 * resist);//TODO jasonize intensity
              target.addVelocity(dir.x, dir.y, dir.z);
              target.velocityChanged = true;
            }

          return true;
        }

      return false;
    }

  @Override
  public boolean attackEntityFrom (DamageSource source, float amount)
    {
      if (DamageSource.OUT_OF_WORLD.equals(source))
        {
          this.dead = true;
          return true;
        }

      if (amount <= 0)
        return false;
      Entity entity1 = source.getTrueSource();
      if (entity1 != null)
        {
          double d1 = entity1.posX - this.posX;

          double d0;
          for (d0 = entity1.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
            {
              d1 = (Math.random() - Math.random()) * 0.01D;
            }

          this.attackedAtYaw = (float) (MathHelper.atan2(d0, d1) * (180F / (float) Math.PI) - this.rotationYaw);
          this.knockBack(entity1, 0.4F, d1, d0);
        } else
        {
          this.attackedAtYaw = (int) (Math.random() * 2.0D) * 180;
        }
      return true;
    }

}
