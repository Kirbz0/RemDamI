package net.kirbz.remdami.interfaces;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

/*
* Code copied from ProfHugo's original 1.14 NoDamI fabric port
* https://github.com/ProfHugo/NoDamI/blob/1.14.4-fabric/src/main/java/net/profhugo/nodami/interfaces/EntityHurtCallback.java
*/

public interface EntityHurtCallback {

    Event<EntityHurtCallback> EVENT = EventFactory.createArrayBacked(EntityHurtCallback.class,
            (listeners) -> (entity, source, amount) -> {
                for (EntityHurtCallback event : listeners) {
                    ActionResult result = event.hurtEntity(entity, source, amount);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult hurtEntity(LivingEntity entity, DamageSource source, float amount);
}
