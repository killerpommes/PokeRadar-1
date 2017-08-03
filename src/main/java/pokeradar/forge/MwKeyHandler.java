package pokeradar.forge;

import java.util.ArrayList;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import org.lwjgl.input.Keyboard;

import pokeradar.Mw;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import pokeradar.util.Reference;

public class MwKeyHandler
{
	public static KeyBinding keyRadar = new KeyBinding("key.open_radar", Keyboard.KEY_BACKSLASH, Reference.MOD_NAME);
	public static KeyBinding keyMapGui = new KeyBinding("key.mw_open_gui", Keyboard.KEY_M, Reference.MOD_NAME);
	public static KeyBinding keyNewMarker = new KeyBinding("key.mw_new_marker", Keyboard.KEY_INSERT, Reference.MOD_NAME);
	public static KeyBinding keyMapMode = new KeyBinding("key.mw_next_map_mode", Keyboard.KEY_N, Reference.MOD_NAME);
	public static KeyBinding keyNextGroup = new KeyBinding("key.mw_next_marker_group", Keyboard.KEY_COMMA, Reference.MOD_NAME);
	public static KeyBinding keyTeleport = new KeyBinding("key.mw_teleport", Keyboard.KEY_PERIOD, Reference.MOD_NAME);
	public static KeyBinding keyZoomIn = new KeyBinding("key.mw_zoom_in", Keyboard.KEY_PRIOR, Reference.MOD_NAME);
	public static KeyBinding keyZoomOut = new KeyBinding("key.mw_zoom_out", Keyboard.KEY_NEXT, Reference.MOD_NAME);
	public static KeyBinding keyUndergroundMode = new KeyBinding("key.mw_underground_mode", Keyboard.KEY_U, Reference.MOD_NAME);

	public final KeyBinding[] keys =
	{
			keyMapGui,
			keyNewMarker,
			keyMapMode,
			keyNextGroup,
			keyTeleport,
			keyZoomIn,
			keyZoomOut,
			keyUndergroundMode,
			keyRadar
	};

	public MwKeyHandler()
	{
		ArrayList<String> listKeyDescs = new ArrayList<String>();
		// Register bindings
		for (KeyBinding key : this.keys)
		{
			if (key != null)
			{
				ClientRegistry.registerKeyBinding(key);
			}
			listKeyDescs.add(key.getKeyDescription());
		}
	}

	@SubscribeEvent
	public void keyEvent(InputEvent.KeyInputEvent event)
	{
		this.checkKeys();
	}

	private void checkKeys()
	{
		for (KeyBinding key : this.keys)
		{
			if ((key != null) && key.isPressed())
			{
				Mw.getInstance().onKeyDown(key);
			}
		}
	}
}
