package braziliannight;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(BrazilianNight.MODID)
public class BrazilianNight
{

  public static BrazilianNight modInstance;
  public static final String MODID = "braziliannight";
  public static final String DIMNAME = "portallis";
  public static final ResourceLocation DIM_LOC = new ResourceLocation(MODID, "portallis");

  public final ItemGroup GROUP = new ItemGroup(MODID)
    {
      @Override
      public ItemStack createIcon ()
        {
          return new ItemStack(Blocks.BLACK_CONCRETE);
        }
    };

  public BrazilianNight()
    {
      modInstance = this;
    }
}
