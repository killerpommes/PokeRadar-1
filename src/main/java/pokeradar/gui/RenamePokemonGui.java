package pokeradar.gui;

import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiRenamePokemon;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeChecker;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiTextFieldTransparent;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import pokeradar.inject.EnumColor;
import pokeradar.inject.EnumFormat;

import java.io.IOException;

public class RenamePokemonGui extends GuiRenamePokemon {

    public RenamePokemonGui(PixelmonData targetPacket, GuiScreenPokeChecker parent) {
        super(targetPacket, parent);
    }

    @Override
    public void keyTyped(char c, int i) throws IOException {
        super.keyTyped(c, i);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f)
    {
        super.drawScreen(mouseX, mouseY, f);
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        super.initGui();
        int height = this.height;
        int width = this.width;
        int count = 2000;
        int colorLen = EnumColor.values().length;
        int buttonHeight = Math.min((int)Math.floor(height / colorLen), 20);
        int interval = 0;

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
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button != null)
        {
            if (button.id >= 2000 && button.id < (2000 + EnumColor.values().length))
            {
                GuiTextFieldTransparent textField = ReflectionHelper.getPrivateValue(GuiRenamePokemon.class, this, new String[]{"theGuiTextField"});
                if (textField != null) {
                    textField.writeText(EnumColor.get(button.id - 2000).getCode());
                    ReflectionHelper.setPrivateValue(GuiRenamePokemon.class, this, textField, new String[]{"theGuiTextField"});
                }
            }
            else if (button.id >= 3000 && button.id < (3000 + EnumFormat.values().length))
            {
                GuiTextFieldTransparent textField = ReflectionHelper.getPrivateValue(GuiRenamePokemon.class, this, new String[]{"theGuiTextField"});
                if (textField != null) {
                    textField.writeText(EnumFormat.get(button.id - 3000).getCode());
                    ReflectionHelper.setPrivateValue(GuiRenamePokemon.class, this, textField, new String[]{"theGuiTextField"});
                }
            }
        }
    }
}
