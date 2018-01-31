package pokeradar.entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pixelmonmod.pixelmon.blocks.enums.EnumPokeChestType;
import com.pixelmonmod.pixelmon.blocks.enums.EnumPokechestVisibility;
import com.pixelmonmod.pixelmon.blocks.machines.BlockShrine;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityPokeChest;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityShrine;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.*;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.tileentity.TileEntity;
import pokeradar.map.MapRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import pokeradar.map.PKMarker;
import pokeradar.gui.RadarGui;
import pokeradar.util.Reference;
import pokeradar.util.Utils;

public class EntityFinder
{
    public static List<PKMarker> pkMarkers = new ArrayList<PKMarker>();
    public static boolean evolved = false;
    private static List<String> legendaryList = Reference.LEGENDARIES;
    public static void getAndDraw(MapRenderer mp, Minecraft mc)
    {
        if ((mc.world != null) && (mc.world.getLoadedEntityList().size() > 1))
        {
            List<Entity> entityList = mc.world.loadedEntityList;
            List<TileEntity> tileList = mc.world.loadedTileEntityList;
            Iterator entityIt = entityList.iterator();
            Iterator tileIt = tileList.iterator();

            while (entityIt.hasNext()) {
                Object entityObj = entityIt.next();
                if (entityObj instanceof EntityPixelmon)//.getClass().getName().equals("com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon"))
                {
                    EntityPixelmon entity = (EntityPixelmon) entityObj;
                    //System.out.println(entity.getName() + " " + entityIt.);
                    try
                    {
                        Object spawnLocation = entity.getSpawnLocation();
                        Boolean hasOwner = entity.hasOwner();

                        if (!hasOwner) {
                            String name = entity.getPokemonName();
                            EnumBossMode bossMode = entity.getBossMode();
                            Boolean isShiny = entity.getIsShiny();
                            Field levelField = Utils.findUnderlying(entity.getClass(), "level");
                            levelField.setAccessible(true);
                            Level level = (Level) levelField.get(entity);
                            BaseStats baseStats = entity.baseStats;
                            Stats stats = entity.stats;
                            EVsStore eVsStore = null;
                            IVStore ivStore = null;
                            EnumNature nature = entity.getNature();
                            int ivSum = 0;
                            if (baseStats != null) {
                                eVsStore = entity.baseStats.evGain;
                            }
                            if (stats != null) {
                                ivStore = entity.stats.IVs;
                                if (ivStore != null) {
                                    for (int i : ivStore.getArray())
                                        ivSum += i;
                                }
                            }

                            EntityType type = EntityType.REGULAR;
                            String nameLower = name.toLowerCase();

                            String desc = "";
                            String levelStr = "Lvl " + level.getLevel();
                            String ivStr = "IV " + (int) (((double) ivSum / 186) * 100) + "%";
                            String natureStr = nature.getLocalizedName();


                            if (isShiny) {
                                type = EntityType.SHINY;
                                //desc = desc + " (shiny)";
                            }

                            if (RadarGui.searchString != null) {
                                String[] nameArr = RadarGui.searchString.toLowerCase().split(",");
                                for (int na = 0; na < nameArr.length; na++) {
                                    if ((nameArr[na].trim().length() >= 3) &&
                                            (nameLower.contains(nameArr[na].trim()))) {
                                        if (isShiny) {
                                            type = EntityType.SPECIAL;
                                        } else {
                                            type = EntityType.SEARCHED;
                                        }
                                    }
                                }
                            }

                            if (RadarGui.getEV() != null && eVsStore != null) {
                                Boolean badEV = false;
                                if (eVsStore.HP > 0 && !RadarGui.getEV().equals(EVType.HP))
                                    badEV = true;
                                if (eVsStore.Attack > 0 && !RadarGui.getEV().equals(EVType.Attack))
                                    badEV = true;
                                if (eVsStore.Defence > 0 && !RadarGui.getEV().equals(EVType.Defense))
                                    badEV = true;
                                if (eVsStore.SpecialAttack > 0 && !RadarGui.getEV().equals(EVType.SpecialAttack))
                                    badEV = true;
                                if (eVsStore.SpecialDefence > 0 && !RadarGui.getEV().equals(EVType.SpecialDefense))
                                    badEV = true;
                                if (eVsStore.Speed > 0 && !RadarGui.getEV().equals(EVType.Speed))
                                    badEV = true;

                                if (badEV) {
                                    type = (isShiny) ? type : EntityType.REGULAR;
                                } else {
                                    if (isShiny) {
                                        type = EntityType.SPECIAL;
                                    } else {
                                        type = EntityType.SEARCHED;
                                    }
                                }
                            }

                            if (RadarGui.getNature() != null && nature != null &&
                                    (type == EntityType.SEARCHED || type == EntityType.SPECIAL))
                            {
                                boolean foundNature = false;
                                if (RadarGui.getNature().equals(nature)) {
                                    foundNature = true;
                                }

                                if (!foundNature) {
                                    type = (isShiny) ? type : EntityType.REGULAR;
                                } else {
                                    if (isShiny) {
                                        type = EntityType.SPECIAL;
                                    } else {
                                        type = EntityType.SEARCHED;
                                    }
                                }
                            }

                            if (legendaryList.contains(name)) {
                                type = EntityType.SPECIAL;
                                desc = "Legendary";
                            }

                            if (bossMode != EnumBossMode.NotBoss && bossMode != EnumBossMode.Equal) {
                                switch (bossMode) {
                                    case Uncommon:
                                        type = EntityType.BOSS_UNCOMMON;
                                        break;
                                    case Rare:
                                        type = EntityType.BOSS_RARE;
                                        break;
                                    case Legendary:
                                        type = EntityType.BOSS_LEGENDARY;
                                        break;
                                    case Ultimate:
                                        type = EntityType.BOSS_ULTIMATE;
                                        break;
                                }

                                desc += ((desc.isEmpty()) ? "" : " ") + bossMode.toString() + " Boss";
                            } else {
                                //name = name + " " + levelStr + ((isShiny) ? " (shiny)" : "");
                                desc += ((desc.isEmpty()) ? "" : " ") + levelStr;
                            }

                            //desc += ((desc.isEmpty()) ? "" : " ") + natureStr;

                            if ((spawnLocation != null) &&
                                    (spawnLocation.toString().equals("Statue"))) {
                                type = EntityType.STATUE;
                                desc = "Statue";
                            }

                            pkMarkers.add(new PKMarker(type, entity, name, desc, type.getRadius(), mp, entity.dimension));
                        }
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (entityObj instanceof EntityOtherPlayerMP)
                {
                    try
                    {
                        EntityOtherPlayerMP entity = (EntityOtherPlayerMP) entityObj;
                        pkMarkers.add(new PKMarker(EntityType.PLAYER, entity, entity.getDisplayNameString(), "Player", EntityType.PLAYER.getRadius(), mp, entity.dimension));
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (entityObj instanceof NPCTrainer)
                {
                    try
                    {
                        NPCTrainer entity = (NPCTrainer) entityObj;
                        pkMarkers.add(new PKMarker(EntityType.NPC, entity, "NPC Trainer", "Level: " + entity.getDisplayText(), EntityType.NPC.getRadius(), mp, entity.dimension));
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (entityObj instanceof EntityNPC)
                {
                    try
                    {
                        EntityNPC entity = (EntityNPC) entityObj;
                        pkMarkers.add(new PKMarker(EntityType.NPC, entity, entity.getDisplayText(), "NPC", EntityType.NPC.getRadius(), mp, entity.dimension));
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            while (tileIt.hasNext()) {
                Object tileObj = tileIt.next();
                if (tileObj instanceof TileEntityPokeChest) {
                    TileEntityPokeChest tile = (TileEntityPokeChest) tileObj;
                    EnumPokeChestType chestType = tile.getType();
                    String desc = "";
                    switch(chestType) {
                        case SPECIAL:
                            desc = "Special";
                            break;
                        case POKEBALL:
                            desc = "Tier 1";
                            break;
                        case ULTRABALL:
                            desc = "Tier 2";
                            break;
                        case MASTERBALL:
                            desc = "Tier 3";
                            break;
                    }
                    if(tile.getVisibility() == EnumPokechestVisibility.Hidden){
                        desc = "Hidden";
                    }
                    pkMarkers.add(new PKMarker(EntityType.LOOT, tile, "PokeLoot", desc, EntityType.LOOT.getRadius(), mp, mc.world.provider.getDimension()));
                    //lootMarker.desc = " (" + (int) lootMarker.getDistanceToMarker(mc.thePlayer) + "m)" ;
                    //pkMarkers.add(lootMarker);
                }
                if (tileObj instanceof TileEntityShrine) {
                    TileEntityShrine tile = (TileEntityShrine) tileObj;
                    BlockShrine blockShrine = (BlockShrine) tile.getBlockType();
                    String type = "";
                    if (blockShrine != null && blockShrine.rockType != null)
                        type = blockShrine.rockType.toString();
                    pkMarkers.add(new PKMarker(EntityType.SHRINE, tile, "PokeShrine", type, EntityType.SHRINE.getRadius(), mp, mc.world.provider.getDimension()));
                    //shrineMarker.desc = " (" + (int) shrineMarker.getDistanceToMarker(mc.thePlayer) + "m)" ;
                    //pkMarkers.add(shrineMarker);
                }
            }
        }
    }

}
