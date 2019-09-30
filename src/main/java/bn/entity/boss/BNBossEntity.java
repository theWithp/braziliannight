package bn.entity.boss;

import static bn.BNConstants.DEFAULT_STEP_HEIGHT;

import bn.BNConstants;
import bn.entity.BNEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class BNBossEntity extends BNEntity implements IEntityMultiPart, IRangedAttackMob {
    private MultiPartEntityPart[] parts;
    private BossInfoServer bossInfo;

    public BNBossEntity(World w) {
	super(w);
	if (w != null) { // not sure if this check is actually needed or w != null is just part of the
			 // contract
	    bossInfo = new BossInfoServer(getDisplayName(), getBarColor(), BossInfo.Overlay.PROGRESS);
	}
	stepHeight = DEFAULT_STEP_HEIGHT;
    }

    // the point of this is that bosses should have big hurtboxes and still fit
    // through doors, but who knows what it actually does...
    @Override
    public void setSize(float w, float h) {
	if (parts == null) {
	    parts = new MultiPartEntityPart[] { new MultiPartEntityPart(this, "defaultBody", w, h) {
		@Override
		public void onUpdate() {
		    super.onUpdate();
		    isDead = ((Entity) parent).isDead;
		}

		@Override
		public boolean shouldRenderInPass(int pass) {
		    return false;
		}
	    } };
	} else
	    super.setSize(w, h);
    }

    @Override
    public boolean isNonBoss() {
	return false;
    }

    protected abstract BossInfo.Color getBarColor();

    @Override
    public World getWorld() {
	return world;
    }

    @Override
    protected float modifyDamageAmount(DamageSource d, float amount) {
	if (d == DamageSource.IN_WALL)
	    return 0;
	if (amount > BNConstants.BOSS_HURT_CAP)
	    amount = BNConstants.BOSS_HURT_CAP;
	return amount;
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
	return attackEntityFrom(source, damage);
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
	super.addTrackingPlayer(player);
	if (bossInfo != null)
	    bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
	super.removeTrackingPlayer(player);
	if (bossInfo != null)
	    bossInfo.removePlayer(player);
    }

    @Override
    public boolean canBeLeashedTo(EntityPlayer p) {
	return false;
    }

    @Override
    public void onUpdate() {
	if (parts != null && parts[0] != null && parts[0].partName == "defaultBody") {
	    parts[0].setPosition(posX, posY, posZ);
	    if (!world.isRemote)
		parts[0].setVelocity(motionX, motionY, motionZ);// this used to be inverted, test it.
	    if (!parts[0].addedToChunk)
		world.spawnEntity(parts[0]);
	}
	// there was some client side only visibility voodoo here. removed for now.
	if (bossInfo != null)
	    bossInfo.setPercent(getHealth() / getMaxHealth());
    }

}
