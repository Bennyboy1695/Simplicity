package io.github.bennyboy1695.simplicity;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(id = "simplicity", name = "Simplicity", description = "A plugin that shrink's and simplifies multiple commands together. ", authors = {"Bennyboy1695"})
public class Simplicity {

    @Inject
    private Logger logger;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        CommandSpec simplicity = CommandSpec.builder()
                .description(Text.of("The command that does the shrinking!"))
                .arguments(GenericArguments.seq(new TypeElement(Text.of("type"))), GenericArguments.string(Text.of("message")), GenericArguments.optional(GenericArguments.integer(Text.of("length"))), GenericArguments.optional(GenericArguments.string(Text.of("sound"))), GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .executor(new CommandSimplicity())
                .permission("simplicity.command.simplicity")
                .build();

        Sponge.getCommandManager().register(this, simplicity, "simplicity", "sc", "sp", "simp", "shrink");
    }
}
