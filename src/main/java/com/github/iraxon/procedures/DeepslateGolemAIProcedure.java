package com.github.iraxon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.Entity;

import com.github.iraxon.entity.DeepslateGolemEntity;
import com.github.iraxon.procedures.FormationStateNBTWrapper.Order;

import java.util.Objects;
import java.util.Set;
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
	}

	private static class AI {

		private static void common_ai(@Nonnull DeepslateGolemEntity soldier) {
			SoldierAIUtils.update_team(soldier, SoldierNBT.getPlayerUUID(soldier));

			final var targ = SoldierNBT.getMoveTarget(soldier);
			soldier.getNavigation().moveTo(targ.x, targ.y, targ.z, 1.0);
		}

		@SuppressWarnings("null")
		private static void commander_ai(@Nonnull DeepslateGolemEntity commander) {

			final FormationStateNBTWrapper formationWrapper = SoldierNBT.formationWrapper(commander);

			SoldierAIUtils.setAttackTarget(commander, 16, commander.position());

			if (formationWrapper.getOrder().equals(Order.FOLLOW)) {

				var player = commander.level().getPlayerByUUID(
						Objects.requireNonNull(UUID.fromString(SoldierNBT.getPlayerUUID(commander))));
				if (player != null) {
					SoldierNBT.setMoveTarget(commander,
							player.position()
									.add(commander.position().subtract(player.position()).normalize().scale(0.5)));
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
	}

	public class SoldierAIUtils {

		public static void update_team(@Nonnull DeepslateGolemEntity entity, String playerUUIDString) {
			if (playerUUIDString.equals("")) {
				return;
			}

			final var player = PhalanxUtils.getEntityByUUID(Objects.requireNonNull(entity.level()), Player.class,
					Objects.requireNonNull(entity.position()), 64,
					playerUUIDString).orElse(null);
			AlignTeamProcedure.execute(entity, player);
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
		public static void setAttackTarget(@Nonnull DeepslateGolemEntity entity, double size,
				@Nonnull Vec3 target_pos) {
			entity.setTarget(PhalanxUtils.getNearestEntityWithPredicate(
					Objects.requireNonNull(entity.level()),
					LivingEntity.class,
					target_pos,
					size,
					(@Nonnull LivingEntity e) -> SoldierAIUtils.should_target(entity, e)).orElse(null));
		}

		/**
		 * Tells whether the soldier should consider the other entity
		 * an enemy to be targeted
		 *
		 * @param subject
		 * @param possible_target
		 * @return
		 */
		public static boolean should_target(@Nonnull DeepslateGolemEntity subject,
				@Nonnull LivingEntity possible_target) {

			// No friendly fire
			if (isAllied(subject, possible_target)) {
				return false;
			}

			var team = possible_target.getTeam();
			if (team != null && !team.isAlliedTo(subject.getTeam())) {
				return true;
			}

			// Retaliation
			if (SoldierNBT.getLastAttackerUUID(subject).equals(possible_target.getStringUUID())) {
				return true;
			}

			return false;
		}

		public static boolean isAllied(@Nonnull DeepslateGolemEntity subject, @Nonnull LivingEntity other) {

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

		private static Set<Class<? extends LivingEntity>> kosTypes = Set.of(
			Zombie.class,
			AbstractSkeleton.class,
			Creeper.class,
			Spider.class,
			Slime.class,
			Raider.class,
			Guardian.class
		);

		public static boolean isKillOnSight(@Nonnull LivingEntity potentialTarget) {
			return kosTypes.stream().anyMatch(
				t -> t.isInstance(potentialTarget)
			);
		}

	}
}
