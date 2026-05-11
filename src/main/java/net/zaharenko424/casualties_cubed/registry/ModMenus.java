package net.zaharenko424.casualties_cubed.registry;

import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.menu.MedicalMixerMenu;
import net.zaharenko424.casualties_cubed.menu.LargeMedibagMenu;
import net.zaharenko424.casualties_cubed.menu.MediumMedibagMenu;
import net.zaharenko424.casualties_cubed.menu.SmallMedibagMenu;
import net.zaharenko424.casualties_cubed.menu.LootPlayerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CasualtiesCubed.MOD_ID);

    public static final RegistryObject<MenuType<SmallMedibagMenu>> SMALL_MEDIBAG =
            MENUS.register("small_medibag",
                    () -> IForgeMenuType.create((id, inv, buf) -> {
                        boolean mainHand = buf.readBoolean();
                        ItemStack stack = mainHand ? inv.player.getMainHandItem() : inv.player.getOffhandItem();
                        return new SmallMedibagMenu(id, inv, stack);
                    }));

    public static final RegistryObject<MenuType<MediumMedibagMenu>> MEDIUM_MEDIBAG =
            MENUS.register("medium_medibag",
                    () -> IForgeMenuType.create((id, inv, buf) -> {
                        boolean mainHand = buf.readBoolean();
                        ItemStack stack = mainHand ? inv.player.getMainHandItem() : inv.player.getOffhandItem();
                        return new MediumMedibagMenu(id, inv, stack);
                    }));

    public static final RegistryObject<MenuType<LargeMedibagMenu>> LARGE_MEDIBAG =
            MENUS.register("large_medibag",
                    () -> IForgeMenuType.create((id, inv, buf) -> {
                        boolean mainHand = buf.readBoolean();
                        ItemStack stack = mainHand ? inv.player.getMainHandItem() : inv.player.getOffhandItem();
                        return new LargeMedibagMenu(id, inv, stack);
                    }));

    public static final RegistryObject<MenuType<LootPlayerMenu>> LOOT_PLAYER =
            MENUS.register("loot_player", () -> IForgeMenuType.create((id, inv, data) -> {
                int targetId = data.readVarInt();
                Player target = (Player) inv.player.level().getEntity(targetId);
                return new LootPlayerMenu(id, inv, target);
            }));


    public static final RegistryObject<MenuType<MedicalMixerMenu>> MEDICAL_MIXER =
            registerMenuType("medical_mixer_menu",MedicalMixerMenu::new);


    public static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory){
        return MENUS.register(name, ()->IForgeMenuType.create(factory));
    }
}
