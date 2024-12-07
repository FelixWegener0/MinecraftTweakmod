package org.felixWegener.tweakmod;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Objects;

public class SignActions {

    private static final boolean enableTeleport = true;
    private static final boolean enableChunkLoading = false;

    public static void register() {
        System.out.println("register");
        UseBlockCallback.EVENT.register(SignActions::onUseBlock);
    }

    private static ActionResult onUseBlock(PlayerEntity player, World world, Hand pos, BlockHitResult state) {
        BlockPos position = state.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(position);

        if (blockEntity instanceof SignBlockEntity signBlockEntity) {

            SignText content = signBlockEntity.getText(true);
            Text[] lines = content.getMessages(true);
            String text = lines[0].getString();

            // Player Teleport Signs
            if (enableTeleport && Objects.equals(text, "teleport")) {

                double x = Double.parseDouble(lines[1].getString());
                double y = Double.parseDouble(lines[2].getString());
                double z = Double.parseDouble(lines[3].getString());
                BlockPos targetBlock = new BlockPos((int) x, (int) y, (int) z);

                int cost = 0;
                double distance = calculateDistanceXZ(targetBlock, player.getBlockPos());

                if (distance > 100 && distance < 1000) cost = 5;
                if (distance > 1000 && distance < 10000) cost = 10;
                if (distance >= 10000) cost = 20;

                if (player.experienceLevel >= cost) {
                    player.experienceLevel = player.experienceLevel - cost;
                    player.addExperience(0);

                    player.sendMessage(Text.literal("Du wirst für " + cost + " level teleportiert").formatted(Formatting.AQUA), false);

                    ChunkPos targetChunk = new ChunkPos(targetBlock);
                    world.getChunk(targetChunk.x, targetChunk.z);
                    player.teleport(x, y, z, false);
                } else {
                    player.sendMessage(Text.literal("Du hast nicht genug level für einen Teleport, benötigte level: " + cost).formatted(Formatting.AQUA), false);
                }
            }
            if (enableChunkLoading && Objects.equals(text, "chunkLoader")) {
                if (world instanceof ServerWorld serverWorld) {
                    loadGivenChunk(serverWorld, position);
                }
            }

            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

    public static void loadGivenChunk(ServerWorld world, BlockPos position) {
        ChunkPos chunkPosition = new ChunkPos(position);
        world.getChunkManager().addTicket(ChunkTicketType.START, chunkPosition, 1, null);
        world.getChunk(chunkPosition.x, chunkPosition.z).updateAllBlockEntities();

        System.out.println("Chunk loaded at: " + position.getX() + " " + position.getZ());
    }

    public static void unloadGivenChunk(ServerWorld world, BlockPos position) {
        ChunkPos chunkPosition = new ChunkPos(position);
        world.getChunkManager().removeTicket(ChunkTicketType.START, chunkPosition, 1, null);

        System.out.println("Chunk unloaded at: " + position.getX() + " " + position.getZ());
    }

    public static double calculateDistanceXZ(BlockPos playerPos, BlockPos targetPos) {
        double deltaX = playerPos.getX() - targetPos.getX();
        double deltaZ = playerPos.getZ() - targetPos.getZ();

        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

}
