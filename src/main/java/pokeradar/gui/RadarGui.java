package pokeradar.gui;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import pokeradar.entities.EVType;
import pokeradar.util.Logging;
import pokeradar.util.Reference;
import pokeradar.util.VersionCheck;

import java.awt.*;
import java.util.List;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class RadarGui extends GuiScreen {
    public static String searchString = "";
    private static GuiTextField search = null;
    private static String searchLabel = "Search for Pokemon";
    private static String evLabel = "EV Filter";
    private static String natureLabel = "Nature Filter";
    private static int searchWidth = 220;
    private static GuiButton options = null;
    private static GuiButton updateButton = null;
    private static String optionsStr = "Map Settings";
    private static String updateAvail = "PokeRadar Update Available: ";

    private static List<EVType> evTypeList;
    private static List<EnumNature> natureList;
    private static int evIndex = 0;
    private static int natureIndex = 0;

    public void initGui() {
        super.initGui();
        this.search = new GuiTextField(0, fontRenderer, this.width / 2 - searchWidth / 2 , 40, searchWidth, 18);
        this.search.setMaxStringLength(255);
        this.search.setText(this.searchString);
        this.search.setFocused(true);


        Keyboard.enableRepeatEvents(true);

        evTypeList = new ArrayList<EVType>();
        evTypeList.add((EVType) null);
        evTypeList.addAll(Arrays.asList(EVType.values()));

        natureList = new ArrayList<EnumNature>();
        natureList.add((EnumNature) null);
        natureList.addAll(Arrays.asList(EnumNature.values()));

        // EV Filter PREV and NEXT
        this.buttonList.add(
                new GuiButton(100, this.width / 2 - searchWidth / 2, 90,
                        15, 20, "<"));
        this.buttonList.add(
                new GuiButton(101, this.width / 2 - searchWidth / 2 + 20 + fontRenderer.getStringWidth(evLabel)*2, 90,
                        15, 20, ">"));

        // Nature Filter PREV and NEXT
        this.buttonList.add(
                new GuiButton(200, this.width / 2 - searchWidth / 2, 130,
                        15, 20, "<"));
        this.buttonList.add(
                new GuiButton(201, this.width / 2 - searchWidth / 2 + 20 + fontRenderer.getStringWidth(natureLabel)*2, 130,
                        15, 20, ">"));

        // Mod Settings
        this.buttonList.add(
                this.options = new GuiButton(1, this.width / 2 - fontRenderer.getStringWidth(optionsStr.toString()) / 2, 176,
                        fontRenderer.getStringWidth(optionsStr.toString()) + 15, 20, optionsStr));

        // Version check
        if (!VersionCheck.isLatestVersion())
            this.buttonList.add(
                    this.updateButton = new GuiButton(2, 2, 2,
                            fontRenderer.getStringWidth(updateAvail + VersionCheck.getLatestVersion()) + 15,
                            20,updateAvail + VersionCheck.getLatestVersion()));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.searchString = search.getText();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f)
    {
        this.drawDefaultBackground();

        this.drawCenteredString(this.fontRenderer, searchLabel, this.width / 2, 30, Color.WHITE.getRGB());

        GlStateManager.scale(2,2,1);
        this.drawString(this.fontRenderer, evLabel, this.width / 4 - searchWidth / 4 + 9, 47, Color.WHITE.getRGB());
        this.drawString(this.fontRenderer, natureLabel, this.width / 4 - searchWidth / 4 + 9 , 67, Color.WHITE.getRGB());

        GlStateManager.scale(0.5,0.5,1);
        this.drawString(this.fontRenderer, getEVTypeLabel(), this.width / 2 - searchWidth / 2, 113, Color.ORANGE.getRGB());
        this.drawString(this.fontRenderer, getNatureLabel(), this.width / 2 - searchWidth / 2, 153, Color.ORANGE.getRGB());

        // Version check
        if (VersionCheck.isLatestVersion())
            this.drawString(this.fontRenderer, "PokeRadar is up to date! [" + VersionCheck.getLatestVersion() + "]", 2, 2, Color.GRAY.getRGB());

        //this.drawCenteredString(this.fontRenderer, evLabel, this.width / 2, 90, 0xffffff);
        search.drawTextBox();
        super.drawScreen(mouseX, mouseY, f);

    }

    @Override
    protected void keyTyped(char c, int i) throws IOException
    {
        if (i == Keyboard.KEY_RETURN || i == Keyboard.KEY_BACKSLASH) {
            this.mc.displayGuiScreen((GuiScreen) null);
            return;
        }

        super.keyTyped(c, i);
        search.textboxKeyTyped(c, i);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 100)
        {
            // PREV EV
            if (evIndex != 0)
                evIndex--;
            else
                evIndex = evTypeList.size() - 1;
        }
        if (button.id == 101)
        {
            // NEXT EV
            if (evIndex != evTypeList.size() - 1)
                evIndex++;
            else
                evIndex = 0;
        }
        if (button.id == 200)
        {
            // PREV NATURE
            if (natureIndex != 0)
                natureIndex--;
            else
                natureIndex = natureList.size() - 1;
        }
        if (button.id == 201)
        {
            // NEXT NATURE
            if (natureIndex != natureList.size() - 1)
                natureIndex++;
            else
                natureIndex = 0;
        }
        else if (button == this.options)
        {
            try
            {
                GuiScreen newScreen = ModGuiConfig.class
                        .getConstructor(GuiScreen.class)
                        .newInstance(this);
                this.mc.displayGuiScreen(newScreen);
            }
            catch (Exception e)
            {
                Logging.logError(
                        "There was a critical issue trying to build the config GUI for %s",
                        Reference.MOD_ID);
            }
        }
        else if (button == this.updateButton)
        {
            if (Desktop.isDesktopSupported())
            {
                try {
                    Desktop.getDesktop().browse(new URI(VersionCheck.getUpdateURL()));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static EVType getEV() {
        if (evTypeList == null)
            return null;

        return evTypeList.get(evIndex);
    }

    public static EnumNature getNature() {
        if (natureList == null)
            return null;

        return natureList.get(natureIndex);
    }

    public static String getEVTypeLabel() {
        EVType type = getEV();
        if (type == null)
            return "OFF";
        else
            return type.toString();
    }

    public static String getNatureLabel() {
        EnumNature nature = getNature();
        if (nature == null)
            return "OFF";
        return  nature.toString() + " +" + getNatureShorthand(nature.increasedStat) + " -" + getNatureShorthand(nature.decreasedStat);
    }

    public static String getNatureShorthand(StatsType type) {
        switch (type) {
            case Accuracy:
                return "Acc";
            case HP:
                return "HP";
            case Speed:
                return "Speed";
            case Attack:
                return "Atk";
            case Defence:
                return "Def";
            case Evasion:
                return "Eva";
            case SpecialAttack:
                return "SpAtk";
            case SpecialDefence:
                return "SpDef";
            case None:
                return "None";
            default:
                return "";
        }
    }

    private String getSelectionString(ButtonRotator buttonRotator) {
        Enum<?> selected = buttonRotator.getSelected();
        return selected == null ? buttonRotator.getDefaultLabel() : selected.toString();
    }
}
