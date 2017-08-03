package pokeradar.forge;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.*;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.GuiOpenClose;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.*;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;

import pokeradar.Mw;
import pokeradar.config.Config;
import pokeradar.gui.*;
import pokeradar.inject.ColorData;
import pokeradar.inject.EnumColor;
import pokeradar.inject.EnumFormat;
import pokeradar.overlay.OverlaySlime;
import pokeradar.util.Logging;
import pokeradar.util.Reference;
import pokeradar.util.Utils;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static pokeradar.gui.RadarGui.getNatureShorthand;

public class EventHandler
{

	Mw mw;
	private static boolean firstDraw = false;

	public EventHandler(Mw mw)
	{
		this.mw = mw;
	}

	@SubscribeEvent
	public void eventChunkLoad(ChunkEvent.Load event)
	{
		if (event.getWorld().isRemote)
		{
			this.mw.onChunkLoad(event.getChunk());
		}
	}

	@SubscribeEvent
	public void eventChunkUnload(ChunkEvent.Unload event)
	{
		if (event.getWorld().isRemote)
		{
			this.mw.onChunkUnload(event.getChunk());
		}
	}

	@SubscribeEvent
	public void onClientChat(ClientChatReceivedEvent event)
	{
		if (OverlaySlime.seedFound || !OverlaySlime.seedAsked)
		{
			return;
		}
		try
		{ // I don't want to crash the game when we derp up in here
			if (event.getMessage() instanceof TextComponentTranslation)
			{
				TextComponentTranslation component = (TextComponentTranslation) event.getMessage();
				if (component.getKey().equals("commands.seed.success"))
				{
					Long lSeed = (long) 0;
					if (component.getFormatArgs()[0] instanceof Long)
					{
						lSeed = (Long) component.getFormatArgs()[0];
					}
					else
					{
						lSeed = Long.parseLong((String) component.getFormatArgs()[0]);
					}
					OverlaySlime.setSeed(lSeed);
					event.setCanceled(true); // Don't let the player see this
					// seed message, They didn't do
					// /seed, we did
				}
			}
			else if (event.getMessage() instanceof TextComponentString)
			{
				TextComponentString component = (TextComponentString) event.getMessage();
				String msg = component.getUnformattedText();
				if (msg.startsWith("Seed: "))
				{ // Because bukkit...
					OverlaySlime.setSeed(Long.parseLong(msg.substring(6)));
					event.setCanceled(true); // Don't let the player see this
					// seed message, They didn't do
					// /seed, we did
				}
			}
		}
		catch (Exception e)
		{
			Logging.logError("Something went wrong getting the seed. %s", new Object[]
			{
					e.toString()
			});
		}
	}

	@SubscribeEvent
	public void renderMap(RenderGameOverlayEvent.Post event)
	{
		if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
		{
			Mw.getInstance().onTick();
		}
	}

	@SubscribeEvent
	public void onTextureStitchEventPost(TextureStitchEvent.Post event)
	{
		if (Config.reloadColours)
		{
			Logging.logInfo(
					"Skipping the first generation of blockcolours, models are not loaded yet",
					(Object[]) null);
		}
		else
		{
			this.mw.reloadBlockColours();
		}
	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event)
	{
		if (Mw.getInstance().ready)
		{
			Mw.getInstance().markerManager.drawMarkersWorld(event.getPartialTicks());
		}
	}

