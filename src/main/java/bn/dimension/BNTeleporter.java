package bn.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class BNTeleporter implements ITeleporter
{
  public static final BNTeleporter TELEPORTER = new BNTeleporter();

  private BNTeleporter()
    {}

  @Override
  public void placeEntity (World world, Entity entity, float yaw)
    {
      entity.posX = 0;
      entity.posZ = 0;
      int floor = 80;
      while ((world.getBlockState(new BlockPos(0, 0, floor)).getBlock() == Blocks.AIR) && floor > 0)
        floor--;
      entity.posY = floor;
    }

}
