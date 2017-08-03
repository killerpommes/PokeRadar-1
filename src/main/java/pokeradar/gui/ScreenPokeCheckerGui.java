package pokeradar.gui;

import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeChecker;
import com.pixelmonmod.pixelmon.comm.PixelmonData;

public class ScreenPokeCheckerGui extends GuiScreenPokeChecker {

    public ScreenPokeCheckerGui(PixelmonData selected, boolean b, int box) {
        super(selected, b, box);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        boolean isEgg = this.targetPacket.isEgg;
        this.targetPacket.isEgg = false;
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.targetPacket.isEgg = isEgg;
    }
}
