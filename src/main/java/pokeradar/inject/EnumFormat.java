package pokeradar.inject;

import com.mojang.realmsclient.gui.ChatFormatting;

import static com.mojang.realmsclient.gui.ChatFormatting.PREFIX_CODE;

public enum EnumFormat {

    RESET("r", "Reset"),
    BOLD("l", "Bold"),
    ITALIC("o", "Italic"),
    UNDERLINE("n", "Underline"),
    STRIKE("m", "Strike"),
    RANDOM("k", "Random");

    private String code;
    private String name;

    EnumFormat(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getCode()
    {
        return PREFIX_CODE + this.code;
    }

    public static EnumFormat get(int index)
    {
        return EnumFormat.values()[index];
    }
}
