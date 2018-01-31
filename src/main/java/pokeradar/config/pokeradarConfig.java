package pokeradar.config;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import pokeradar.util.Reference;

public class pokeradarConfig
{
    public final String configCategory;
    public static boolean drawMarkersInWorldDef = true;
    public static boolean drawMarkersInWorld = drawMarkersInWorldDef;
    public static boolean drawMarkersNameInWorldDef = true;
    public static boolean drawMarkersNameInWorld = drawMarkersNameInWorldDef;

    public static boolean drawLootMarkersDef = true;
    public static boolean drawLootMarkers = drawLootMarkersDef;

    public static boolean drawShinyMarkersDef = true;
    public static boolean drawShinyMarkers = drawShinyMarkersDef;

    public static boolean drawSearchedMarkersDef = true;
    public static boolean drawSearchedMarkers = drawSearchedMarkersDef;

    public static boolean drawBossMarkersDef = true;
    public static boolean drawBossMarkers = drawBossMarkersDef;

    public static boolean drawSpecialMarkersDef = true;
    public static boolean drawSpecialMarkers = drawSpecialMarkersDef;

    public static boolean useTexturesDef = false;
    public static boolean useTextures = useTexturesDef;

    public static double textureSizeDef = 1;
    public static double textureSize = textureSizeDef;

    public static double dotSizeDef = 1;
    public static double dotSize = dotSizeDef;


    public pokeradarConfig(String configCategory)
    {
        this.configCategory = configCategory;
    }

    public void loadConfig()
    {
        // get options from config file
        pokeradarConfig.drawMarkersInWorld = ConfigurationHandler.configuration.getBoolean(
                "drawMarkersInWorld",
                Reference.catPokeRadarConfig,
                pokeradarConfig.drawMarkersInWorldDef,
                "",
                "mw.config.pokeradar.drawMarkersInWorld");
        pokeradarConfig.drawMarkersNameInWorld = ConfigurationHandler.configuration.getBoolean(
                "drawMarkersNameInWorld",
                Reference.catPokeRadarConfig,
                pokeradarConfig.drawMarkersNameInWorldDef,
                "",
                "mw.config.pokeradar.drawMarkersNameInWorld");
        pokeradarConfig.drawLootMarkers = ConfigurationHandler.configuration.getBoolean(
                "drawLootMarkers",
                Reference.catPokeRadarConfig,
                pokeradarConfig.drawLootMarkersDef,
                "",
                "mw.config.pokeradar.drawLootMarkers");
        pokeradarConfig.drawShinyMarkers = ConfigurationHandler.configuration.getBoolean(
                "drawShinyMarkers",
                Reference.catPokeRadarConfig,
                pokeradarConfig.drawShinyMarkersDef,
                "",
                "mw.config.pokeradar.drawShinyMarkers");
        pokeradarConfig.drawSearchedMarkers = ConfigurationHandler.configuration.getBoolean(
                "drawSearchedMarkers",
                Reference.catPokeRadarConfig,
                pokeradarConfig.drawSearchedMarkersDef,
                "",
                "mw.config.pokeradar.drawSearchedMarkers");
        pokeradarConfig.drawBossMarkers = ConfigurationHandler.configuration.getBoolean(
                "drawBossMarkers",
                Reference.catPokeRadarConfig,
                pokeradarConfig.drawBossMarkersDef,
                "",
                "mw.config.pokeradar.drawBossMarkers");
        pokeradarConfig.drawSpecialMarkers = ConfigurationHandler.configuration.getBoolean(
            "drawSpecialMarkers",
            Reference.catPokeRadarConfig,
            pokeradarConfig.drawSpecialMarkersDef,
            "",
            "mw.config.pokeradar.drawSpecialMarkers");
        pokeradarConfig.useTextures = ConfigurationHandler.configuration.getBoolean(
                "useTextures",
                Reference.catPokeRadarConfig,
                pokeradarConfig.useTexturesDef,
                "",
                "mw.config.pokeradar.useTextures");
        Property textureSize = ConfigurationHandler.configuration.get(
                Reference.catPokeRadarConfig,
                "sizeTexture",
                1,"Change the size of texture markers on your minimap",
                1,
                30);
        textureSize.setConfigEntryClass(getSliderClass());
        textureSize.setLanguageKey("mw.config.pokeradar.textureSize");
        pokeradarConfig.textureSize = textureSize.getDouble();
        Property dotSize = ConfigurationHandler.configuration.get(
                Reference.catPokeRadarConfig,
                "sizeDot",
                1,"Change the size of dot markers on your minimap",
                1,
                30);
        dotSize.setConfigEntryClass(getSliderClass());
        dotSize.setLanguageKey("mw.config.pokeradar.dotSize");
        pokeradarConfig.dotSize = dotSize.getDouble();

    }

    public void setDefaults()
    {
        this.drawMarkersInWorld = true;
        this.drawMarkersNameInWorld = true;
        this.drawLootMarkers = true;
        this.drawShinyMarkers = true;
        this.drawSearchedMarkers = true;
        this.drawBossMarkers = true;
        this.drawSpecialMarkers = true;
        this.useTextures = false;
    }

    public Class<? extends GuiConfigEntries.IConfigEntry> getSliderClass()
    {
        return GuiConfigEntries.NumberSliderEntry.class;
    }

    public String getConfigCategory()
    {
        return this.configCategory;
    }
}
