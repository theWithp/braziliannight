package braziliannight.block;

import braziliannight.BN;
import braziliannight.dimension.BNTeleport;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.common.thread.EffectiveSide;

public class WarpDoor extends Block
{
  public WarpDoor(Vec3i startPos, Vec3i endPos, ResourceLocation startDim, ResourceLocation endDim)
    {
      super(Block.Properties.create(Material.SHULKER).hardnessAndResistance(2F));
      pos1 = startPos;
      pos2 = endPos;
      dim1 = null;
      dim2 = null;
      dim1Str = startDim;
      dim2Str = endDim;
    }

  public WarpDoor()
    {
      this(null, new BlockPos(0, 34, 0), null, new ResourceLocation(BN.MODID, BN.DIMNAME));
    }

  @Override
  public boolean onBlockActivated (BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
      BlockRayTraceResult rts)
    {
      if (EffectiveSide.get().isClient())
        return false;
      if (dim1 == null)
        {
          if (dim1Str == null)
            dim1 = DimensionType.OVERWORLD;
          else
            dim1 = DimensionType.byName(dim1Str);
        }
      if (dim2 == null)
        {
          if (dim2Str == null)
            dim2 = DimensionType.OVERWORLD;
          else
            dim2 = DimensionType.byName(dim2Str);
        }

      boolean target2 = false;
      if (pos1 == null)
        pos1 = playerIn.getServer().getWorld(dim1).getSpawnPoint();
      if (pos2 == null)
        pos2 = playerIn.getServer().getWorld(dim2).getSpawnPoint();

      if (dim1 != dim2)
        {
          DimensionType curDim = playerIn.dimension;
          if (curDim.equals(dim1))
            target2 = true;
          else if (curDim.equals(dim2))
            target2 = false;
          //if they're in neither dimension do nothing I guess
          else
            return false;
        } else
        {
          Vec3i playerPos = playerIn.getPosition();
          if (playerPos.distanceSq(pos1) <= playerPos.distanceSq(pos2))
            target2 = true;
        }
      if (target2)
        BNTeleport.teleport((ServerPlayerEntity) playerIn, pos2, dim2);
      else
        BNTeleport.teleport((ServerPlayerEntity) playerIn, pos1, dim1);
      return true;

    }

  protected Vec3i pos1;
  protected Vec3i pos2;
  protected DimensionType dim1;
  protected DimensionType dim2;
  private ResourceLocation dim1Str;
  private ResourceLocation dim2Str;
}
