package braziliannight.dimension;

import braziliannight.BrazilianNight;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlayEntityEffectPacket;
import net.minecraft.network.play.server.SPlaySoundEventPacket;
import net.minecraft.network.play.server.SPlayerAbilitiesPacket;
import net.minecraft.network.play.server.SRespawnPacket;
import net.minecraft.network.play.server.SServerDifficultyPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

public class BNTeleport
{
  public static void changeDim (ServerPlayerEntity player, BlockPos pos, DimensionType type)
    { // copy from ServerPlayerEntity#changeDimension
      if (!ForgeHooks.onTravelToDimension(player, type))
        return;

      DimensionType dimensiontype = player.dimension;

      ServerWorld serverworld = player.server.getWorld(dimensiontype);
      player.dimension = type;
      ServerWorld serverworld1 = player.server.getWorld(type);
      WorldInfo worldinfo = player.world.getWorldInfo();
      player.connection
          .sendPacket(new SRespawnPacket(type, worldinfo.getGenerator(), player.interactionManager.getGameType()));
      player.connection
          .sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
      PlayerList playerlist = player.server.getPlayerList();
      playerlist.updatePermissionLevel(player);
      serverworld.removeEntity(player, true);
      player.revive();
      float f = player.rotationPitch;
      float f1 = player.rotationYaw;

      player.setLocationAndAngles(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);
      serverworld.getProfiler().endSection();
      serverworld.getProfiler().startSection("placing");
      player.setLocationAndAngles(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);

      serverworld.getProfiler().endSection();
      player.setWorld(serverworld1);
      serverworld1.func_217447_b(player);
      player.connection.setPlayerLocation(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, f1, f);
      player.interactionManager.setWorld(serverworld1);
      player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
      playerlist.sendWorldInfo(player, serverworld1);
      playerlist.sendInventory(player);

      for (EffectInstance effectinstance : player.getActivePotionEffects())
        {
          player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectinstance));
        }

      player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
      BasicEventHooks.firePlayerChangedDimensionEvent(player, dimensiontype, type);
    }

  public static boolean hubOverworldWormhole (World w, ServerPlayerEntity p)
    {
      if (!w.isRemote)
        {
          // Go To Portallis
          if (w.getDimension().getType().equals(DimensionType.OVERWORLD))
            {
              if (DimensionType.byName(BrazilianNight.DIM_LOC) == null)
                {
                  DimensionManager.registerDimension(BrazilianNight.DIM_LOC, BrazilianNight.DIMENSION, null, true);
                }
              changeDim(p, new BlockPos(0, 33, 0), DimensionType.byName(BrazilianNight.DIM_LOC));
            }
          // Go to Spawn (bed happens later)
          else if (w.getDimension().getType().equals(DimensionType.byName(BrazilianNight.DIM_LOC)))
            {
              World overWorld = w.getServer().getWorld(DimensionType.OVERWORLD);
              BlockPos pos = overWorld.getSpawnPoint();
              for (int i = 0; i < overWorld.getMaxHeight(); i++)
                {
                  if (w.getBlockState(pos).equals(Blocks.AIR.getDefaultState()))
                    break;
                  pos = pos.up();
                }
              for (int i = 0; i < overWorld.getMaxHeight(); i++)
                {
                  BlockPos below = pos.down();
                  if (w.getBlockState(below).equals(Blocks.AIR.getDefaultState()))
                    pos = below;
                  else
                    break;
                }
              changeDim(p, pos, DimensionType.OVERWORLD);
            }

          return true;
        }
      return false;
    }
}