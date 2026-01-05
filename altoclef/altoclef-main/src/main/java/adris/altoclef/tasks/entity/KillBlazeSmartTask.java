package adris.altoclef.tasks.entity;

import adris.altoclef.AltoClef;
import adris.altoclef.tasks.movement.GetToBlockTask;
import adris.altoclef.tasks.movement.GetToEntityTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.util.helpers.LookHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class KillBlazeSmartTask extends Task {
    private final Entity target;

    public KillBlazeSmartTask(Entity target) {
        this.target = target;
    }

    @Override
    protected void onStart(AltoClef mod) {
        mod.getMobDefenseChain().setTargetEntity(target);
    }

    @Override
    protected Task onTick(AltoClef mod) {
        if (!target.isAlive())
            return null;

        // Equip Weapon
        AbstractKillEntityTask.equipWeapon(mod);

        float hitProg = mod.getPlayer().getAttackCooldownProgress(0);

        // "Hit-Wait-Strafe" Logic
        if (hitProg >= 1.0f) {
            // Ready to Hit
            if (mod.getPlayer().isInRange(target, 4)) {
                LookHelper.lookAt(mod, target.getEyePos());
                mod.getControllerExtras().attack(target);
                return null;
            } else {
                return new GetToEntityTask(target, 3);
            }
        } else {
            // Wait and Strafe
            // Calculate strafe direction (perpendicular to target)
            Vec3d playerPos = mod.getPlayer().getPos();
            Vec3d targetPos = target.getPos();
            Vec3d dir = targetPos.subtract(playerPos).normalize();

            // Cross product with Up vector (0,1,0)
            Vec3d left = dir.crossProduct(new Vec3d(0, 1, 0)).normalize();

            // Strafe 3 blocks to the left
            Vec3d strafePos = playerPos.add(left.multiply(3));

            // Check if safe? For now just go.
            BlockPos targetBlock = new BlockPos(strafePos);

            return new GetToBlockTask(targetBlock);
        }
    }

    @Override
    protected void onStop(AltoClef mod, Task interruptTask) {
        mod.getMobDefenseChain().setTargetEntity(null);
    }

    @Override
    protected boolean isEqual(Task other) {
        return other instanceof KillBlazeSmartTask && ((KillBlazeSmartTask) other).target.equals(target);
    }

    @Override
    protected String toDebugString() {
        return "Smart Killing Blaze (Hit-Wait-Strafe)";
    }
}
