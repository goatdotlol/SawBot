package baritone.altoclef;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Settings specific to AltoClef's integration with Baritone.
 * Populated to satisfy AltoClef compilation requirements.
 */
public class AltoClefSettings {

    private final Object breakMutex = new Object();
    private final Object placeMutex = new Object();
    private final Object propertiesMutex = new Object();
    private final Object globalHeuristicsMutex = new Object();

    private final HashSet<BlockPos> blocksToAvoidBreaking = new HashSet<>();
    private final List<Predicate<BlockPos>> breakAvoiders = new ArrayList<>();
    private final List<Predicate<BlockPos>> placeAvoiders = new ArrayList<>();
    private final List<Item> protectedItems = new ArrayList<>();

    private final List<Predicate<BlockPos>> forceWalkOnPredicates = new ArrayList<>();
    private final List<Predicate<BlockPos>> forceAvoidWalkThroughPredicates = new ArrayList<>();
    private final List<BiPredicate<BlockState, ItemStack>> forceUseToolPredicates = new ArrayList<>();

    private final List<BiFunction<Double, BlockPos, Double>> globalHeuristics = new ArrayList<>();

    private boolean flowingWaterPassAllowed = false;
    private boolean swimThroughLava = false;

    public Object getBreakMutex() {
        return breakMutex;
    }

    public Object getPlaceMutex() {
        return placeMutex;
    }

    public Object getPropertiesMutex() {
        return propertiesMutex;
    }

    public Object getGlobalHeuristicMutex() {
        return globalHeuristicsMutex;
    }

    public HashSet<BlockPos> getBlocksToAvoidBreaking() {
        return blocksToAvoidBreaking;
    }

    public List<Predicate<BlockPos>> getBreakAvoiders() {
        return breakAvoiders;
    }

    public List<Predicate<BlockPos>> getPlaceAvoiders() {
        return placeAvoiders;
    }

    public List<Item> getProtectedItems() {
        return protectedItems;
    }

    public List<Predicate<BlockPos>> getForceWalkOnPredicates() {
        return forceWalkOnPredicates;
    }

    public List<Predicate<BlockPos>> getForceAvoidWalkThroughPredicates() {
        return forceAvoidWalkThroughPredicates;
    }

    public List<BiPredicate<BlockState, ItemStack>> getForceUseToolPredicates() {
        return forceUseToolPredicates;
    }

    public List<BiFunction<Double, BlockPos, Double>> getGlobalHeuristics() {
        return globalHeuristics;
    }

    public boolean isFlowingWaterPassAllowed() {
        return flowingWaterPassAllowed;
    }

    public void setFlowingWaterPass(boolean allowed) {
        this.flowingWaterPassAllowed = allowed;
    }

    public void allowSwimThroughLava(boolean allow) {
        this.swimThroughLava = allow;
    }
}
