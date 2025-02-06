package org.felixWegener.tweakmod.SignActionStuff;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Objects;

public class SignActions {

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
            if (Objects.equals(text, "teleport")) {
                TeleportAction.TeleportPlayerAction(lines, player, world);
            }

            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

}