	@SubscribeEvent
	public void onEvent(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getTarget() instanceof EntityPixelmon
				&& event.getHand() == EnumHand.MAIN_HAND
				&& event.getItemStack() == null)
		{
			try {
				EntityPlayerSP player = this.mw.mc.thePlayer;
				EntityPixelmon entity = (EntityPixelmon) event.getTarget();
				String name = entity.getName();
				EnumBossMode bossMode = entity.getBossMode();
				Boolean isShiny = entity.getIsShiny();
				Field levelField = Utils.findUnderlying(entity.getClass(), "level");
				levelField.setAccessible(true);
				Level level = (Level) levelField.get(entity);
				BaseStats baseStats = entity.baseStats;
				Stats stats = entity.stats;
				Gender gender = entity.gender;
				EVsStore eVsStore = null;
				IVStore ivStore = null;
				EnumNature nature = entity.getNature();
				boolean isEgg = entity.isEgg;
				int ivSum = 0;
				int evSum = 0;
				EnumType hiddenPower = EnumType.Normal;
				int form = entity.getForm();
				//GuiHelper.bindPokemonSprite(name, Pokedex.nameToID(name), form, isShiny, mw.mc);
				String extra = "";
				if (Reference.LEGENDARIES.contains(name)) {
					extra = "Legendary";
				}/*
				if (stats != null) {
					eVsStore = entity.stats.EVs;
					ivStore = entity.stats.IVs;
					if (ivStore != null) {
						for (int i : ivStore.getArray())
							ivSum += i;

						int hp = ivStore.HP % 2;
						int atk = ivStore.Attack % 2;
						int def = ivStore.Defence % 2;
						int sp = ivStore.Speed % 2;
						int spa = ivStore.SpAtt % 2;
						int spd = ivStore.SpDef % 2;
						double top = (double)(32 * spd + 16 * spa + 8 * sp + 4 * def + 2 * atk + hp);
						int typeHidden = (int)Math.floor(top * 15.0D / 63.0D);
						switch (typeHidden) {
							case 0:
								hiddenPower = EnumType.Fighting;
								break;
							case 1:
								hiddenPower = EnumType.Flying;
								break;
							case 2:
								hiddenPower = EnumType.Poison;
								break;
							case 3:
								hiddenPower = EnumType.Ground;
								break;
							case 4:
								hiddenPower = EnumType.Rock;
								break;
							case 5:
								hiddenPower = EnumType.Bug;
								break;
							case 6:
								hiddenPower = EnumType.Ghost;
								break;
							case 7:
								hiddenPower = EnumType.Steel;
								break;
							case 8:
								hiddenPower = EnumType.Fire;
								break;
							case 9:
								hiddenPower = EnumType.Water;
								break;
							case 10:
								hiddenPower = EnumType.Grass;
								break;
							case 11:
								hiddenPower = EnumType.Electric;
								break;
							case 12:
								hiddenPower = EnumType.Psychic;
								break;
							case 13:
								hiddenPower = EnumType.Ice;
								break;
							case 14:
								hiddenPower = EnumType.Dragon;
								break;
							case 15:
								hiddenPower = EnumType.Dark;
								break;
						}
					}
				}*/
				/*
				String[] abilities = entity.baseStats.abilities;
				String hiddenAbility = "";
				String ability = "";
				switch(abilities.length){
					case 1:
						ability = abilities[0];
						break;
					case 2:
						ability = abilities[0];
						hiddenAbility = abilities[1];
						break;
					case 3:
						ability = abilities[0] + "/" + abilities[1];
						hiddenAbility = abilities[2];
						break;
				}*/

				String owner = ChatFormatting.ITALIC + "WILD";
				if (entity.hasOwner())
				{
					owner = entity.getOwner().getName();
				}
				player.addChatMessage(new TextComponentString(""));
				player.addChatMessage(new TextComponentString(Reference.CHAT_BRANDING));
				player.addChatMessage(new TextComponentString( "Level: " + ChatFormatting.RESET + level.getLevel() + ChatFormatting.GREEN + " " + ChatFormatting.BOLD + name + ChatFormatting.RESET
						+ ((isShiny) ? " (" + ChatFormatting.GREEN + "shiny" + ChatFormatting.RESET + ")" : "")
						+ ChatFormatting.ITALIC + " " + ChatFormatting.DARK_AQUA + extra));
				player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Owner: " + ChatFormatting.RESET + owner));
				//player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Ability: " + ChatFormatting.RESET + entity.getAbility().getName()));
				//player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Hidden Ability: " + ChatFormatting.RESET + hiddenAbility));
				player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Growth: " + ChatFormatting.RESET + entity.getGrowth().toString()));
				player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Nature: " + nature.toString() + ChatFormatting.RESET
						+ " +" + getNatureShorthand(nature.increasedStat) + " -" + getNatureShorthand(nature.decreasedStat)));
				/*player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "IVs: "
						+ "HP " + ChatFormatting.RESET + ivStore.HP + ChatFormatting.GREEN + ", "
						+ "Atk " + ChatFormatting.RESET + ivStore.Attack + ChatFormatting.GREEN + ", "
						+ "Def " + ChatFormatting.RESET + ivStore.Defence + ChatFormatting.GREEN + ", "
						+ "SpAtk " + ChatFormatting.RESET + ivStore.SpAtt + ChatFormatting.GREEN + ", "
						+ "SpDef " + ChatFormatting.RESET + ivStore.SpDef + ChatFormatting.GREEN + ", "
						+ "Speed " + ChatFormatting.RESET + ivStore.Speed
				));
				player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Total IVs: " + ivSum
						+ "/186 (" + ChatFormatting.RESET + (int)(((double)ivSum/186)*100) + "%" + ChatFormatting.GREEN + ")"));*/
				/*player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "EVs: "
						+ "HP " + ChatFormatting.RESET + eVsStore.HP + ChatFormatting.GREEN + ", "
						+ "ATK " + ChatFormatting.RESET + eVsStore.Attack + ChatFormatting.GREEN + ", "
						+ "DEF " + ChatFormatting.RESET + eVsStore.Defence + ChatFormatting.GREEN + ", "
						+ "SpAtk " + ChatFormatting.RESET + eVsStore.SpecialAttack + ChatFormatting.GREEN + ", "
						+ "SpDef " + ChatFormatting.RESET + eVsStore.SpecialDefence + ChatFormatting.GREEN + ", "
						+ "Speed " + ChatFormatting.RESET + eVsStore.Speed
				));
				player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Total EVs: " + evSum
						+ "/510 (" + ChatFormatting.RESET + (int)(((double)evSum/510)*100) + "%" + ChatFormatting.GREEN + ")"));*/
				//player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Hidden Power: " + ChatFormatting.RESET + hiddenPower.toString()));
				//player.addChatMessage(new TextComponentString(""));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void fetchStats(GuiOpenEvent event) throws NoSuchFieldException, IllegalAccessException {
		GuiScreenPokeCheckerStats guiStats = (GuiScreenPokeCheckerStats) event.getGui();
		Field field = Utils.findUnderlying(guiStats.getClass(),"targetPacket");
		field.setAccessible(true);
		PixelmonData entity = (PixelmonData) field.get(guiStats);

