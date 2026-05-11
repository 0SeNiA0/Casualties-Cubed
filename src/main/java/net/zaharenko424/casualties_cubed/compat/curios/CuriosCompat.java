package net.zaharenko424.casualties_cubed.compat.curios;

import net.minecraftforge.fml.ModList;

public class CuriosCompat {

    public static boolean isLoaded(){
        return ModList.get().isLoaded("curioApi");
    }
}
