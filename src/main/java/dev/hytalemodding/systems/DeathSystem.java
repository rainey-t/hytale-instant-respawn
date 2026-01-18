package dev.hytalemodding.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.PageManager;
import com.hypixel.hytale.server.core.entity.entities.player.pages.RespawnPage;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.hytalemodding.componets.PassThroughRespawnPage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class DeathSystem extends DeathSystems.OnDeathSystem {

    @Override
    public Query<EntityStore> getQuery() {
        return Player.getComponentType();
    }

    @Nonnull
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return Set.of(new SystemDependency<>(Order.BEFORE, DeathSystems.PlayerDeathScreen.class));
    }

    @Override
    public void onComponentAdded(@Nonnull Ref ref, @Nonnull DeathComponent component, @Nonnull Store store, @Nonnull CommandBuffer commandBuffer) {

        Player playerComponent = (Player) store.getComponent(ref, Player.getComponentType());

        assert playerComponent != null;

        Damage deathInfo = component.getDeathInfo();
        Message deathMessage = deathInfo != null ? deathInfo.getDeathMessage(ref, commandBuffer) : null;
        component.setDeathMessage(deathMessage);
        PlayerRef playerRefComponent = (PlayerRef) commandBuffer.getComponent(ref, PlayerRef.getComponentType());

        assert playerRefComponent != null;

        PageManager pageManager = playerComponent.getPageManager();
        pageManager.openCustomPage(
                ref, store, new PassThroughRespawnPage(playerRefComponent, deathMessage, component.displayDataOnDeathScreen(), component.getDeathItemLoss())
        );
        component.setShowDeathMenu(false);

        World world = playerComponent.getWorld();
        if (world != null) {
            playerComponent.getWorld().execute(() -> {
                playerComponent.getPageManager().setPage(ref, store, Page.None);
            });
        }

    }
}
