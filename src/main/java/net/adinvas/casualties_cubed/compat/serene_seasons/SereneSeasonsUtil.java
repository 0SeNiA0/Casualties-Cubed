package net.adinvas.casualties_cubed.compat.serene_seasons;

import net.adinvas.casualties_cubed.compat.TempCompat;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;
import sereneseasons.api.season.SeasonHelper;

public class SereneSeasonsUtil {

    public static boolean isLoaded(){
        return ModList.get().isLoaded("sereneseasons");
    }

    public static float getSeasonScale(Level level, TempCompat.BiomeTemperatureEntry entry){
        if (!isLoaded()) return entry.temperature;

        ISeasonState seasonState = SeasonHelper.getSeasonState(level);
        Season.SubSeason sub = seasonState.getSubSeason();

        int subIndex = sub.ordinal();

        int seasonIndex = subIndex / 3;
        float blend = (subIndex % 3) / 3f;

        Season[] seasons = Season.values();
        Season currentSeason = seasons[seasonIndex];
        Season nextSeason = seasons[(seasonIndex+1)%4];

        float startTemp = switch (currentSeason){
            case WINTER -> entry.winter;
            case SUMMER -> entry.summer;
            case AUTUMN -> entry.fall;
            case SPRING -> entry.spring;
        };

        float targetTemp =switch (nextSeason){
            case WINTER -> entry.winter;
            case SUMMER -> entry.summer;
            case AUTUMN -> entry.fall;
            case SPRING -> entry.spring;
        };

        return Mth.lerp(blend,startTemp,targetTemp);
    }
}
