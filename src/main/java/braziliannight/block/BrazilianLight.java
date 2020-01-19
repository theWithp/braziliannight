package braziliannight.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BrazilianLight extends Block
{

  public BrazilianLight()
    {
      super(Block.Properties.create(Material.BARRIER).lightValue(15).doesNotBlockMovement()
          .hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
    }

  @Override
  public BlockRenderType getRenderType (BlockState state)
    {
      return BlockRenderType.INVISIBLE;
    }

  @Override
  public boolean isSolid (BlockState state)
    {
      return false;
    }

  @Override
  public boolean canEntitySpawn (BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type)
    {
      return false;
    }

}
