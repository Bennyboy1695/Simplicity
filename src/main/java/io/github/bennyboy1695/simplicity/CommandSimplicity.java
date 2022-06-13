package io.github.bennyboy1695.simplicity;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Consumer;

public class CommandSimplicity implements Command<CommandSourceStack> {

    private Simplicity plugin;
    private UUID CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public CommandSimplicity(Simplicity plugin) {
        this.plugin = plugin;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String type = context.getArgument("type", String.class);
        String message = context.getArgument("message", String.class);
        if ("title-chat-sound".equals(type)) {
            try {
                for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                    Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
                    player.sendMessage(componentToITextComponent(component), CONSOLE_UUID);
                    player.connection.send(new ClientboundSetActionBarTextPacket(componentToITextComponent(component)));
                    player.connection.send(new ClientboundSetTitlesAnimationPacket(0, 20 * 7 , 0));
                }
                ServerLifecycleHooks.getCurrentServer().sendMessage(componentToITextComponent(LegacyComponentSerializer.legacyAmpersand().deserialize(message)), CONSOLE_UUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            context.getSource().sendSuccess(new TextComponent(ChatFormatting.RED + "This type is not implemented yet!"), false);
        }
        return 0;
    }

    public static TextComponent componentToITextComponent(Component component) {
        return (TextComponent) TextComponent.Serializer.fromJson(GsonComponentSerializer.gson().serialize(component));
    }

    public abstract class CancellingTask implements Consumer<Runnable> {

    }
}
