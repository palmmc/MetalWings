package xyz.ashyboxy.mc.metalwings;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

public class WorldConfig extends SavedData {
    public StorageMode storageMode = StorageMode.BUNDLE_CONTENTS;

    private static final Factory<WorldConfig> factory = new Factory<>(WorldConfig::new, WorldConfig::load, null);

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putString("storageMode", storageMode.name());
        return nbt;
    }

    public static WorldConfig load(CompoundTag nbt, HolderLookup.Provider provider) {
        WorldConfig config = new WorldConfig();
        try {
            config.storageMode = StorageMode.valueOf(nbt.getString("storageMode"));
        } catch (IllegalArgumentException ignored) {}
        return config;
    }

    public static WorldConfig getConfig(MinecraftServer server) {
        DimensionDataStorage dataStorage = server.overworld().getDataStorage();
        WorldConfig config = dataStorage.computeIfAbsent(factory, MetalWings.MOD_ID);
        config.setDirty();
        return config;
    }
}
