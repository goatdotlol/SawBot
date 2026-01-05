package adris.altoclef;

import adris.altoclef.tasks.resources.CollectStoneTask;
import adris.altoclef.tasks.resources.CollectWoodTask;
import adris.altoclef.tasks.container.CraftInTableTask;
import adris.altoclef.tasksystem.Task;
import net.minecraft.item.Items;

public class SpeedrunController {

    public Task getTickTask(AltoClef mod) {
        // Priority Chain: GetWood -> CraftPickaxe -> GetStone

        // 1. Get Wood logs
        // If we don't have a pickaxe and we don't have enough wood
        if (!mod.getItemStorage().hasItem(Items.WOODEN_PICKAXE) &&
                !mod.getItemStorage().hasItem(Items.STONE_PICKAXE) &&
                !mod.getItemStorage().hasItem(Items.IRON_PICKAXE) &&
                !mod.getItemStorage().hasItem(Items.DIAMOND_PICKAXE)) {

            int logCount = mod.getItemStorage().getItemCount(Items.OAK_LOG) +
                    mod.getItemStorage().getItemCount(Items.BIRCH_LOG) +
                    mod.getItemStorage().getItemCount(Items.SPRUCE_LOG) +
                    mod.getItemStorage().getItemCount(Items.JUNGLE_LOG) +
                    mod.getItemStorage().getItemCount(Items.ACACIA_LOG) +
                    mod.getItemStorage().getItemCount(Items.DARK_OAK_LOG);

            // Need 3 logs for 1 pickaxe (3 planks + 2 sticks = 5 planks = 2 logs? No, 3
            // planks + 2 sticks. 3 planks comes from 1 log. 2 sticks comes from 2 planks
            // (0.5 log). Total 1.25 logs. )
            // But we need crafting table (4 planks = 1 log).
            // Total: 1 log (table) + 1 log (pickaxe head) + 1 log (sticks). = 3 logs.

            if (logCount < 3) {
                return new CollectWoodTask(3);
            }

            // 2. Craft Wooden Pickaxe
            return new CraftInTableTask(Items.WOODEN_PICKAXE, 1);
        }

        // 3. Get Stone
        if (!mod.getItemStorage().hasItem(Items.STONE_PICKAXE) &&
                !mod.getItemStorage().hasItem(Items.IRON_PICKAXE) &&
                !mod.getItemStorage().hasItem(Items.DIAMOND_PICKAXE)) {

            return new CollectStoneTask(3);
        }

        return null;
    }
}
