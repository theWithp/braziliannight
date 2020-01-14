package braziliannight.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class BNItem extends Item
{

  public BNItem(Properties properties)
    {
      super(properties);
    }

  protected ActionResult<ItemStack> success (PlayerEntity user, Hand handIn)
    {
      return new ActionResult<>(ActionResultType.SUCCESS, user.getHeldItem(handIn));
    }

  protected ActionResult<ItemStack> pass (PlayerEntity user, Hand handIn)
    {
      return new ActionResult<>(ActionResultType.PASS, user.getHeldItem(handIn));
    }
}
