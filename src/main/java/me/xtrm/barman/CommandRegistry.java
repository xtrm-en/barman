package me.xtrm.barman;

import com.mojang.brigadier.CommandDispatcher;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.xtrm.barman.command.*;
import net.minecraft.server.command.ServerCommandSource;

/**
 * @author xtrm
 */
public enum CommandRegistry {
  INSTANCE;

  private final Set<Supplier<Consumer<CommandDispatcher<ServerCommandSource>>>>
      commands = Set.of(
          // SpawnCommand::new,
          ToggleEndCommand::new);

  public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    commands.forEach(supplier -> supplier.get().accept(dispatcher));
  }
}
