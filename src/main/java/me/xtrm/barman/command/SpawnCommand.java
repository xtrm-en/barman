package me.xtrm.barman.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.xtrm.barman.Barman;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author xtrm
 */
public class SpawnCommand implements Consumer<CommandDispatcher<ServerCommandSource>> {
    @Override
    public void accept(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("setspawn")
                .requires(Permissions.require("barman.command.spawn.set", 4))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayerOrThrow();

                    float angle = player.headYaw;
                    BlockPos pos = player.getBlockPos();
                    MutableWorldProperties mutableWorldProperties = (MutableWorldProperties) source.getWorld().getLevelProperties();
                    mutableWorldProperties.setSpawnPos(pos, angle);

                    player.sendMessage(Text.literal("Spawn set to " + pos + " with angle " + angle).formatted(Formatting.GREEN), false);

                    Barman.CONFIG.spawnPos = pos;
                    Barman.CONFIG.spawnAngle = angle;
                    Barman.saveConfig();

                    return 1;
                })
        );

        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("spawn")
                .requires(Permissions.require("barman.command.spawn", 4))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    Entity entity = source.getEntityOrThrow();

                    UUID uuid = entity.getUuid();
                    long now = System.currentTimeMillis();
                    long lastSpawn = Barman.CONFIG.spawnTimeouts.getOrDefault(uuid, 0L);
                    long diff = now - lastSpawn;
                    long TIMEOUT_DURATION = 1000 * 60 * 60 * 24;
                    boolean isTimedout = diff < TIMEOUT_DURATION;
                    boolean canBypass = entity instanceof ServerPlayerEntity && ((ServerPlayerEntity) entity).getAbilities().creativeMode;
                    if (isTimedout && !canBypass) {
                        String time = String.format("%02d:%02d:%02d", (24 - diff / 1000 / 60 / 60), (60 - diff / 1000 / 60) % 60, (60 - diff / 1000) % 60);
                        entity.sendMessage(Text.literal("Vous ne pouvez pas utiliser cette commande! Veuillez attendre " + time + ".").formatted(Formatting.RED));
                        return 1;
                    }

                    Barman.CONFIG.spawnTimeouts.put(uuid, now);
                    Barman.saveConfig();

                    Identifier world = Barman.CONFIG.worldIdentifier;
                    if (!world.equals(entity.getEntityWorld().getRegistryKey().getValue())) {
                        Entity temp = entity.moveToWorld(context.getSource().getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, world)));
                        if (temp != null) entity = temp;
                    }

                    BlockPos spawnPos = Barman.CONFIG.spawnPos;
                    entity.teleport(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                    entity.setYaw(Barman.CONFIG.spawnAngle);
                    entity.setPitch(0);

                    entity.sendMessage(Text.literal("Teleported to spawn!").formatted(Formatting.GREEN));

                    return 1;
                })
        );
    }
}
