package pokeradar.map;

import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.util.helpers.SpriteHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import pokeradar.entities.EntityType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import pokeradar.map.mapmode.MapMode;
import pokeradar.util.Render;

import java.awt.*;
import java.awt.geom.Point2D;

public class PKMarker {
    public Point2D.Double screenPos = new Point2D.Double(0.0D, 0.0D);
    public String name = "";
    public String pkName = "";
    public String desc = "";
    public int form = 0;
    public boolean isShiny = false;
    public boolean isMega = false;
    ResourceLocation sprite;
    public EntityPixelmon entityPixelmon;
    public double r = 0D;
    public double x = 0D;
    public double y = 0D;
    public double z = 0D;
    public int coordX = 0;
    public int coordY = 0;
    public int coordZ = 0;
    public int dimension;
    public EntityType type;
    private static double borderWidth = 0.4D;
    private static int borderColor = 0xff000000;
    public float yaw;

    // pk, player, npc entities
    public PKMarker(EntityType type, Entity entity, String name, String desc, double radius, MapRenderer mp, int dimension)
    {
        this.name = name;

        if (entity instanceof EntityPixelmon)
        {
            EntityPixelmon entityPixelmon = (EntityPixelmon) entity;
            this.form = entityPixelmon.getForm();
            this.isShiny = entityPixelmon.getIsShiny();
            this.pkName = entityPixelmon.getPokemonName();

            String extra = SpriteHelper.getSpriteExtra(this.pkName, this.form);
            String pkNum = String.format("%03d", new Object[]{Integer.valueOf(Pokedex.nameToID(this.pkName))});
            if (this.form > 0 && this.isShiny) {
                if(entityPixelmon.baseStats != null) {
                    EnumPokemon enumPokemon = entityPixelmon.baseStats.pokemon;
                    if (enumPokemon != null && enumPokemon.hasMega()) {
                        this.isShiny = false;
                        this.isMega = true;
                        this.name = "Mega " + this.name;
                    }
                }
            }
            if (this.isShiny) {
                this.sprite = GuiResources.shinySprite(pkNum + extra);
                this.name += " (shiny)";
            } else {
                this.sprite = GuiResources.sprite(pkNum + extra);
            }
        }

        this.yaw = entity.rotationYaw;
        this.dimension = dimension;
        this.type = type;
        this.r = radius;
        this.desc = desc;
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
        this.coordX = (int)x;
        this.coordY = (int)y;
        this.coordZ = (int)z;
        double scale = mp.getMapView().getDimensionScaling(Minecraft.getMinecraft().theWorld.provider.getDimensionType().getId());
        Point2D.Double p = mp.getMapMode().getClampedScreenXY(mp.getMapView(), x * scale, z * scale);
        this.screenPos.setLocation(p.x + mp.getMapMode().getXTranslation(), p.y + mp.getMapMode().getYTranslation());
    }

    // tile entities
    public PKMarker(EntityType type, TileEntity entity, String name, String desc, double radius, MapRenderer mp, int dimension)
    {
        this.yaw = 0f;
        this.dimension = dimension;
        this.type = type;
        this.r = radius;
        this.name = name;
        this.desc = desc;
        this.x = entity.getPos().getX();
        this.y = entity.getPos().getY();
        this.z = entity.getPos().getZ();
        this.coordX = (int)x;
        this.coordY = (int)y;
        this.coordZ = (int)z;
        double scale = mp.getMapView().getDimensionScaling(Minecraft.getMinecraft().theWorld.provider.getDimensionType().getId());
        Point2D.Double p = mp.getMapMode().getClampedScreenXY(mp.getMapView(), x * scale, z * scale);
        this.screenPos.setLocation(p.x + mp.getMapMode().getXTranslation(), p.y + mp.getMapMode().getYTranslation());
    }


    public void draw(MapMode mapMode, MapView mapView)
    {
        if (this.type == EntityType.LOOT || this.type == EntityType.SHRINE) {
            drawTile(mapMode, mapView);
            return;
        }
        double scale = mapView.getDimensionScaling(mapView.getDimension());
        Point.Double p = mapMode.getClampedScreenXY(mapView, x * scale, z * scale);

        Render.setColour(this.type.getColor().getRGB());
        Render.drawCircle(p.x, p.y, this.type.getRadius());
        Render.setColour(borderColor);
        Render.drawCircleBorder(p.x, p.y, this.type.getRadius(), borderWidth);
    }

    public void drawPKTexture(MapMode mapMode, MapView mapView, float mapRotationDegrees)
    {
        if (this.type == EntityType.LOOT || this.type == EntityType.SHRINE) {
            drawTile(mapMode, mapView);
            return;
        }

        double scale = mapView.getDimensionScaling(mapView.getDimension());
        Point.Double p = mapMode.getClampedScreenXY(mapView, x * scale, z * scale);

        double markerSize = this.type.getTextureDimension();

        Render.setColour(0xffffffff);
        if (this.sprite != null)
            Minecraft.getMinecraft().renderEngine.bindTexture(this.sprite);
        Render.drawTexturedRect(p.x - markerSize / 2, p.y - markerSize / 2, markerSize, markerSize);
    }

    public void drawTile(MapMode mapMode, MapView mapView)
    {
        double scale = mapView.getDimensionScaling(mapView.getDimension());
        Point.Double p = mapMode.getClampedScreenXY(mapView, x * scale, z * scale);

        Render.setColour(this.type.getColor().getRGB());
        Render.drawRect(p.x, p.y, this.type.getRadius(), this.type.getRadius());
        Render.setColour(borderColor);
        Render.drawRectBorder(p.x, p.y, this.type.getRadius(), this.type.getRadius(), borderWidth * 2);
    }

    public double getDistanceToMarker(Entity entityIn)
    {
        if(entityIn == null)
            return 0D;
        double d0 = this.x - entityIn.posX;
        double d1 = this.y - entityIn.posY;
        double d2 = this.z - entityIn.posZ;
        return MathHelper.sqrt_double((d0 * d0) + (d1 * d1) + (d2 * d2));
    }
}