		EntityPlayerSP player = this.mw.mc.thePlayer;
		String name = entity.name;
		EnumBossMode bossMode = entity.bossMode;
		boolean isShiny = entity.isShiny;
		int level = entity.lvl;
		Gender gender = entity.gender;
		int[] EVs = entity.evs;
		int[] IVs = entity.ivs;
		EnumNature nature = entity.nature;
		boolean isEgg = entity.isEgg;
		int ivSum = 0;
		int evSum = 0;
		EnumType hiddenPower = EnumType.Normal;
		int form = entity.form;
		//GuiHelper.bindPokemonSprite(name, Pokedex.nameToID(name), form, isShiny, mw.mc);
		String extra = "";
		if (Reference.LEGENDARIES.contains(name)) {
			extra = "Legendary";
		}
		if (EVs.length > 0 && IVs.length > 0)
		{
			for (int i : IVs)
				ivSum += i;
			for (int i : EVs)
				evSum += i;

			int hp = IVs[0] % 2;
			int atk = IVs[1] % 2;
			int def = IVs[2] % 2;
			int sp = IVs[5] % 2;
			int spa = IVs[3] % 2;
			int spd = IVs[4] % 2;
			double top = (double)(32 * spd + 16 * spa + 8 * sp + 4 * def + 2 * atk + hp);
			int typeHidden = (int)Math.floor(top * 15.0D / 63.0D);
			switch (typeHidden) {
				case 0:
					hiddenPower = EnumType.Fighting;
					break;
				case 1:
					hiddenPower = EnumType.Flying;
					break;
				case 2:
					hiddenPower = EnumType.Poison;
					break;
				case 3:
					hiddenPower = EnumType.Ground;
					break;
				case 4:
					hiddenPower = EnumType.Rock;
					break;
				case 5:
					hiddenPower = EnumType.Bug;
					break;
				case 6:
					hiddenPower = EnumType.Ghost;
					break;
				case 7:
					hiddenPower = EnumType.Steel;
					break;
				case 8:
					hiddenPower = EnumType.Fire;
					break;
				case 9:
					hiddenPower = EnumType.Water;
					break;
				case 10:
					hiddenPower = EnumType.Grass;
					break;
				case 11:
					hiddenPower = EnumType.Electric;
					break;
				case 12:
					hiddenPower = EnumType.Psychic;
					break;
				case 13:
					hiddenPower = EnumType.Ice;
					break;
				case 14:
					hiddenPower = EnumType.Dragon;
					break;
				case 15:
					hiddenPower = EnumType.Dark;
					break;
			}
		}

