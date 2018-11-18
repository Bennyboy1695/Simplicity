package io.github.bennyboy1695.simplicity;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class CommandSimplicity implements CommandExecutor {

    private Simplicity plugin;

    public CommandSimplicity(Simplicity plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String message = args.<String>getOne("message").get();
        String sound = args.<String>getOne("sound").orElse("no_arg");
        String type = args.<String>getOne("type").get();
        Integer lengthInSeconds = args.<Integer>getOne("length").orElse(5);
        final Integer[] length = {lengthInSeconds};

        Title title = Title.builder().actionBar(TextSerializers.FORMATTING_CODE.deserialize(message)).build();

        switch (type) {
            case "title-sound":
                if (!Sponge.getGame().getRegistry().getType(SoundType.class, sound).isPresent()) {
                    src.sendMessage(Text.of(TextColors.RED, "You have selected to play a sound, but haven't given a sound to play. Or have given an invalid sound!"));
                    return CommandResult.success();
                }
                try {
                    Task.builder().interval(1, TimeUnit.SECONDS).execute(new CancellingTask() {
                        @Override
                        public void accept(Task task) {
                            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                                player.sendTitle(title);
                            }
                            length[0]--;
                            if (length[0] < 1) {
                                task.cancel();
                            }
                        }
                    }).submit(plugin);
                    for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                        player1.playSound(SoundType.of(sound), player1.getPosition(), 1.0f);
                    }
                    return CommandResult.success();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "chat-sound":
                if (!Sponge.getGame().getRegistry().getType(SoundType.class, sound).isPresent()) {
                    src.sendMessage(Text.of(TextColors.RED, "You have selected to play a sound, but haven't given a sound to play. Or have given an invalid sound!"));
                    return CommandResult.success();
                }
                try {
                        for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                            player1.playSound(SoundType.of(sound), player1.getPosition(), 1.0f);
                            player1.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                        }
                        return CommandResult.success();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "title-chat":
                try {
                    for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                        player1.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                    }
                    Task.builder().interval(1, TimeUnit.SECONDS).execute(new CancellingTask() {
                        @Override
                        public void accept(Task task) {
                            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                                player.sendTitle(title);
                            }
                            length[0]--;
                            if (length[0] < 1) {
                                task.cancel();
                            }
                        }
                    }).submit(plugin);
                    return CommandResult.success();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "title-chat-sound":
                if (!Sponge.getGame().getRegistry().getType(SoundType.class, sound).isPresent()) {
                    src.sendMessage(Text.of(TextColors.RED, "You have selected to play a sound, but haven't given a sound to play. Or have given an invalid sound!"));
                    return CommandResult.success();
                }
                try {
                        for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                            player1.playSound(SoundType.of(sound), player1.getPosition(), 1.0f);
                            player1.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                        }
                    Task.builder().interval(1, TimeUnit.SECONDS).execute(new CancellingTask() {
                        @Override
                        public void accept(Task task) {
                            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                                player.sendTitle(title);
                            }
                            length[0]--;
                            if (length[0] < 1) {
                                task.cancel();
                            }
                        }
                    }).submit(plugin);
                        return CommandResult.success();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }

    public abstract class CancellingTask implements Consumer<Task> {

    }
}
