package braziliannight.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class Killplane extends Block
{

  public Killplane()
    {
      super(Block.Properties.create(Material.BARRIER).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
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
  public void onEntityCollision (BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
      entityIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, 10000); //DIE DAMN YOU
    }

  @Override
  public void onFallenUpon (World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
      super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
      entityIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, 10000); //DIE DAMN YOU
    }

  @Override
  public void onLanded (IBlockReader worldIn, Entity entityIn)
    {
      super.onLanded(worldIn, entityIn);
      entityIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, 10000); //DIE DAMN YOU
    }

}