		player.addChatMessage(new TextComponentString(""));
		player.addChatMessage(new TextComponentString(Reference.CHAT_BRANDING));
		player.addChatMessage(new TextComponentString( ((isEgg) ? ChatFormatting.YELLOW + "EGG" : "Level " + level)
				+ ChatFormatting.GREEN  + " " + ChatFormatting.BOLD + name + ChatFormatting.RESET
				+ ((isShiny) ? " (" + ChatFormatting.GREEN + "shiny" + ChatFormatting.RESET + ")" : "")
				+ ChatFormatting.ITALIC + " " + ChatFormatting.DARK_AQUA + extra
		));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Gender: " + ChatFormatting.RESET + gender.toString()
				+ " " + ChatFormatting.GREEN + "Growth: " + ChatFormatting.RESET + entity.growth.toString()
		));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Nature: " + nature.toString() + ChatFormatting.RESET
				+ " +" + getNatureShorthand(nature.increasedStat) + " -" + getNatureShorthand(nature.decreasedStat)
		));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Ability: " + ChatFormatting.RESET + entity.ability));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Hidden Power: " + ChatFormatting.RESET + hiddenPower.toString()));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "IVs: "
				+ "HP " + ChatFormatting.RESET + IVs[0] + ChatFormatting.GREEN + ", "
				+ "Atk " + ChatFormatting.RESET + IVs[1] + ChatFormatting.GREEN + ", "
				+ "Def " + ChatFormatting.RESET + IVs[2] + ChatFormatting.GREEN + ", "
				+ "SpAtk " + ChatFormatting.RESET + IVs[3] + ChatFormatting.GREEN + ", "
				+ "SpDef " + ChatFormatting.RESET + IVs[4] + ChatFormatting.GREEN + ", "
				+ "Speed " + ChatFormatting.RESET + IVs[5]
		));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Total IVs: " + ivSum
				+ "/186 (" + ChatFormatting.RESET + (int)(((double)ivSum/186)*100) + "%" + ChatFormatting.GREEN + ")"
		));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "EVs: "
				+ "HP " + ChatFormatting.RESET + EVs[0] + ChatFormatting.GREEN + ", "
				+ "ATK " + ChatFormatting.RESET + EVs[1] + ChatFormatting.GREEN + ", "
				+ "DEF " + ChatFormatting.RESET + EVs[2] + ChatFormatting.GREEN + ", "
				+ "SpAtk " + ChatFormatting.RESET + EVs[3] + ChatFormatting.GREEN + ", "
				+ "SpDef " + ChatFormatting.RESET + EVs[4] + ChatFormatting.GREEN + ", "
				+ "Speed " + ChatFormatting.RESET + EVs[5]
		));
		player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "Total EVs: " + evSum
				+ "/510 (" + ChatFormatting.RESET + (int)(((double)evSum/510)*100) + "%" + ChatFormatting.GREEN + ")"));
		field.setAccessible(false);
	}

	// a bit odd way to reload the blockcolours. if the models are not loaded
	// yet then the uv values and icons will be wrong.
	// this only happens if fml.skipFirstTextureLoad is enabled.
	@SubscribeEvent
	public void onGuiOpenEvent(GuiOpenEvent event)
	{
		GuiScreen guiScreen = event.getGui();

		// Rename Screen
		if (guiScreen instanceof GuiRenamePokemon && !(guiScreen instanceof RenamePokemonGui))
		{
			GuiRenamePokemon gui = (GuiRenamePokemon) guiScreen;
			PixelmonData targetPacket = ReflectionHelper.getPrivateValue(GuiRenamePokemon.class, gui, new String[]{"targetPacket"});
			GuiScreenPokeChecker parent = ReflectionHelper.getPrivateValue(GuiRenamePokemon.class, gui, new String[]{"parentGuiScreen"});

			event.setGui(new RenamePokemonGui(targetPacket, parent));
		}
		// Stats Screen
		else if (guiScreen instanceof GuiScreenPokeCheckerStats && !(guiScreen instanceof ScreenPokeCheckerStatsGui))
		{
			GuiScreenPokeChecker gui = (GuiScreenPokeChecker) guiScreen;
			PixelmonData targetPacket = ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"targetPacket"});

			GuiScreenPokeCheckerStats guiStats = (GuiScreenPokeCheckerStats) guiScreen;
			boolean isPC = ReflectionHelper.getPrivateValue(GuiScreenPokeCheckerStats.class, guiStats, new String[]{"isPC"});
			int box = ReflectionHelper.getPrivateValue(GuiScreenPokeCheckerStats.class, guiStats, new String[]{"box"});

			event.setGui(new ScreenPokeCheckerStatsGui(targetPacket, isPC, box));
		}
		// Moves Screen
		else if (guiScreen instanceof GuiScreenPokeCheckerMoves && !(guiScreen instanceof ScreenPokeCheckerMovesGui))
		{
			GuiScreenPokeChecker gui = (GuiScreenPokeChecker) guiScreen;
			PixelmonData targetPacket = ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"targetPacket"});

			GuiScreenPokeCheckerMoves guiMoves = (GuiScreenPokeCheckerMoves) guiScreen;
			boolean isPC = ReflectionHelper.getPrivateValue(GuiScreenPokeCheckerMoves.class, guiMoves, new String[]{"isPC"});
			int box = ReflectionHelper.getPrivateValue(GuiScreenPokeCheckerMoves.class, guiMoves, new String[]{"box"});

			event.setGui(new ScreenPokeCheckerMovesGui(targetPacket, isPC, box));
		}
		// Summary Screen
		else if (guiScreen instanceof GuiScreenPokeChecker
				&& !(guiScreen instanceof ScreenPokeCheckerGui)
				&& !(guiScreen instanceof ScreenPokeCheckerStatsGui))
		{
			GuiScreenPokeChecker gui = (GuiScreenPokeChecker) guiScreen;
			PixelmonData targetPacket = ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"targetPacket"});
			boolean isPC = ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"isPC"});
			int box = ReflectionHelper.getPrivateValue(GuiScreenPokeChecker.class, gui, new String[]{"box"});

			event.setGui(new ScreenPokeCheckerGui(targetPacket, isPC, box));
		}
		else if (guiScreen instanceof GuiMainMenu && Config.reloadColours)
		{
			this.mw.reloadBlockColours();
			Config.reloadColours = false;
		}
		else if (guiScreen instanceof GuiGameOver)
		{
			this.mw.onPlayerDeath();
		}
		else if (guiScreen instanceof GuiScreenRealmsProxy)
		{
			try
			{
				RealmsScreen proxy = ((GuiScreenRealmsProxy) guiScreen).getProxy();
				RealmsMainScreen parrent = null;

				if (proxy instanceof RealmsLongRunningMcoTaskScreen
						|| proxy instanceof RealmsConfigureWorldScreen)
				{
					Object obj = FieldUtils.readField(proxy, "lastScreen", true);
					if (obj instanceof RealmsMainScreen)
					{
						parrent = (RealmsMainScreen) obj;
					}

					if (parrent != null)
					{
						long id = (Long) FieldUtils.readField(parrent, "selectedServerId", true);
						if (id > 0)
						{
							ArrayList list = (ArrayList) FieldUtils.readField(
									parrent,
									"realmsServers",
									true);
							for (Object item : list)
							{
								RealmsServer server = (RealmsServer) item;
								String Name = server.getName();
								String Owner = server.owner;
								StringBuilder builder = new StringBuilder();
								builder.append(server.owner);
								builder.append("_");
								builder.append(server.getName());
								Utils.RealmsWorldName = builder.toString();
							}
						}
					}

				}
			}
			catch (IllegalAccessException e)
			{

			}
		}
	}
}
