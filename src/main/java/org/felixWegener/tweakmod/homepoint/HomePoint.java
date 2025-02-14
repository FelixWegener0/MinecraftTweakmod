package org.felixWegener.tweakmod.homepoint;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;


public class HomePoint {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("home").executes(context -> {
                teleportPlayertoSpawn(context);
                return 1;
            }));
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("player_tp")
                    .then(CommandManager.argument("X", IntegerArgumentType.integer())
                        .then(CommandManager.argument("Y", IntegerArgumentType.integer())
                            .then(CommandManager.argument("Z", IntegerArgumentType.integer())
                                .executes(HomePoint::teleportPlayer)
                            )
                        )
                    )
            );
        });
    }

    private static int teleportPlayer(CommandContext<ServerCommandSource> context) {
        int x = IntegerArgumentType.getInteger(context, "X");
        int y = IntegerArgumentType.getInteger(context, "Y");
        int z = IntegerArgumentType.getInteger(context, "Z");
        ServerPlayerEntity player = context.getSource().getPlayer();

        ChunkPos targetChunk = new ChunkPos(x, z);
        assert player != null;
        player.getServerWorld().getChunk(targetChunk.x, targetChunk.z);
        Inventory inventory = player.getInventory();
        int selectedSlot = player.getInventory().selectedSlot;

        if (inventory.getStack(selectedSlot).getItem() == Items.NETHER_STAR) {
            if (player.teleport(x + 0.5, y, z + 0.5, false)) {
                context.getSource().sendFeedback(() -> Text.literal("Du wirst teleportiert").formatted(Formatting.AQUA), false);
                inventory.setStack(selectedSlot, ItemStack.EMPTY);
            } else {
                context.getSource().sendFeedback(() -> Text.literal("Du kannst nicht teleportiert werden").formatted(Formatting.AQUA), false);
            }
        } else {
            context.getSource().sendFeedback(() -> Text.literal("Du musst einen Nether Star in der Hand halten zum teleportieren").formatted(Formatting.AQUA), false);
        }

        return 1;
    }

    private static void teleportPlayertoSpawn(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        assert player != null;
        BlockPos spawnPoint = player.getSpawnPointPosition();
        ServerWorld world = player.getServerWorld();

        ChunkPos targetChunk = new ChunkPos(spawnPoint);
        world.getChunk(targetChunk.x, targetChunk.z);

        double add_x = 0.5;
        double add_y = 0.56250;
        double add_z = 0.5;

        BlockState respawnBlock = world.getBlockState(spawnPoint);
        if (respawnBlock.getBlock() == Blocks.RESPAWN_ANCHOR) {
            add_y = 1;
        }

        if (player.teleport(spawnPoint.getX() + add_x, spawnPoint.getY() + add_y, spawnPoint.getZ() + add_z, false)) {
            context.getSource().sendFeedback(() -> Text.literal("Du wirst teleportiert").formatted(Formatting.AQUA), false);
        } else {
            context.getSource().sendFeedback(() -> Text.literal("Du kannst nicht teleportiert werden"+ spawnPoint).formatted(Formatting.AQUA), false);
        }
    }

}
