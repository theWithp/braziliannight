package braziliannight.item;

import javax.annotation.Nonnull;

import braziliannight.BrazilianNight;
import braziliannight.dimension.BNTeleport;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.EffectiveSide;

public class DimMirror extends BNItem
{

  public DimMirror()
    {
      super(new Properties().maxStackSize(1).group(BrazilianNight.modInstance.GROUP));
    }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick (World worldIn, PlayerEntity playerIn, Hand handIn)
    {
      if (EffectiveSide.get().isClient())
        return success(playerIn, handIn);
      BNTeleport.hubOverworldWormhole(worldIn, (ServerPlayerEntity) playerIn);
      return pass(playerIn, handIn);
    }
}
