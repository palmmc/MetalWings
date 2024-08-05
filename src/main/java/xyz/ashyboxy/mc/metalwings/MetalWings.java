package xyz.ashyboxy.mc.metalwings;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetalWings implements ModInitializer {
	public static final String MOD_ID = "metalwings";
    public static final Logger LOGGER = LoggerFactory.getLogger("Metal Wings");

	@Override
	public void onInitialize() {
		LOGGER.info("want some fancy elytra? uwu~");
		CommandRegistrationCallback.EVENT.register(MetalWingsCommands::register);
	}

    public static ResourceLocation id(String path) {
		// tryBuild is in both 1.20.6 and 1.21, but i don't want it to silently fail
		ResourceLocation rl = ResourceLocation.tryBuild(MOD_ID, path);
		if (rl == null) throw new ResourceLocationException("Invalid character in location: " + MOD_ID + ":" + path);
		return rl;
	}
}
