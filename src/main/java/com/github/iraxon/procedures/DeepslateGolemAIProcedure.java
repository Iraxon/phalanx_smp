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

		AI.common_ai(entity);

		switch (SoldierNBT.getType(entity)) {
			case COMMANDER -> AI.commander_ai(entity);
			case HEAVY_INFANTRY -> AI.heavy_infantry_ai(entity);
			case SKIRMISHER -> AI.skirmisher_ai(entity);
		}
		;
	}

	private static class AI {

		private static void common_ai(@Nonnull DeepslateGolemEntity soldier) {
			AIUtils.update_team(soldier, SoldierNBT.getPlayerUUID(soldier));

			final var targ = SoldierNBT.getMoveTarget(soldier);
			soldier.getNavigation().moveTo(targ.x, targ.y, targ.z, 1.0);
		}

		@SuppressWarnings("null")
		private static void commander_ai(@Nonnull DeepslateGolemEntity commander) {

			final FormationStateNBTWrapper formationWrapper = SoldierNBT.formationWrapper(commander);

			if (formationWrapper.getOrder().equals(Order.FOLLOW)) {

				var player = commander.level().getPlayerByUUID(
						Objects.requireNonNull(UUID.fromString(SoldierNBT.getPlayerUUID(commander))));
				if (player != null) {
					SoldierNBT.setMoveTarget(commander,
							player.position()
									.add(commander.position().subtract(player.position()).normalize().scale(1.25)));
				}
			} else if (formationWrapper.getOrder().equals(Order.HALT)) {
				SoldierNBT.clearMoveTarget(commander);
			}
		}

		private static void heavy_infantry_ai(@Nonnull DeepslateGolemEntity soldier) {
			//
		}

		public static void skirmisher_ai(@Nonnull DeepslateGolemEntity entity) {
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
					(@Nonnull LivingEntity e) -> AIUtils.should_target(entity, e, oldAtkTarget));
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

		/**
		 * Tells whether the soldier should consider the other entity
		 * an enemy to be targeted
		 *
		 * @param subject
		 * @param possible_target
		 * @return
		 */
		private static boolean should_target(@Nonnull DeepslateGolemEntity subject,
				@Nonnull LivingEntity possible_target, @Nullable LivingEntity oldTarget) {

			if (isAllied(subject, possible_target)) {
				return false;
			}

			// To allow retaliation
			if (possible_target == oldTarget) {
				return true;
			}
			var team = possible_target.getTeam();
			if (team != null && !team.isAlliedTo(subject.getTeam())) {
				return true;
			}
			return false;
		}

		private static boolean isAllied(@Nonnull DeepslateGolemEntity subject, @Nonnull LivingEntity other) {

			final var subjectTeam = subject.getTeam();
			final var otherTeam = other.getTeam();

			if (subjectTeam != null && subjectTeam.isAlliedTo(otherTeam)) {
				return true;
			}

			final var subjectPlayerUUID = SoldierNBT.getPlayerUUID(subject);

			if (other instanceof DeepslateGolemEntity e) {
				return subjectPlayerUUID.equals(SoldierNBT.getPlayerUUID(e));
			} else if (other instanceof Player p) {
				return subjectPlayerUUID.equals(p.getStringUUID());
			}

			return false;
		}

	}
}
