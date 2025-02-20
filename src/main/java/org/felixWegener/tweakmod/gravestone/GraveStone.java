package org.felixWegener.tweakmod.gravestone;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class GraveStone {

    public static void register() {
        System.out.println("register gravestone mod");
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
        BlockPos pos2 = player.getBlockPos().add(0, 1, 0);

        world.setBlockState(pos, Blocks.CHEST.getDefaultState());
        world.setBlockState(pos2, Blocks.CHEST.getDefaultState());

        BlockEntity blockEntity = world.getBlockEntity(pos);
        BlockEntity blockEntity2 = world.getBlockEntity(pos2);

        if (blockEntity instanceof ChestBlockEntity chest && blockEntity2 instanceof ChestBlockEntity chest2) {
            DefaultedList<ItemStack> mainInventory = player.getInventory().main;
            DefaultedList<ItemStack> armorInventory = player.getInventory().armor;
            DefaultedList<ItemStack> offHandInventory = player.getInventory().offHand;

            int chestSize = chest.size();
            int slot = 0;
            int slotChest2 = 0;

            for (ItemStack stack : mainInventory) {
                if (!stack.isEmpty()) {
                    if (slot < chestSize) {
                        chest.setStack(slot++, stack);
                    } else {
                        chest2.setStack(slotChest2++, stack);
                    }
                }
            }

            for (ItemStack stack : armorInventory) {
                if (!stack.isEmpty()) {
                    if (slot < chestSize) {
                        chest.setStack(slot++, stack);
                    } else {
                        chest2.setStack(slotChest2++, stack);
                    }
                }
            }

            for (ItemStack stack : offHandInventory) {
                if (!stack.isEmpty()) {
                    if (slot < chestSize) {
                        chest.setStack(slot++, stack);
                    } else {
                        chest2.setStack(slotChest2, stack);
                    }
                }
            }

            if (slot < chestSize) world.setBlockState(pos2, Blocks.AIR.getDefaultState());
            player.getInventory().clear();
            player.sendMessage(Text.literal("Death position: " + pos.getX() + ' ' + pos.getY() + ' ' + pos.getZ()).formatted(Formatting.AQUA), false);
        }
    }

}
