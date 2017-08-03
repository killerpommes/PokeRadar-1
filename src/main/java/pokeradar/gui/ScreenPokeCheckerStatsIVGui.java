package pokeradar.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.pixelmonmod.pixelmon.client.gui.pokechecker.GuiPokeCheckerTabs;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.enums.EnumType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class ScreenPokeCheckerStatsIVGui extends ScreenPokeCheckerStatsGui {
    private int hexWhite = Color.WHITE.getRGB();

    public ScreenPokeCheckerStatsIVGui(PixelmonData selected, boolean b, int box) {
        super(selected, b, box);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiPokeCheckerTabs(2, 200, this.width / 2 + 36, this.height / 2 + 80, 69, 15, I18n.translateToLocal("gui.screenpokechecker.stats")));
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 200)
            this.mc.displayGuiScreen(new ScreenPokeCheckerStatsGui(this.targetPacket, this.isPC, this.box));
        else
            super.actionPerformed(button);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        int hexColor = this.hexWhite;
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);

        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("gui.screenpokechecker.lvl") + " " + this.targetPacket.lvl, 10, -14, hexColor);
        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("gui.screenpokechecker.number") + " " + this.targetPacket.getNationalPokedexNumber(), -30, -14, hexColor);
        this.drawCenteredString(this.mc.fontRendererObj, String.valueOf(this.targetPacket.OT), 8, 126, hexColor);


        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("gui.screenpokechecker.ot"), -32, 112, hexColor);
        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("nbt.hp.name"), 60, -12, hexColor);
        String strHP = String.valueOf(this.targetPacket.ivs[0]);


        this.drawString(this.mc.fontRendererObj, strHP, 200 - strHP.length() * 3, -12, hexColor);

        String strATK = String.valueOf(this.targetPacket.ivs[1]);

        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("nbt.attack.name"), 60, 9, hexColor);
        this.drawString(this.mc.fontRendererObj, strATK, 200 - strATK.length() * 3, 9, hexColor);

        String strDEF = String.valueOf(this.targetPacket.ivs[2]);

        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("nbt.defense.name"), 60, 28, hexColor);
        this.drawString(this.mc.fontRendererObj, strDEF, 200 - strDEF.length() * 3, 28, hexColor);

        String strSATK = String.valueOf(this.targetPacket.ivs[3]);

        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("nbt.spattack.name"), 60, 48, hexColor);
        this.drawString(this.mc.fontRendererObj, strSATK, 200 - strSATK.length() * 3, 48, hexColor);

        String strSDEF = String.valueOf(this.targetPacket.ivs[4]);

        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("nbt.spdefense.name"), 60, 69, hexColor);
        this.drawString(this.mc.fontRendererObj, strSDEF, 200 - strSDEF.length() * 3, 69, hexColor);

        String strSPD = String.valueOf(this.targetPacket.ivs[5]);

        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("nbt.speed.name"), 60, 88, hexColor);
        this.drawString(this.mc.fontRendererObj, strSPD, 200 - strSPD.length() * 3, 88, hexColor);
        hexColor = this.hexWhite;
        this.drawCenteredString(this.mc.fontRendererObj, "Hidden Power", 95, 115, hexColor);
        this.drawCenteredString(this.mc.fontRendererObj, "Total IV", 174, 115, hexColor);
        this.drawCenteredString(this.mc.fontRendererObj, I18n.translateToLocal("gui.screenpokechecker.growth"), 8, 137, hexColor);

        int ivSum = 0;
        for (int i : this.targetPacket.ivs)
            ivSum += i;

        EnumType hiddenPower = EnumType.Normal;
        int hp = this.targetPacket.ivs[0] % 2;
        int atk = this.targetPacket.ivs[1] % 2;
        int def = this.targetPacket.ivs[2] % 2;
        int sp = this.targetPacket.ivs[5] % 2;
        int spa = this.targetPacket.ivs[3] % 2;
        int spd = this.targetPacket.ivs[4] % 2;
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

        this.drawCenteredString(this.mc.fontRendererObj, hiddenPower.getLocalizedName(), 95, 130, hexColor);
        this.drawCenteredString(this.mc.fontRendererObj, ivSum + "/186", 174, 130, -1);
        this.drawCenteredString(this.mc.fontRendererObj, "" + ChatFormatting.YELLOW + ChatFormatting.BOLD + String.valueOf((int)(((double)ivSum/186)*100)) + "%",174, 144, -1);
        this.drawCenteredString(this.mc.fontRendererObj, this.targetPacket.growth.getLocalizedName(), 8, 150, -1);


        this.drawString(this.mc.fontRendererObj, I18n.translateToLocal("gui.screenpokechecker.stats"), 145, 166, hexColor);

        boolean isEgg = this.targetPacket.isEgg;
        this.targetPacket.isEgg = false;
        this.drawBasePokemonInfo();
        this.targetPacket.isEgg = isEgg;
    }
}
