package org.felixWegener.tweakmod.shulkerBoxOpener;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ShulkerBoxOpener {

    public static void register() {
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            openShulker(playerEntity);
            return null;
        });
    }

    private static void openShulker(PlayerEntity player) {
        int selectedSlot = player.getInventory().selectedSlot;
        ItemStack item = player.getInventory().getStack(selectedSlot);

        if (item.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock shulkerBox) {
            ContainerComponent container = item.get(DataComponentTypes.CONTAINER);
            SimpleInventory simpleInventory = new SimpleInventory(27);

            if (container != null) {
                int currentSlot = 0;

                for (ItemStack stack : container.iterateNonEmpty()) {
                    simpleInventory.setStack(currentSlot, stack);
                    currentSlot++;
                }

                player.openHandledScreen(new NamedScreenHandlerFactory() {
                    @Override
                    public Text getDisplayName() {
                        return Text.literal(item.getName().getString());
                    }

                    @Override
                    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                        //return new CustomShulkerScreenHandler(syncId, player.getInventory(), simpleInventory, item);
                        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, player.getInventory(), simpleInventory, 3);
                    }
                });

            }
        }
    }

}
