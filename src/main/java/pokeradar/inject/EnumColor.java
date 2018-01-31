package pokeradar.inject;

import java.awt.Color;

import static com.mojang.realmsclient.gui.ChatFormatting.PREFIX_CODE;

public enum EnumColor {
    DARK_RED("4", "Dark Red", new Color(170,0,0)),
    RED("c", "Red", new Color(255,0,0)),
    GOLD("6", "Gold", new Color(255,170,0)),
    YELLOW("e", "Yellow", new Color(255,255,85)),
    DARK_GREEN("2", "Dark Green", new Color(0,170,0)),
    GREEN("a", "Green", new Color(85,255,85)),
    DARK_AQUA("3", "Dark Aqua", new Color(0,170,170)),
    AQUA("b", "Aqua", new Color(85,255,255)),
    DARK_BLUE("1", "Dark Blue", new Color(0,0,170)),
    BLUE("9", "Blue", new Color(85,85,255)),
    DARK_PURPLE("5", "Dark Purple", new Color(170,0,170)),
    LIGHT_PURPLE("d", "Light Purple", new Color(255,85,255)),
    WHITE("f", "White", new Color(255,255,255)),
    GRAY("7", "Gray", new Color(170,170,170)),
    DARK_GRAY("8", "Dark Gray", new Color(85,85,85)),
    BLACK("0", "Black", new Color(0,0,0));

    private String code;
    private String name;
    private Color color;

    EnumColor (String code, String name, Color color)
    {
        this.code = code;
        this.name = name;
        this.color = color;
    }

    public String getName()
    {
        return this.name;
    }

    public String getCode()
    {
        return PREFIX_CODE + this.code;
    }

    public Color getColor()
    {
        return this.color;
    }

    public static EnumColor get(int index)
    {
        return EnumColor.values()[index];
    }
}
