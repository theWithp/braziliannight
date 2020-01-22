package braziliannight.item;

import javax.annotation.Nonnull;

import braziliannight.BN;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.EffectiveSide;

public class CharmXyz extends BNItem
{
  public CharmXyz()
    {
      super(new Properties().maxStackSize(1).group(BN.GROUP));
      BN.LOG.trace("DimMirror::new, OK!");

    }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick (World worldIn, PlayerEntity playerIn, Hand handIn)
    {
      if (EffectiveSide.get().isClient())
        return pass(playerIn, handIn);
      int chunkx = playerIn.chunkCoordX;
      int chunkz = playerIn.chunkCoordZ;
      BlockPos pPos = playerIn.getPosition();
      int innerX = pPos.getX() - chunkx * 16;
      int innerZ = pPos.getZ() - chunkz * 16;
      int y = pPos.getY();
      StringBuilder b = new StringBuilder();
      b.append("Chunk: (");
      b.append(chunkx);
      b.append(", ");
      b.append(chunkz);
      b.append(") Pos in chunk: (");
      b.append(innerX);
      b.append(", ");
      b.append(y);
      b.append(", ");
      b.append(innerZ);
      b.append(")");

      playerIn.sendMessage(new StringTextComponent(b.toString()));
      return success(playerIn, handIn);
    }

}
