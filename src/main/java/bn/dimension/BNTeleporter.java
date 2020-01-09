package bn.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class BNTeleporter implements ITeleporter
{
  public static final BNTeleporter TELEPORTER = new BNTeleporter();

  private BNTeleporter()
    {}

  private void putEntityAtSpawn (Entity e, World w)
    {
      BlockPos b = w.getSpawnPoint();
      while (!w.getBlockState(b).getBlock().equals(Blocks.AIR))
        b = b.up();
      putEntityAtBlockPos(e, b);
    }

  private void putEntityAtBlockPos (Entity e, BlockPos target)
    {
      e.setPosition(target.getX(), target.getY(), target.getZ());
    }

  @Override
  public void placeEntity (World world, Entity entity, float yaw)
    {
      // can't switch on constants so this has to be if/elseif
      int dim = entity.dimension;
      if (dim == 0)
        {
          // go to bed (or to spawn if you're not a player / have no bed)
          if (entity instanceof EntityPlayer)
            {
              EntityPlayer player = (EntityPlayer) entity;
              BlockPos target = player.getBedLocation();
              if (target != null)
                putEntityAtBlockPos(entity, target.up());
              else
                putEntityAtSpawn(entity, world);
            } else
            putEntityAtSpawn(entity, world);
        } else if (dim == BNInitWorldGen.PORT_ID)
        putEntityAtBlockPos(entity, new BlockPos(0, BNInitWorldGen.DEFAULT_FLOOR_POS + 1, 0));
      else
        System.err.println("BrazilianNight, Error: used BNTeleporter to go to non BNDimension!");
    }
}
