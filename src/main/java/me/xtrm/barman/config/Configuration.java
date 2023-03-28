package me.xtrm.barman.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xtrm
 */
public class Configuration {
    public boolean endEnabled = false;
    public boolean killEndItemFrames = true;
    public BlockPos spawnPos = BlockPos.ORIGIN;
    public float spawnAngle = Float.MAX_VALUE;
    public Identifier worldIdentifier = World.OVERWORLD.getRegistry();
    public Map<UUID, Long> spawnTimeouts = new HashMap<>();

    public void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            World world = server.getWorld(World.OVERWORLD);
            assert world != null;
            WorldProperties worldProperties = world.getLevelProperties();
            MutableWorldProperties mutableWorldProperties = (MutableWorldProperties) worldProperties;

            if (spawnPos == BlockPos.ORIGIN) {
                spawnPos = new BlockPos(worldProperties.getSpawnX(), worldProperties.getSpawnY(), worldProperties.getSpawnZ());
            }
            if (spawnAngle == Float.MAX_VALUE) {
                spawnAngle = worldProperties.getSpawnAngle();
            }

            mutableWorldProperties.setSpawnPos(spawnPos, spawnAngle);
        });
    }
}
