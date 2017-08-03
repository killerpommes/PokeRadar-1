package pokeradar.forge;

import java.io.File;

import pokeradar.Mw;
import pokeradar.api.MwAPI;
import pokeradar.config.ConfigurationHandler;
import pokeradar.overlay.OverlayGrid;
import pokeradar.overlay.OverlaySlime;
import pokeradar.region.MwChunk;
import pokeradar.util.Reference;
import pokeradar.util.VersionCheck;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(File configFile)
	{
		ConfigurationHandler.init(configFile);
		MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());
	}

	@Override
	public void load()
	{
		EventHandler eventHandler = new EventHandler(Mw.getInstance());
		MinecraftForge.EVENT_BUS.register(eventHandler);

		MwKeyHandler keyEventHandler = new MwKeyHandler();
		MinecraftForge.EVENT_BUS.register(keyEventHandler);
	}

	@Override
	public void postInit()
	{
		if (Loader.isModLoaded("VersionChecker"))
		{
			FMLInterModComms.sendRuntimeMessage(
					Reference.MOD_ID,
					"VersionChecker",
					"addVersionCheck",
					Reference.VersionURL);
		}
		else
		{
			VersionCheck versionCheck = new VersionCheck();
			Thread versionCheckThread = new Thread(versionCheck, "Version Check");
			versionCheckThread.start();
		}
		if (Loader.isModLoaded("CarpentersBlocks"))
		{
			MwChunk.carpenterdata();
		}
		if (Loader.isModLoaded("ForgeMultipart"))
		{
			MwChunk.FMPdata();

		}
		MwAPI.registerDataProvider("Slime", new OverlaySlime());
		MwAPI.registerDataProvider("Grid", new OverlayGrid());
	}
}
