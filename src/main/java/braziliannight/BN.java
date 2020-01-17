package braziliannight;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(BN.MODID)
public class BN
{

  public static final String MODID = "braziliannight";
  public static final String DIMNAME = "portallis";
  public static final ResourceLocation DIM_LOC = new ResourceLocation(MODID, "portallis");
  public static final Logger LOG = LogManager.getLogger(BN.class);

  public static final ItemGroup GROUP = new ItemGroup(MODID)
    {
      @Override
      public ItemStack createIcon ()
        {
          return new ItemStack(Blocks.BLACK_CONCRETE);
        }
    };

  public BN()
    {
      LOG.trace("BN::new, OK!");
    }
}
