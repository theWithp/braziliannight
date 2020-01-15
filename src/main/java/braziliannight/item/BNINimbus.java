package braziliannight.item;

import javax.annotation.Nonnull;

import braziliannight.BrazilianNight;
import braziliannight.entity.BNNimbus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.EffectiveSide;

public class BNINimbus extends BNItem
{
  public BNINimbus()
    {
      super(new Properties().maxStackSize(1).group(BrazilianNight.modInstance.GROUP));
    }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick (World worldIn, PlayerEntity playerIn, Hand handIn)
    {
      if (EffectiveSide.get().isServer() && handIn.equals(Hand.MAIN_HAND))
        {
          BNNimbus sled = new BNNimbus(worldIn);
          BlockPos pos = playerIn.getPosition().add(playerIn.getHorizontalFacing().getDirectionVec());
          sled.setPosition(pos.getX(), pos.getY(), pos.getZ());
          worldIn.addEntity(sled);
          playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, ItemStack.EMPTY);
        }
      return pass(playerIn, handIn);
    }

  // subitems ???
}
