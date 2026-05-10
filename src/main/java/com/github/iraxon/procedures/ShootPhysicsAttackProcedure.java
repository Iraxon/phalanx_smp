package com.github.iraxon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import java.util.Comparator;

public class ShootPhysicsAttackProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return false;
		boolean hitEntity = false;
		double i = 0;
		Vec3 stepPosition = Vec3.ZERO;
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.return")), SoundSource.HOSTILE, 1, (float) 1.5);
			} else {
				_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.return")), SoundSource.HOSTILE, 1, (float) 1.5, false);
			}
		}
		i = 3;
		while (i <= 6) {
			stepPosition = (new Vec3(x, (y + entity.getEyeHeight()), z)).add((((entity.getLookAngle()).normalize()).scale(i)));
			hitEntity = false;
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.CRIT, (stepPosition.x()), (stepPosition.y()), (stepPosition.z()), 1, 0.01, 0.01, 0.01, 0);
			{
				final Vec3 _center = new Vec3((stepPosition.x()), (stepPosition.y()), (stepPosition.z()));
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (!(entityiterator == entity)) {
						entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("phalanx_smp:spear"))), entity), 5);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(stepPosition.x(), stepPosition.y(), stepPosition.z()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.hit")), SoundSource.HOSTILE, 1, (float) 0.75);
							} else {
								_level.playLocalSound((stepPosition.x()), (stepPosition.y()), (stepPosition.z()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.hit")), SoundSource.HOSTILE, 1, (float) 0.75, false);
							}
						}
						hitEntity = true;
					}
				}
			}
			if (hitEntity) {
				return true;
			}
			if (!(world.getBlockState(BlockPos.containing(stepPosition.x(), stepPosition.y(), stepPosition.z()))).is(BlockTags.create(new ResourceLocation("minecraft:replaceable")))) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(stepPosition.x(), stepPosition.y(), stepPosition.z()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.hit_ground")), SoundSource.HOSTILE, 1, (float) 0.75);
					} else {
						_level.playLocalSound((stepPosition.x()), (stepPosition.y()), (stepPosition.z()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.trident.hit_ground")), SoundSource.HOSTILE, 1, (float) 0.75, false);
					}
				}
				return false;
			}
			i = i + 0.5;
		}
		return false;
	}
}