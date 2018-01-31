package pokeradar.inject;

import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiRenamePokemon;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pokeradar.gui.RenamePokemonGui;
import pokeradar.util.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ColorData
{

    public static final int POS_X = 5;
    public static final int POS_Y = 5;

    public static int buttonWidth = 50;
    public static int buttonHeight = 20;

    public static final int PADDING = 3;
    public static final int OUTLINE = 2;

    public static final String FORMAT_BUTTON_STR = "Insert Format Symbol"; // TODO: Translate
    //public static List<GuiButton> buttonList = new ArrayList<GuiButton>();
    public static final int FORMAT_BUTTON_ID = 10;

    public static final String FORMAT_SYMBOL = new String( new char[] { ( char ) 167 } );

    public static void initGui( GuiScreen gui) {
        int height = gui.height;
        int width = gui.width;
        int count = 2000;
        int colorLen = EnumColor.values().length;
        int buttonHeight = Math.min((int)Math.floor(height / colorLen), 20);
        int interval = 0;

        List<GuiButton> buttonList = ReflectionHelper.getPrivateValue(GuiScreen.class, gui, new String[]{"field_146292_n", "buttonList"});
        if (buttonList != null) {

            // COLORS loop
            for (EnumColor color : EnumColor.values()) {
                GuiButton button = new GuiButton(count, 0, interval, 80, buttonHeight, color.getCode() + color.getName());
                buttonList.add(button);
                count++;
                interval += buttonHeight;
            }

            count = 3000;
            interval = 0;

            // FORMAT loop
            for (EnumFormat format : EnumFormat.values()) {
                GuiButton button = new GuiButton(count, width - 80, interval, 80, buttonHeight, format.getCode() + format.getName());
                buttonList.add(button);
                count++;
                interval += buttonHeight;
            }

            ReflectionHelper.setPrivateValue(GuiScreen.class, gui, buttonList, new String[]{"field_146292_n", "buttonList"});
        }
    }
}
