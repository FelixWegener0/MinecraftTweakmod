package org.felixWegener.tweakmod;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;

public class ShulkerBoxOpener {

    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack itemStack = player.getStackInHand(hand);
            System.out.println("in UseItemCallback event");

            if (!world.isClient) {
                System.out.println("world is server");
                // Prüfen, ob der Spieler eine Shulkerbox in der Hand hat
                if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().asItem() == Items.SHULKER_BOX) {

                    System.out.println(Items.SHULKER_BOX);


                    openShulkerBox((ServerPlayerEntity) player, itemStack);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }

    private static void openShulkerBox(ServerPlayerEntity player, ItemStack shulkerBox) {
        Inventory shulkerInventory = new SimpleInventory(27);

        // Inhalt der Shulkerbox auslesen


        // Shulkerbox öffnen
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, playerEntity) ->
                        new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inventory, shulkerInventory, 3),
                Text.literal("Shulker Box")
        ));
    }

}
