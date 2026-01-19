package net.squarefish;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import net.squarefish.commands.ExampleCommand;
import net.squarefish.events.ExampleEvent;
import net.squarefish.systems.DeathSystem;

import javax.annotation.Nonnull;

public class InstantRespawn extends JavaPlugin {

    public InstantRespawn(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);
        this.getEntityStoreRegistry().registerSystem(new DeathSystem());
    }
}