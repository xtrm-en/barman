package me.xtrm.barman;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xtrm
 */
public class BarmanServerInitializer implements DedicatedServerModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("barman");

	@Override
	public void onInitializeServer() {
		LOGGER.info("Shaking cocktails...");


	}
}
