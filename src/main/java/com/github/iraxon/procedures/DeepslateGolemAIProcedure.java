package com.github.iraxon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.FormationStateNBTWrapper.Order;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DeepslateGolemAIProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z, @Nullable Entity entity) {
		if (entity instanceof DeepslateGolemEntity golem)
			inner_execute(golem);
	}

	private static void inner_execute(@Nonnull DeepslateGolemEntity entity) {

		@Nonnull
		final DeepslateGolemNBTWrapper nbt = DeepslateGolemNBTWrapper.of(entity);

		AI.common_ai(nbt, entity);

		switch (nbt.getType()) {
			case COMMANDER -> AI.commander_ai(nbt, entity);
			case HEAVY_INFANTRY -> AI.heavy_infantry_ai(nbt, entity);
			case SKIRMISHER -> AI.skirmisher_ai(nbt, entity);
		}
		;
	}

	private static class AI {

		private static void common_ai(@Nonnull DeepslateGolemNBTWrapper nbt, @Nonnull DeepslateGolemEntity soldier) {
			AIUtils.update_team(soldier, nbt.getPlayerUUID());

			final var targ = nbt.getMoveTarget();
			soldier.getNavigation().moveTo(targ.x, targ.y, targ.z, 1.0);
		}

		@SuppressWarnings("null")
		private static void commander_ai(@Nonnull DeepslateGolemNBTWrapper nbt,
				@Nonnull DeepslateGolemEntity commander) {

			final FormationStateNBTWrapper formationWrapper = nbt.formationWrapper();

			if (formationWrapper.getOrder().equals(Order.FOLLOW)) {

				var player = commander.level().getPlayerByUUID(
						Objects.requireNonNull(UUID.fromString(nbt.getPlayerUUID())));
				if (player != null) {
					nbt.setMoveTarget(
							player.position()
									.add(commander.position().subtract(player.position()).normalize().scale(1.25)));
				}
			} else if (formationWrapper.getOrder().equals(Order.HALT)) {
				nbt.clearMoveTarget();
			}
		}

		private static void heavy_infantry_ai(@Nonnull DeepslateGolemNBTWrapper nbt,
				@Nonnull DeepslateGolemEntity soldier) {
			//
		}

		public static void skirmisher_ai(DeepslateGolemNBTWrapper nbt, DeepslateGolemEntity entity) {
			//
		}

		/**
		 * Selects the best attack target for this soldier
		 *
		 * @param entity
		 * @param size       Size of cubic search area for targets
		 * @param target_pos The location the soldier is "supposed to" be at; useful
		 *                   option to make sure formation is kept
		 * @return
		 */
		private static Optional<LivingEntity> getAttackTarget(@Nonnull DeepslateGolemEntity entity, double size,
				@Nonnull Vec3 target_pos) {
			final LivingEntity oldAtkTarget = entity.getTarget();
			return PhalanxUtils.getNearestEntityWithPredicate(
					Objects.requireNonNull(entity.level()),
					LivingEntity.class,
					target_pos,
					size,
					(@Nonnull LivingEntity e) -> (e == oldAtkTarget) // To allow retaliation
							|| should_target(entity, e));
		}

		/**
		 * Tells whether the soldier should consider the other entity
		 * an enemy to be targeted
		 *
		 * @param subject
		 * @param possible_target
		 * @return
		 */
		private static boolean should_target(@Nonnull DeepslateGolemEntity subject,
				@Nonnull LivingEntity possible_target) {
			// Needs to be improved
			var team = possible_target.getTeam();
			if (team != null && !team.isAlliedTo(subject.getTeam())) {
				return true;
			}
			return false;
		}

	}

	private class AIUtils {

		private static void update_team(@Nonnull DeepslateGolemEntity entity, String playerUUIDString) {
			if (playerUUIDString.equals("")) {
				return;
			}

			final var player = PhalanxUtils.getEntityByUUID(Objects.requireNonNull(entity.level()), Player.class,
					Objects.requireNonNull(entity.position()), 64,
					playerUUIDString).orElse(null);
			AlignTeamProcedure.execute(entity, player);
		}

	}
}
