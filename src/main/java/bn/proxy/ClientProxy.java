package bn.proxy;

import bn.item.DimMirror;
import bn.render.RenderAvocado;
import bn.render.boss.RenderWhiteWitch;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = bn.BNConstants.MOD_ID)
public class ClientProxy extends CommonProxy
{

  public ClientProxy()
    {
      MinecraftForge.EVENT_BUS.register(this);
    }

  @Override
  public void serverRegister ()
    {}

  @SubscribeEvent
  public void registerModels (ModelRegistryEvent ev)
    {
      RenderWhiteWitch.register();
      RenderAvocado.register();
      DimMirror.registerRender();
    }

  @Override
  public EntityLivingBase getLocalPlayer ()
    {
      return Minecraft.getMinecraft().player;
    }

}
