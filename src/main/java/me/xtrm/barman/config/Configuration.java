package me.xtrm.barman.config;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;

/**
 * @author xtrm
 */
public class Configuration {
    public BlockPos spawnPos = BlockPos.ORIGIN;
    public float spawnAngle = Float.MAX_VALUE;

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
