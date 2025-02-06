package org.felixWegener.tweakmod.gravestone;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class GraveStone {

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, source, amount) -> {
            if (entity instanceof ServerPlayerEntity player) {
                storeInventoryInChest(player);
                return true;
            }
            return true;
        });
    }

    private static void storeInventoryInChest(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos();

        world.setBlockState(pos, Blocks.CHEST.getDefaultState());
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof ChestBlockEntity chest) {
            DefaultedList<ItemStack> mainInventory = player.getInventory().main;
            DefaultedList<ItemStack> armorInventory = player.getInventory().armor;
            DefaultedList<ItemStack> offHandInventory = player.getInventory().offHand;

            int slot = 0;

            for (ItemStack stack : mainInventory) {
                if (!stack.isEmpty() && slot < chest.size()) {
                    chest.setStack(slot++, stack);
                }
            }

            for (ItemStack stack : armorInventory) {
                if (!stack.isEmpty() && slot < chest.size()) {
                    chest.setStack(slot++, stack);
                }
            }

            for (ItemStack stack : offHandInventory) {
                if (!stack.isEmpty() && slot < chest.size()) {
                    chest.setStack(slot++, stack);
                }
            }

            player.getInventory().clear();
        }
    }

}
