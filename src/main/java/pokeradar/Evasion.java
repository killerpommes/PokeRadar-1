package pokeradar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.NetworkModHolder;
import net.minecraftforge.fml.relauncher.CoreModManager;
import pokeradar.forge.Pokeradar;
import pokeradar.util.Reference;

import java.lang.reflect.Field;
import java.util.*;

public class Evasion {

    public static void ProcessEvasion() {
        try {
            ModScanner();
            ModList();
            ActiveModList();
            NetworkRegistry();
            AddBranding();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    private static void ModScanner() throws NoSuchFieldException, IllegalAccessException {
        // IGNORE ME! https://www.youtube.com/watch?v=FMNJuSl91qY
        Field field = CoreModManager.class.getDeclaredField("ignoredModFiles");
        field.setAccessible(true);
        List<String> ignore = (List<String>) field.get(CoreModManager.class);
        List<String> ignoreCopy = new ArrayList(ignore);
        ignoreCopy.add(Reference.MOD_ID + "-" + Reference.VERSION + ".jar");
        field.set(FMLCommonHandler.instance(), ImmutableList.copyOf(ignoreCopy));
        field.setAccessible(false);
    }

    @SuppressWarnings("unchecked")
    private static void ModList() throws NoSuchFieldException, IllegalAccessException {
        // MODS
        Field mods = Loader.instance().getClass().getDeclaredField("mods");
        mods.setAccessible(true);

        List<ModContainer> modsList = (List)mods.get(Loader.instance());
        List<ModContainer> modsListTmp = new ArrayList();
        for (int i = 0; i < modsList.size(); i++) {
            if (!((ModContainer)modsList.get(i)).getModId().equals(Reference.MOD_ID)) {
                modsListTmp.add(modsList.get(i));
            }
        }
        mods.set(Loader.instance(), ImmutableList.copyOf(modsListTmp));
        mods.setAccessible(false);
    }

    @SuppressWarnings("unchecked")
    private static void ActiveModList() {
        List<ModContainer> modListActive = Loader.instance().getActiveModList();
        List<ModContainer> tmpModListActive = new ArrayList(modListActive);
        int thismod = 0;
        for (int i = 0; i < modListActive.size(); i++) {
            if (((ModContainer) modListActive.get(i)).getModId().equals(Reference.MOD_ID)) {
                thismod = i;
            }
        }

        //Iterator<ModContainer> iter = modListActive.
        modListActive.remove(modListActive.get(thismod));
        //modListActive.clear();
        /*for (int i = 0; i < tmpModListActive.size(); i++) {
            modListActive.add(tmpModListActive.get(i));
        }*/
    }

    @SuppressWarnings("unchecked")
    private static void NetworkRegistry() throws NoSuchFieldException, IllegalAccessException {
        Field registryField = NetworkRegistry.INSTANCE.getClass().getDeclaredField("registry");
        registryField.setAccessible(true);

        Map<ModContainer, NetworkModHolder> registry = (Map)registryField.get(NetworkRegistry.INSTANCE);
        Map<ModContainer, NetworkModHolder> registryTmp = new HashMap();
        for (Map.Entry networkMod : registry.entrySet()) {
            if (!((ModContainer)networkMod.getKey()).getModId().equals(Reference.MOD_ID)) {
                registryTmp.put((ModContainer)networkMod.getKey(), (NetworkModHolder)networkMod.getValue());
            }
        }
        registryField.set(NetworkRegistry.INSTANCE, ImmutableMap.copyOf(registryTmp));
        registryField.setAccessible(false);
    }

    @SuppressWarnings("unchecked")
    private static void AddBranding()
    {
        try
        {
            Field brdfield = FMLCommonHandler.instance().getClass().getDeclaredField("brandings");
            brdfield.setAccessible(true);
            List<String> brd = (List<String>) brdfield.get(FMLCommonHandler.instance());
            List<String> brdCopy = new ArrayList(brd);
            brdCopy.add(Reference.BRANDING);
            brdfield.set(FMLCommonHandler.instance(), ImmutableList.copyOf(brdCopy));
            brdfield.setAccessible(false);
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

    }
}
