package bn.proxy;

import net.minecraft.entity.EntityLivingBase;

public class CommonProxy {

    public void clientRegister() {
    }

    // if we had any server only events we'd register ourself with the event handler
    // here
    public void serverRegister() {
    }

    public EntityLivingBase getLocalPlayer() {
	return null;// nonono
    }
}
