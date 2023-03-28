package me.xtrm.barman.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import me.xtrm.barman.Barman;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

/**
 * @author xtrm
 */
public class ToggleEndCommand implements Consumer<CommandDispatcher<ServerCommandSource>> {
    @Override
    public void accept(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("toggleend")
                .requires(Permissions.require("barman.command.toggleend", 4))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayerOrThrow();

                    Barman.CONFIG.endEnabled ^= true;
                    Barman.saveConfig();

                    player.sendMessage(Text.literal("The End is now " + (Barman.CONFIG.endEnabled ? "enabled" : "disabled") + "!").formatted(Formatting.RED), false);

                    return 1;
                })
        );
    }
}
