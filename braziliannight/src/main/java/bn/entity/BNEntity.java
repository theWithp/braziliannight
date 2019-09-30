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

public abstract class BNEntity extends EntityMob {

    public BNEntity(World worldIn) {
	super(worldIn);
    }

    private int ticksInState = 0;
    private static final DataParameter<String> MARSHALED_STATE = EntityDataManager.<String>createKey(BNEntity.class,
	    DataSerializers.STRING);

    private static String marshal(BNEntityState state) {
	return state.name();
    }

    private static BNEntityState unmarshal(String s) {
	return EnumUtils.getEnum(BNEntityState.class, s);
    }

    @Override
    public boolean isAIDisabled() {
	return false;
    }

    @Override
    protected void entityInit() {
	super.entityInit();
	dataManager.register(MARSHALED_STATE, marshal(BNEntityState.IDLE));
    }

    @Override
    public void onUpdate() {
	super.onUpdate();
	ticksInState = getTicksInCurrentAction() + 1;
    }

    protected BNEntityState getState() {
	return unmarshal(dataManager.get(MARSHALED_STATE));
    }

    protected void setState(BNEntityState state) {
	// the client is not allowed to set entity state
	if (!world.isRemote && state != getState())
	    ticksInState = 0;
	dataManager.set(MARSHALED_STATE, marshal(state));
    }

    protected void applyEntityAttributes(double maxHealth) {
	super.applyEntityAttributes();
	getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(BNConstants.FOLLOW_RANGE);
	getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
    }

    @Override
    public boolean attackEntityFrom(DamageSource d, float amount) {
	amount = modifyDamageAmount(d, amount);
	if (amount <= 0) {
	    heal(-amount);
	    return false;
	}
	return super.attackEntityFrom(d, amount);
    }

    protected abstract float modifyDamageAmount(DamageSource d, float amount);

    public int getTicksInCurrentAction() {
	return ticksInState;
    }

    public boolean isSpinning() {
	return getState() == BNEntityState.SPINNING;
    }

    public boolean isCasting() {
	return getState() == BNEntityState.CASTING;
    }

}
