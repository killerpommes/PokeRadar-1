package pokeradar.gui;

import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiPokeCheckerTabs;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiScreenPokeCheckerStats;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;

import java.awt.*;
import java.io.IOException;

public class ScreenPokeCheckerStatsGui extends GuiScreenPokeCheckerStats {
    private int hexWhite = Color.WHITE.getRGB();
    private int hexIncrease = Color.GREEN.getRGB();
    private int hexDecrease = Color.RED.getRGB();
    protected boolean isPC;
    protected int box;

    public ScreenPokeCheckerStatsGui(PixelmonData selected, boolean b, int box) {
        super(selected, b, box);
        this.isPC = b;
        this.box = box;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiPokeCheckerTabs(3, 0, this.width / 2 + 107, this.height / 2 + 80, 17, 15, ""));
        this.buttonList.add(new GuiPokeCheckerTabs(0, 1, this.width / 2 - 127, this.height / 2 + 80, 90, 15, I18n.translateToLocal("gui.screenpokechecker.summary")));
        this.buttonList.add(new GuiPokeCheckerTabs(1, 2, this.width / 2 - 34, this.height / 2 + 80, 69, 15, I18n.translateToLocal("gui.screenpokechecker.moves")));
        this.buttonList.add(new GuiPokeCheckerTabs(4, 4, this.width / 2 - 44, this.height / 2 - 107, 9, 9, "", this.targetPacket));
        this.buttonList.add(new GuiPokeCheckerTabs(7, 5, this.width / 2 - 44, this.height / 2 - 1, 9, 8, "", this.targetPacket));
        this.buttonList.add(new GuiButtonTab(100, this.width / 2 + 52, this.height / 2 - 114, 26, 12, "IV"));
        this.buttonList.add(new GuiButtonTab(101, this.width / 2 + 77, this.height / 2 - 114, 26, 12, "EV"));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        boolean isEgg = this.targetPacket.isEgg;
        this.targetPacket.isEgg = false;
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        this.targetPacket.isEgg = isEgg;
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        switch(button.id)
        {
            case 100:
                this.mc.displayGuiScreen(new ScreenPokeCheckerStatsIVGui(this.targetPacket, this.isPC, this.box));
                break;
            case 101:
                this.mc.displayGuiScreen(new ScreenPokeCheckerStatsEVGui(this.targetPacket, this.isPC, this.box));
                break;
        }
        super.actionPerformed(button);
    }
}
