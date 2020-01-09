package bn.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

public class BNItem extends Item
{
  protected BNItem(String name)
    {
      super();
      this.setUnlocalizedName(name);
      this.setRegistryName(name);
    }

  // convenience methods to make onItemRightClick implementations cleaner
  protected ActionResult<ItemStack> success (EntityPlayer user, EnumHand handIn)
    {
      return new ActionResult<>(EnumActionResult.SUCCESS, user.getHeldItem(handIn));
    }

  protected ActionResult<ItemStack> pass (EntityPlayer user, EnumHand handIn)
    {
      return new ActionResult<>(EnumActionResult.PASS, user.getHeldItem(handIn));
    }
}
