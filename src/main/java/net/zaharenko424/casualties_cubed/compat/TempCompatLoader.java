package net.zaharenko424.casualties_cubed.compat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.floats.FloatObjectPair;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TempCompatLoader extends SimpleJsonResourceReloadListener {

    public static final TempCompatLoader INSTANCE = new TempCompatLoader();

    public TempCompatLoader() {
        super(new Gson(), "casualties_cubed");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> files, ResourceManager manager, ProfilerFiller profilerFiller) {
        TempCompat.clearBlock();
        TempCompat.clearBiome();
        files.forEach((fileLocation, jsonElement) -> {
            if (!fileLocation.getPath().equals("temperature_blocks") && !fileLocation.getPath().equals("temperature_biomes"))
                return;

            if (fileLocation.getPath().equals("temperature_blocks")) {
                JsonObject json = jsonElement.getAsJsonObject();
                for (String key : json.keySet()) {
                    try {
                        ResourceLocation blockId = ResourceLocation.parse(key);
                        JsonElement element = json.get(key);

                        if (element.isJsonPrimitive()) {
                            TempCompat.addEntryBlock(blockId, Either.left(element.getAsFloat()));
                        } else {
                            JsonArray arr;
                            if (element.isJsonArray()) {
                                arr = element.getAsJsonArray();
                            } else {
                                arr = new JsonArray();
                                arr.add(element);
                            }

                            List<FloatObjectPair<StatePropertiesPredicate>> list = new ArrayList<>();
                            JsonObject obj;
                            for (JsonElement e : arr) {
                                obj = e.getAsJsonObject();
                                list.add(FloatObjectPair.of(obj.get("temperature").getAsFloat(),
                                        StatePropertiesPredicate.fromJson(obj.get("properties"))));
                            }

                            TempCompat.addEntryBlock(blockId, Either.right(List.copyOf(list)));
                        }
                    } catch (Exception e) {
                        System.err.println("[Prototype Pain] Failed to parse temperatureblock compat entry: " + key);
                    }
                }
            } else if (fileLocation.getPath().equals("temperature_biomes")) {
                JsonObject json = jsonElement.getAsJsonObject();
                for (String key : json.keySet()) {
                    try {
                        ResourceLocation blockId = ResourceLocation.parse(key);
                        JsonObject obj = json.getAsJsonObject(key);
                        float day = obj.has("temperature") ? obj.get("temperature").getAsFloat() : null;
                        float night = obj.has("nightChange") ? obj.get("nightChange").getAsFloat() : 0;
                        float spring = obj.has("spring") ? obj.get("spring").getAsFloat() : day;
                        float summer = obj.has("summer") ? obj.get("summer").getAsFloat() : day;
                        float fall = obj.has("fall") ? obj.get("fall").getAsFloat() : day;
                        float winter = obj.has("winter") ? obj.get("winter").getAsFloat() : day;

                        if (!Float.isNaN(day)) {
                            TempCompat.addEntryBiome(blockId, new TempCompat.BiomeTemperatureEntry(day, night, spring, summer, fall, winter));
                        }
                    } catch (Exception e) {
                        System.err.println("[Prototype Pain] Failed to parse temperaturebiome compat entry: " + key);
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onReload(AddReloadListenerEvent event) {
        event.addListener(INSTANCE);
    }
}
