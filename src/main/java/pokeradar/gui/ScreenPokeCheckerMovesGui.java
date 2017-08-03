package pokeradar.gui;

import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeCheckerMoves;
import com.pixelmonmod.pixelmon.comm.PixelmonData;

public class ScreenPokeCheckerMovesGui extends GuiScreenPokeCheckerMoves {

    public ScreenPokeCheckerMovesGui(PixelmonData selected, boolean b, int box) {
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
