package org.felixWegener.tweakmod.enderChestOpener;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public class EnderChestOpener {

    public static void register() {
        System.out.println("register Ender Chest opener mod");
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {

            if (!checkItem(playerEntity)) {
                return ActionResult.PASS;
            }

//            ItemStack item = new ItemStack(Items.NETHERITE_PICKAXE);
//            item.setDamage(0);

            EnderChestInventory enderChest = playerEntity.getEnderChestInventory();
            playerEntity.openHandledScreen(new NamedScreenHandlerFactory() {
                @Override
                public Text getDisplayName() {
                    return Text.literal("Portable Ender Chest").formatted(Formatting.ITALIC, Formatting.DARK_AQUA);
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, playerInventory, enderChest, 3);
                }
            });

            return ActionResult.SUCCESS;
        });
    }

    private static boolean checkItem(PlayerEntity player) {
        int currentSlot = player.getInventory().selectedSlot;
        ItemStack itemStack = player.getInventory().getStack(currentSlot);

        return itemStack.getItem().equals(Items.NETHER_STAR);
    }

}
