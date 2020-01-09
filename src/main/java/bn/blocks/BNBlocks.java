package bn.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

public class BNBlocks
{
  public static final IBlockState UNBREAKABLE_BLACK_CONCRETE = makeUBC();

  private static IBlockState makeUBC ()
    {
      // TODO still very not confident in the unbreakability here
      Block b = Blocks.CONCRETE.setBlockUnbreakable();
      return b.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLACK);
    }
}
