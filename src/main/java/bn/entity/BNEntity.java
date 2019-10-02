package bn.entity;

import org.apache.commons.lang3.EnumUtils;

import bn.BNConstants;
import bn.state.BNEntityState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class BNEntity extends EntityMob
{
  // instance variables
  private int ticksInState = 0;

  // constants
  private static final DataParameter<String> MARSHALED_STATE = EntityDataManager.<String>createKey(BNEntity.class,
      DataSerializers.STRING);

  private static final int BOSS_RESIST_TIME = BNConstants.BOSS_RESIST_TIME;

  // constructor
  public BNEntity(World worldIn)
    {
      super(worldIn);
    }

  // initialization methods
  @Override
  protected void entityInit ()
    {
      super.entityInit();
      dataManager.register(MARSHALED_STATE, marshal(BNEntityState.IDLE));
    }

  // query methods

  protected BNEntityState getState ()
    {
      return unmarshal(dataManager.get(MARSHALED_STATE));
    }

  public int getTicksInCurrentAction ()
    {
      return ticksInState;
    }

  public boolean isSpinning ()
    {
      return getState() == BNEntityState.SPINNING;
    }

  public boolean isCasting ()
    {
      return getState() == BNEntityState.CASTING;
    }

  @Override
  public boolean isAIDisabled ()
    {
      return false;
    }

  protected abstract float modifyDamageAmount (DamageSource d, float amount);

  @Override
  public void onUpdate ()
    {
      super.onUpdate();
      ticksInState = getTicksInCurrentAction() + 1;
    }

  protected void setState (BNEntityState state)
    {
      // the client is not allowed to set entity state
      if (!world.isRemote && state != getState())
        ticksInState = 0;
      dataManager.set(MARSHALED_STATE, marshal(state));
    }

  // state transition and action methods
  @Override
  protected void applyEntityAttributes ()
    {
      super.applyEntityAttributes();
      getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(BNConstants.FOLLOW_RANGE);
    }

  @Override
  public boolean attackEntityFrom (DamageSource d, float amount)
    {

      float newAmount = modifyDamageAmount(d, amount);
      if (newAmount <= 0)
        {
          heal(-newAmount);
          return false;
        }
      if (!super.attackEntityFrom(d, newAmount))
        return false;
      hurtResistantTime = BOSS_RESIST_TIME;
      return true;
    }

  // utility methods
  private static String marshal (BNEntityState state)
    {
      return state.name();
    }

  private static BNEntityState unmarshal (String s)
    {
      return EnumUtils.getEnum(BNEntityState.class, s);
    }
}
