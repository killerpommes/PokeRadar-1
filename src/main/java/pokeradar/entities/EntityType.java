package pokeradar.entities;

import pokeradar.config.pokeradarConfig;

import java.awt.*;

public enum EntityType
{
    STATUE(2.0D, new Color(130, 130, 130)),
    SHRINE(4.0D, new Color(0, 0, 255)),
    NPC(2.0D, new Color(130, 130, 130)),
    REGULAR(1.9D, new Color(255, 255, 255)),
    PLAYER(2.1D, new Color(0, 0, 0)),
    LOOT(2.1D, new Color(255, 229, 2)),
    SHINY(3.0D, new Color(204, 0, 255)),
    SEARCHED(3.0D, new Color(0, 255, 0)),
    BOSS_UNCOMMON(3.0D, new Color(0, 255, 0)),
    BOSS_RARE(3.0D, new Color(255, 255, 0)),
    BOSS_LEGENDARY(3.0D, new Color(255, 0, 0)),
    BOSS_ULTIMATE(3.0D, new Color(255, 140, 0)),
    SPECIAL(5.5D, new Color(204, 0, 255));

    private double radius;
    private Color color;

    EntityType(double radiusN, Color color)
    {
        this.radius = radiusN;
        this.color = color;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public double getRadius()
    {
        return this.radius + (pokeradarConfig.dotSize - 1);
    }

    public double getTextureDimension()
    {
        return this.radius * 3.5 + 6 + (pokeradarConfig.textureSize - 1);
    }

    public Color getColor()
    {
        return this.color;
    }
}
