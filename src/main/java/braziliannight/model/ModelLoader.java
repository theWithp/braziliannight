package braziliannight.model;

import static braziliannight.BN.MODID;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModelLoader
{

  public static Map<String, EntityType<?>> creatures = new HashMap<>();

  public void addCreature (String name, EntityType<?> type)
    {
      creatures.put(name, type);
    }

  @SubscribeEvent
  public static void clientSetup (final FMLClientSetupEvent event)
    {
      for (EntityType<?> type : creatures.values())
        {

        }
    }
}