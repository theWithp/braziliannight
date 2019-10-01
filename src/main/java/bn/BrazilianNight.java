package bn;

import static bn.BNConstants.CLIENT_PROXY_CLASS;
import static bn.BNConstants.MC_VERSIONS;
import static bn.BNConstants.MOD_ID;
import static bn.BNConstants.MOD_NAME;
import static bn.BNConstants.SERVER_PROXY_CLASS;
import static bn.BNConstants.VERSION;

import bn.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, acceptedMinecraftVersions = MC_VERSIONS)
public class BrazilianNight
{

  @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = SERVER_PROXY_CLASS)
  public static CommonProxy proxy;

  // Registration of models happens in clientProxy, registration of entities
  // happens in the static EntityInit class
  @EventHandler
  public void load (FMLInitializationEvent ev)
    {
      proxy.serverRegister();
      proxy.clientRegister();
    }
}
