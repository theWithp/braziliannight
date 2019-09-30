package bn.magic;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PlayerEffects {

    // if we wanted to we could extend this to be able to knock baubles off too...
    public static void knockOffArmor(EntityPlayer target, World w) {
	int slot = w.rand.nextInt(4);
	if (target.inventory.armorInventory.get(slot).isEmpty())
	    return; // nothing to knock off there
	ItemStack armr = target.inventory.armorInventory.get(slot).copy();
	if (!target.inventory.addItemStackToInventory(armr)) {
	    EntityItem knocked = new EntityItem(w, target.posX, target.posY, target.posZ, armr);
	    knocked.setVelocity(w.rand.nextDouble() * 0.2 - 0.1, w.rand.nextDouble() * 0.2 - 0.1,
		    w.rand.nextDouble() * 0.2 - 0.1);
	    w.spawnEntity(knocked);
	}
    }
}
