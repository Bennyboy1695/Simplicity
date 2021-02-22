package io.github.bennyboy1695.simplicity;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.gson.GsonComponentSerializer;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class CommandSimplicity implements Command<CommandSource> {

    private Simplicity plugin;
    private UUID CONSOLE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public CommandSimplicity(Simplicity plugin) {
        this.plugin = plugin;
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String type = context.getArgument("type", String.class);
        String message = context.getArgument("message", String.class);
        if ("title-chat-sound".equals(type)) {
            try {
                for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                    player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                    player.sendMessage(convertToITextComponent(LegacyComponentSerializer.legacy().deserialize(message, '&')), CONSOLE_UUID);
                    player.connection.sendPacket(new STitlePacket(STitlePacket.Type.ACTIONBAR, TextComponentUtils.func_240645_a_(context.getSource(), convertToITextComponent(LegacyComponentSerializer.legacy().deserialize(message, '&')), player, 0)));
                }
                ServerLifecycleHooks.getCurrentServer().sendMessage(convertToITextComponent(LegacyComponentSerializer.legacy().deserialize(message, '&')), CONSOLE_UUID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            context.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + "This type is not implemented yet!"), false);
        }
        return 0;
    }

    public static ITextComponent convertToITextComponent(Component component) {
        return ITextComponent.Serializer.getComponentFromJson(GsonComponentSerializer.INSTANCE.serialize(component));
    }

    public abstract class CancellingTask implements Consumer<Runnable> {

    }
}
