package xyz.ashyboxy.mc.metalwings;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.*;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class MetalWingsCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx,
                                Commands.CommandSelection env) {
        var baseCmd = Commands.literal(MetalWings.MOD_ID)
                .requires(s -> s.getServer().isSingleplayer() || s.hasPermission(2))
                .executes(MetalWingsCommands::baseCmd)
                .build();
        var storageModeCmd = Commands.literal("storageMode")
                .build();
        var storageModeArg = Commands.argument("storageMode", StringArgumentType.word())
                .suggests((ctx2, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(StorageMode.values()).map(Enum::name), builder))
                .executes(MetalWingsCommands::storageModeArg)
                .build();

        dispatcher.getRoot().addChild(baseCmd);
        baseCmd.addChild(storageModeCmd);
        storageModeCmd.addChild(storageModeArg);
    }

    public static int baseCmd(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSystemMessage(Component.literal(
                "storageMode: " + WorldConfig.getConfig(ctx.getSource().getServer()).storageMode));
        return 0;
    }

    public static int storageModeArg(CommandContext<CommandSourceStack> ctx) {
        String strMode = StringArgumentType.getString(ctx, "storageMode");
        StorageMode mode;
        try {
            mode = StorageMode.valueOf(strMode);
        } catch (IllegalArgumentException e) {
            ctx.getSource().sendFailure(Component.literal(strMode + " is not a valid storage mode"));
            return -1;
        }
        WorldConfig.getConfig(ctx.getSource().getServer()).storageMode = mode;
        ctx.getSource().sendSuccess(() -> Component.literal("Set Metal Wings storage mode to " + strMode), true);
        return 1;
    }
}
