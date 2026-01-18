package squarefish;

import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import squarefish.commands.ExampleCommand;
import squarefish.events.ExampleEvent;
import squarefish.systems.DeathSystem;

import javax.annotation.Nonnull;

public class RBDPlugin extends JavaPlugin {

    public RBDPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand("example", "An example command"));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, ExampleEvent::onPlayerReady);
        this.getEntityStoreRegistry().registerSystem(new DeathSystem());
    }
}