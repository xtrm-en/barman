package me.xtrm.barman.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.xtrm.barman.Barman;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MutableWorldProperties;

import java.util.function.Consumer;

/**
 * @author xtrm
 */
public class SpawnCommand implements Consumer<CommandDispatcher<ServerCommandSource>> {
    @Override
    public void accept(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<ServerCommandSource>literal("setspawn")
                        .requires(Permissions.require("barman.command.setspawn", false))
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayerOrThrow();
                            float angle = player.headYaw;
                            BlockPos pos = player.getBlockPos();
                            MutableWorldProperties mutableWorldProperties = (MutableWorldProperties) source.getWorld().getLevelProperties();
                            mutableWorldProperties.setSpawnPos(pos, angle);

                            Barman.CONFIG.spawnPos = pos;
                            Barman.CONFIG.spawnAngle = angle;
                            Barman.saveConfig();

                            return 1;
                        })
        );

        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("spawn").executes(context -> {
            ServerCommandSource source = context.getSource();
            Entity entity = source.getEntityOrThrow();
            BlockPos spawnPos = Barman.CONFIG.spawnPos;
            entity.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            entity.setYaw(Barman.CONFIG.spawnAngle);
            entity.setHeadYaw(Barman.CONFIG.spawnAngle);
            entity.setPitch(0);
            return 1;
        }));
    }
}
