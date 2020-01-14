package braziliannight.block;

import braziliannight.BrazilianNight;
import braziliannight.dimension.BNTeleport;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DimDoor extends DoorBlock
{

  public DimDoor()
    {
      super(Block.Properties.create(Material.PORTAL).hardnessAndResistance(2F));

    }

  @Override
  public boolean isValidPosition (BlockState state, IWorldReader worldIn, BlockPos pos)
    {
      return (worldIn.getDimension().getType().getId() == DimensionType.OVERWORLD.getId()
          || worldIn.getDimension().getType() == DimensionType.byName(BrazilianNight.DIM_LOC)
              && super.isValidPosition(state, worldIn, pos));
    }

  @Override
  public boolean onBlockActivated (BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
      BlockRayTraceResult rts)
    {
      return worldIn.isRemote ? false : BNTeleport.hubOverworldWormhole(worldIn, (ServerPlayerEntity) playerIn);
    }
}
