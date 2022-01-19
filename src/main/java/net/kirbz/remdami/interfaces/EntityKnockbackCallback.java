package net.kirbz.remdami.interfaces;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

/*
 * Code copied from ProfHugo's original 1.14 NoDamI fabric port
 * https://github.com/ProfHugo/NoDamI/blob/1.14.4-fabric/src/main/java/net/profhugo/nodami/interfaces/EntityKnockbackCallback.java
 */

public interface EntityKnockbackCallback {

    Event<EntityKnockbackCallback> EVENT = EventFactory.createArrayBacked(EntityKnockbackCallback.class,
            (listeners) -> (entity, strength, dx, dz) -> {
                for (EntityKnockbackCallback event : listeners) {
                    ActionResult result = event.takeKnockback(entity, strength, dx, dz);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult takeKnockback(LivingEntity entity, double strength, double dx, double dz);
}