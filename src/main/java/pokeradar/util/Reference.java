package pokeradar.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Sets;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.ResourceLocation;

public final class Reference
{
	public static final String MOD_ID = "pokeradar";
	public static final String MOD_NAME = "PokeRadar";
	public static final String AUTHOR = "CallMeTheDr";
	public static final String VERSION = "3.8.0";
	public static final String ACCEPTABLE_MC_VERSIONS = "[1.10.2, 1.12.2]";
	public static final String MOD_GUIFACTORY_CLASS = "pokeradar.gui.ModGuiFactoryHandler";
	public static final String CLIENT_PROXY_CLASS = "pokeradar.forge.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "pokeradar.forge.CommonProxy";

	public static final String ACCEPTABLE_REMOTE_VERSIONS = "*";
	public static final boolean CLIENT_ONLY = true;
	public static final String DEPENDENCIES = "";//required-after:pixelmon";

	public static final String BRANDING = MOD_NAME + " " + VERSION + " by " + AUTHOR;
	public static final String RADAR_BRANDING = ChatFormatting.RED + "Poke" + ChatFormatting.WHITE + "Radar" + ChatFormatting.RESET;
	public static final String CHAT_BRANDING = ChatFormatting.BOLD + "" + ChatFormatting.WHITE + "--- " + ChatFormatting.RED + "Poke" + ChatFormatting.WHITE + "Radar" + " ---" + ChatFormatting.RESET;

	public static final List<String> LEGENDARIES = Arrays.asList(new String[]{"Articuno", "Zapdos", "Moltres", "Mewtwo", "Mew", "Entei", "Raikou", "Suicune", "Ho-oh", "Ho-Oh", "HoOh", "Lugia", "Celebi", "Regirock", "Regice", "Registeel", "Latios", "Latias", "Groudon", "Kyogre", "Rayquaza", "Jirachi", "Deoxys", "Uxie", "Azelf", "Mesprit", "Dialga", "Palkia", "Giratina", "Cresselia", "Darkrai", "Manaphy", "Phione", "Heatran", "Regigigas", "Shaymin", "Arceus", "Victini", "Cobalion", "Terrakion", "Virizion", "Keldeo", "Thundurus", "Tornadus", "Landorus", "Zekrom", "Reshiram", "Kyurem", "Genesect", "Meloetta", "Xerneas", "Yveltal", "Zygarde", "Diancie", "Hoopa", "Volcanion", "Cosmog", "Cosmoem", "Solgaleo", "Lunala", "Necrozma", "Magearna", "Marshadow", "Tapu Koko", "Tapu Lele", "Tapu Bulu", "Tapu Fini", "Nihilego", "Buzzwole", "Pheromosa", "Xurkitree", "Celesteela", "Kartana", "Guzzlord"});

	public static final String VersionURL = "https://goo.gl/T20VFb";
	public static final String ForgeVersionURL = "https://raw.githubusercontent.com/Vectron/Versions/master/ForgeMwVersion.json";

	public static final String catOptions = "options";
	public static final String catLargeMapConfig = "largemap";
	public static final String catSmallMapConfig = "smallmap";
	public static final String catFullMapConfig = "fullscreenmap";
	public static final String catPokeRadarConfig = "pokeradar";
	public static final String catMapPos = "mappos";

	public static final String PlayerTrailName = "player";
	public static final Pattern patternInvalidChars = Pattern.compile("[^\\p{L}\\p{Nd}_]");
	public static final Pattern patternInvalidChars2 = Pattern.compile("[^\\p{L}\\p{Nd}_ -]");

	public static final String catWorld = "world";
	public static final String catMarkers = "markers";
	public static final String worldDirConfigName = "pokeradar.cfg";
	public static final String blockColourSaveFileName = "MapWriterBlockColours.txt";
	public static final String blockColourOverridesFileName = "MapWriterBlockColourOverrides.txt";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	public static final ResourceLocation backgroundTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/background.png");
	public static final ResourceLocation roundMapTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/border_round.png");
	public static final ResourceLocation squareMapTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/border_square.png");
	public static final ResourceLocation playerArrowTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/arrow_player.png");
	public static final ResourceLocation northArrowTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/arrow_north.png");
	public static final ResourceLocation leftArrowTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/arrow_text_left.png");
	public static final ResourceLocation rightArrowTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/arrow_text_right.png");
	public static final ResourceLocation DummyMapTexture = new ResourceLocation(
			"mapwriter",
			"textures/map/dummy_map.png");

	public static final HashSet<String> PROTOCOLS = Sets.newHashSet(new String[]
	{
			"http",
			"https"
	});
}
