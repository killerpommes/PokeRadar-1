package pokeradar.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import pokeradar.asm.obf.ObfuscationUtils;

import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions({"pokeradar"})
@IFMLLoadingPlugin.MCVersion("1.10.2")
public class PokeradarLoadingPlugin
        implements IFMLLoadingPlugin {

    public static boolean isLoaded = false;

    public PokeradarLoadingPlugin() {
        System.out.println("> Loading Pokeradar Transformers");
        isLoaded = true;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                "pokeradar.asm.AllowedCharactersTransformer",
                "pokeradar.asm.UnlimitedCharactersTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        ObfuscationUtils.setRuntimeDeobfuscated(((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue());
    }

    @Override
    public String getAccessTransformerClass() {
        return getASMTransformerClass()[0];
    }
}