package org.felixWegener.tweakmod;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class TeleportAction {

    public static void TeleportPlayerAction(Text[] lines, PlayerEntity player, World world) {
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

            ChunkPos targetChunk = new ChunkPos(targetBlock);
            world.getChunk(targetChunk.x, targetChunk.z);

            if (player.teleport(x, y, z, false)) {
                player.experienceLevel = player.experienceLevel - cost;
                player.addExperience(0);
                player.sendMessage(Text.literal("Du wirst für " + cost + " Level Teleportiert").formatted(Formatting.AQUA), false);
            } else {
                player.sendMessage(Text.literal("Du kannst nicht zum Ziel Teleportiert werden").formatted(Formatting.AQUA), false);
            }

        } else {
            player.sendMessage(Text.literal("Du hast nicht genug level für einen Teleport, benötigte level: " + cost).formatted(Formatting.AQUA), false);
        }
    }

    public static double calculateDistanceXZ(BlockPos playerPos, BlockPos targetPos) {
        double deltaX = playerPos.getX() - targetPos.getX();
        double deltaZ = playerPos.getZ() - targetPos.getZ();

        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

}
