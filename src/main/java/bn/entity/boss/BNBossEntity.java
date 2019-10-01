package bn.entity.boss;

import bn.BNConstants;
import bn.entity.BNEntity;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class BNBossEntity extends BNEntity implements IRangedAttackMob
{
  // instance variables
  private BossInfoServer bossInfo;

  // constants
  private static final float DEFAULT_STEP_HEIGHT = BNConstants.DEFAULT_STEP_HEIGHT;
  private static final float BOSS_HURT_CAP = BNConstants.BOSS_HURT_CAP;

  // constructor
  public BNBossEntity(World w)
    {
      super(w);
      bossInfo = new BossInfoServer(getDisplayName(), getBarColor(), BossInfo.Overlay.PROGRESS);
      stepHeight = DEFAULT_STEP_HEIGHT;
    }

  // initialization methods
  @Override
  protected void initEntityAI ()
    {
      super.initEntityAI();
      // we want to target players for sure.
      // chicken targeting is just for current testing.
      targetTasks.addTask(2,
          new EntityAINearestAttackableTarget<EntityChicken>(this, EntityChicken.class, false, false));
      targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, false, false));
      targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));

    }

  // query methods
  @Override
  public boolean isNonBoss ()
    {
      return false;
    }

  @Override
  public boolean canBeCollidedWith ()
    {
      return false;
    }

  @Override
  public boolean canDespawn ()
    {
      return false;
    }

  @Override
  protected float modifyDamageAmount (DamageSource d, float amount)
    {
      if (d == DamageSource.IN_WALL)
        return 0;
      if (amount > BOSS_HURT_CAP)
        return BOSS_HURT_CAP;
      return amount;
    }

  @Override
  public boolean canBeLeashedTo (EntityPlayer p)
    {
      return false;
    }

  protected abstract BossInfo.Color getBarColor ();

  // state transition and action methods
  @Override
  public void addTrackingPlayer (EntityPlayerMP player)
    {
      super.addTrackingPlayer(player);
      if (bossInfo != null)
        bossInfo.addPlayer(player);
    }

  @Override
  public void removeTrackingPlayer (EntityPlayerMP player)
    {
      super.removeTrackingPlayer(player);
      if (bossInfo != null)
        bossInfo.removePlayer(player);
    }

  @Override
  public void onUpdate ()
    {
      if (bossInfo != null)
        bossInfo.setPercent(getHealth() / getMaxHealth());
      super.onUpdate();
    }

}
