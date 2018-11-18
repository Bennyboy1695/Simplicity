package io.github.bennyboy1695.simplicity;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;

public class CommandSimplicity implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String message = args.<String>getOne("message").get();
        String sound = args.<String>getOne("sound").orElse("no_arg");
        String type = args.<String>getOne("type").get();
        Player player = args.<Player>getOne("player").orElse(null);

        switch (type) {
            case "title-sound":
                if (!Sponge.getGame().getRegistry().getType(SoundType.class, sound).isPresent()) {
                    src.sendMessage(Text.of(TextColors.RED, "You have selected to play a sound, but haven't given a sound to play. Or have given an invalid sound!"));
                    return CommandResult.success();
                }
                try {
                    if (player != null) {
                        player.getPlayer().get().playSound(SoundType.of(sound), player.getPosition(), 1.0f);
                        player.getPlayer().get().sendTitle(Title.builder().actionBar(TextSerializers.FORMATTING_CODE.deserialize(message)).stay(2).build());
                        return CommandResult.success();
                    } else {
                        for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                            player1.playSound(SoundType.of(sound), player1.getPosition(), 1.0f);
                            player1.sendTitle(Title.builder().actionBar(TextSerializers.FORMATTING_CODE.deserialize(message)).stay(2).build());

                        }
                        return CommandResult.success();
                    }
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
                    if (player != null) {
                        player.getPlayer().get().playSound(SoundType.of(sound), player.getPosition(), 1.0f);
                        player.getPlayer().get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                        return CommandResult.success();
                    } else {
                        for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                            player1.playSound(SoundType.of(sound), player1.getPosition(), 1.0f);
                            player1.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                        }
                        return CommandResult.success();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "title-chat":
                try {
                    for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                        player1.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                        player1.sendTitle(Title.builder().actionBar(TextSerializers.FORMATTING_CODE.deserialize(message)).stay(2).build());
                    }
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
                    if (player != null) {
                        player.getPlayer().get().playSound(SoundType.of(sound), player.getPosition(), 1.0f);
                        player.getPlayer().get().sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                        player.sendTitle(Title.builder().actionBar(TextSerializers.FORMATTING_CODE.deserialize(message)).stay(2).build());
                        return CommandResult.success();
                    } else {
                        for (Player player1 : Sponge.getServer().getOnlinePlayers()) {
                            player1.playSound(SoundType.of(sound), player1.getPosition(), 1.0f);
                            player1.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(message));
                            player1.sendTitle(Title.builder().actionBar(TextSerializers.FORMATTING_CODE.deserialize(message)).stay(2).build());
                        }
                        return CommandResult.success();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }
}
