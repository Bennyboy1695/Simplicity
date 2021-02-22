package io.github.bennyboy1695.simplicity;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Predicate;
import java.util.logging.Logger;

@Mod(value = "simplicity")
public class Simplicity {

    public Simplicity() {
        if (FMLEnvironment.dist != Dist.DEDICATED_SERVER) {
            Logger.getLogger("[Simplicity]").warning("Non Server Enviroment detected. Simplicity will disable!");
            return;
        }
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
        ServerLifecycleHooks.getCurrentServer().getServerStatusResponse().getForgeData().getRemoteModData().remove("simplicity");
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> dispatcher = ServerLifecycleHooks.getCurrentServer().getCommandManager().getDispatcher();
        dispatcher.register(Commands.literal("simplicity")
                .requires(hasPermission("simplicity.command.simplicity"))
                .then(Commands.argument("type", StringArgumentType.string())
                        .then(Commands.argument("message", StringArgumentType.string())
                .executes(new CommandSimplicity(this)))));

    }

    private Predicate<CommandSource> hasPermission(String permission) {
        return source -> {
            if (source.getEntity() != null && source.getEntity() instanceof PlayerEntity) {
                return PermissionAPI.hasPermission((PlayerEntity) source.getEntity(), permission);
                //return source.hasPermissionLevel(1);
            } else {
                return source.hasPermissionLevel(4);
            }
        };
    }
}
