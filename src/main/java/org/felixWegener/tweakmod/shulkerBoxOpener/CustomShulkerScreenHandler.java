package org.felixWegener.tweakmod.shulkerBoxOpener;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;

public abstract class CustomShulkerScreenHandler extends ScreenHandler {
    private final ItemStack shulkerBox;
    private final SimpleInventory inventory;

    public CustomShulkerScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack shulkerBox) {
        super(null, syncId); // Hier den richtigen ScreenHandlerType einsetzen
        this.shulkerBox = shulkerBox;

        // Lade die Items aus der Shulker-Box
        ContainerComponent container = shulkerBox.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        this.inventory = new SimpleInventory(27); // Standardgröße einer Shulker-Box
        //container.copyTo(this.inventory.stacks);

        // Füge Slots für die Shulker-Kiste hinzu
        for (int i = 0; i < 27; i++) {
            this.addSlot(new Slot(inventory, i, 8 + (i % 9) * 18, 18 + (i / 9) * 18));
        }

        // Füge Slots für das Spieler-Inventar hinzu
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Füge Slots für die Hotbar hinzu
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        saveInventory();
    }

    private void saveInventory() {
        // Speichert den Inhalt der Shulker-Kiste beim Schließen
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
        for (int i = 0; i < 27; i++) {
            stacks.set(i, inventory.getStack(i).copy());
        }

        ContainerComponent newContainer = ContainerComponent.fromStacks(stacks);
        shulkerBox.set(DataComponentTypes.CONTAINER, newContainer);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}
