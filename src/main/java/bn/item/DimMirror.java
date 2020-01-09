package bn.item;

import javax.annotation.Nonnull;

import bn.dimension.BNTeleporter;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = bn.BNConstants.MOD_ID)
public class DimMirror extends BNItem
{
  private static final int PORT_ID = bn.dimension.BNInitWorldGen.PORT_ID;
  private static final String NAME = "dimmirror";
  private static DimMirror DIM_MIRROR;

  public static void init ()
    {
      DIM_MIRROR = new DimMirror();
    }

  private DimMirror()
    {
      super(NAME);
      this.setCreativeTab(CreativeTabs.MISC);
    }

  // TODO: make sure this doesn't make nether portals, if it does implement our
  // own ITeleporter
  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick (World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
      if (worldIn.isRemote)
        return success(playerIn, handIn);
      if (playerIn.dimension == PORT_ID)
        {
          playerIn.changeDimension(0, BNTeleporter.TELEPORTER);
        } else
        {
          playerIn.changeDimension(PORT_ID, BNTeleporter.TELEPORTER);
        }
      return pass(playerIn, handIn);
    }

  @SubscribeEvent
  public static void registerItems (RegistryEvent.Register<Item> ev)
    {
      ev.getRegistry().register(DIM_MIRROR);
    }

  public static void registerRender ()
    {

      ModelLoader.setCustomModelResourceLocation(DIM_MIRROR, 0,
          new ModelResourceLocation(DIM_MIRROR.getRegistryName(), "inventory"));
    }
}
