package io.github.bennyboy1695.simplicity;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Mod(value = "simplicity")
public class Simplicity {

    public Simplicity() {
        if (FMLEnvironment.dist != Dist.DEDICATED_SERVER) {
            Logger.getLogger("[Simplicity]").warning("Non Server Enviroment detected. Simplicity will disable!");
            return;
        }
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::registerPerms);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("simplicity")
                .requires(hasPermission(SIMPLICTY))
                .then(Commands.argument("type", StringArgumentType.string())
                        .then(Commands.argument("message", StringArgumentType.string())
                                .executes(new CommandSimplicity(this)))));

    }

    @SubscribeEvent
    public void registerPerms(PermissionGatherEvent.Nodes event) {
        event.addNodes(SIMPLICTY);
    }

    private Predicate<CommandSourceStack> hasPermission(PermissionNode<Boolean> permission) {
        return source -> {
            if (source.getEntity() != null && source.getEntity() instanceof ServerPlayer) {
                return PermissionAPI.getPermission((ServerPlayer) source.getEntity(), permission);
                //return source.hasPermissionLevel(1);
            } else {
                return source.hasPermission(4);
            }
        };
    }

    private static final PermissionNode<Boolean> SIMPLICTY = new PermissionNode<>("simplicity", "command.simplicity", PermissionTypes.BOOLEAN, (player, uuid, context) -> false);
}
