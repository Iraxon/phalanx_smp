package com.github.iraxon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.iraxon.entity.ArmyStandardEntity;
import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.init.PhalanxSmpModEntities;
import com.github.iraxon.procedures.deepslate_golem_systems.PhalanxUtils;
import com.github.iraxon.procedures.deepslate_golem_systems.PlayerLiegeUUID;

public class ArmyStandardRightclickedOnEntityProcedure {
	public static void execute(@Nullable LevelAccessor world, @Nullable Entity entity, @Nullable Entity sourceentity) {
		if (world == null) {
			return;
		}
		if (entity instanceof ArmyStandardEntity standard && sourceentity instanceof Player player) {
			if (PlayerLiegeUUID.isLoyalTo(standard, player)) {

				@SuppressWarnings("null")
				@Nonnull
				final var standard_pos = standard.position();

				PhalanxUtils.spawnMob(world, standard_pos, PhalanxSmpModEntities.DEEPSLATE_GOLEM);
				// Find the golem we just spawned
				final var new_golem = PhalanxUtils.getNearestEntityWithPredicate(
						world,
						DeepslateGolemEntity.class,
						standard_pos,
						1,
						golem -> golem != null
								&& !PlayerLiegeUUID.isSet(golem));

				new_golem.ifPresent(g -> PlayerLiegeUUID.tryProgramLoyalty(g, player));
			} else {
				PlayerLiegeUUID.tryProgramLoyalty(standard, player);
			}
		}
	}
}
