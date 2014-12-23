package mytown.core;

import java.io.File;

import net.minecraftforge.fml.relauncher.Side;
import mytown.core.utils.Log;
import mytown.core.utils.config.ConfigProcessor;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

@Mod(modid = "MyTownCore", name = "MyTownCore", version = "2.0", dependencies = "required-after:Forge", acceptableRemoteVersions = "*")
public class MyTownCore {
	@Instance("MyTownCore")
	public static MyTownCore Instance;
	public static boolean IS_MCPC = false;

	public Log log;
	public Configuration config;

	@EventHandler
	public void preinit(FMLPreInitializationEvent ev) {
		log = new Log(ev.getModLog());

		// Load Configs
		config = new Configuration(new File(ev.getModConfigurationDirectory(), "/MyTown/Core.cfg"));
		ConfigProcessor.load(config, Config.class);
		config.save();

		// Register handlers/trackers
		FMLCommonHandler.instance().bus().register(new PlayerTracker());
	}

	@EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent ev) {
        //Used to decide side to prevent this from erroring out if someone decides to use this on a client (eg development testing)
        if(ev.getSide() == Side.SERVER)
		    MyTownCore.IS_MCPC = ev.getServer().getServerModName().contains("mcpc");
	}
}