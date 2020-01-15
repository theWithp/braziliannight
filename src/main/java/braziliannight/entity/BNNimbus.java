package braziliannight.entity;

import braziliannight.BNRegistration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.EffectiveSide;

public class BNNimbus extends BNEntity
{

  public BNNimbus(EntityType<BNNimbus> entNimbus, World worldIn)
    {
      super(entNimbus, worldIn);

      // step height?
    }

  public BNNimbus(World w)
    {
      this(BNRegistration.NIMBUS, w);
    }

  @Override
  public void tick ()
    {
      this.stepHeight = 1.02f; // ????

      if (EffectiveSide.get().isClient())
        {
          // particle effect if we're in the air
        }
      super.tick();
    }

  @Override
  public ActionResultType applyPlayerInteraction (PlayerEntity player, Vec3d vec, Hand hand)
    {
      if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof PlayerEntity
          && this.getControllingPassenger() != player)
        return ActionResultType.SUCCESS;
      // process interact
      if (EffectiveSide.get().isServer())
        {
          if (player.isSneaking())
            {
              ItemEntity drop = new ItemEntity(world, posX, posY, posZ, new ItemStack(BNRegistration.I_NIMBUS));
              world.addEntity(drop);
              this.remove();
            } else
            {
              player.startRiding(this);
            }
        }
      return ActionResultType.SUCCESS;
    }

  @Override
  public void updateRidden ()
    {
      if (this.isBeingRidden())
        {
          this.getControllingPassenger().setPosition(this.posX,
              this.posY + this.getMountedYOffset() + this.getControllingPassenger().getYOffset(), this.posZ);
        }
    }

  @Override
  public Entity getControllingPassenger ()
    {
      return getPassengers().isEmpty() ? null : getPassengers().get(0);
    }

  @Override
  public double getMountedYOffset ()
    {
      return 1.6f; // TODO jsonize
    }

  @Override
  public void fall (float distance, float damageMultiplier)
    {}

  @Override
  public AxisAlignedBB getCollisionBox (Entity par1Entity)
    {
      return null;
    }

  @Override
  public boolean attackEntityFrom (DamageSource source, float amount)
    {
      if (this.getControllingPassenger() != null
          && !this.getControllingPassenger().isEntityEqual(source.getTrueSource()))
        this.getControllingPassenger().stopRiding();
      return false;
    }

  @Override
  public void travel (Vec3d p_213352_1_)
    {
      // I asssuuuuume this is what par1/par2 were...
      double par1 = p_213352_1_.getX();
      double par2 = p_213352_1_.getZ();

      if (this.getControllingPassenger() != null)
        {
          this.prevRotationYaw = this.rotationYaw = this.getControllingPassenger().rotationYaw;
          this.rotationPitch = this.getControllingPassenger().rotationPitch * 0.5F;
          this.setRotation(this.rotationYaw, this.rotationPitch);
          this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
          par1 = ((LivingEntity) this.getControllingPassenger()).moveStrafing * 0.5F;
          par2 = ((LivingEntity) this.getControllingPassenger()).moveForward;

          if (par2 <= 0.0F)
            {
              par2 *= 0.25F;
            }

          this.stepHeight = 1.0F;
          this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

          if (EffectiveSide.get().isServer())
            {
              par2 *= 0.06f;
              if (par1 != 0)
                {
                  double f4 = Math.sin(this.rotationYaw * Math.PI / 180.0F);
                  double f5 = Math.cos(this.rotationYaw * Math.PI / 180.0F);
                  addVelocity((par1 * f5 - par2 * f4) * 0.06, 0, (par2 * f5 + par1 * f4) * 0.06);
                }
              addVelocity(-Math.sin(Math.toRadians(this.rotationYaw)) * par2,
                  -Math.sin(Math.toRadians(this.rotationPitch)) * par2,
                  Math.cos(Math.toRadians(this.rotationYaw)) * par2);
            }
        } else
        {
          Vec3d oldVel = this.getMotion();
          float newY;
          if (!this.onGround && !this.isInWater())
            newY = -0.1f;
          else
            newY = 0f;
          setVelocity(oldVel.x * 0.7f, newY, oldVel.z * 0.7f);
        }

      // if (this.getControllingPassenger() != null)
      // this.setSize(0.5f, 3); can no longer dynamically set size soooo....
      this.move(MoverType.SELF, this.getMotion());
      // if (this.getControllingPassenger() != null)
      // this.setSize(0.5f, 1); same thing no magic resizing

      double f2 = 0.91;
      Vec3d oldVel = this.getMotion();
      this.setVelocity(oldVel.getX() * f2, oldVel.getY() * f2, oldVel.getZ() * f2);
    }
}
