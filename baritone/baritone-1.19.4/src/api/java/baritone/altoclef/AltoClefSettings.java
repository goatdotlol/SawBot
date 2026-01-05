package baritone.altoclef;

/**
 * Settings specific to AltoClef's integration with Baritone.
 * required for AltoClef to compile.
 */
public class AltoClefSettings {

    public final Setting<Boolean> killaura = new Setting<>(true);

    public static class Setting<T> {
        public T value;

        public Setting(T val) {
            this.value = val;
        }
    }
}
