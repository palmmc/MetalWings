package xyz.ashyboxy.mc.metalwings;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetalWings implements ModInitializer {
	public static final String MOD_ID = "metalwings";
    public static final Logger LOGGER = LoggerFactory.getLogger("Metal Wings");

	@Override
	public void onInitialize() {
		LOGGER.info("want some fancy elytra? uwu~");
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
