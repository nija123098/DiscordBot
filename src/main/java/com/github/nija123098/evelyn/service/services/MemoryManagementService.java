package com.github.nija123098.evelyn.service.services;

import com.github.nija123098.evelyn.service.AbstractService;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class MemoryManagementService extends AbstractService {// disabled, re-enable and fix when management is used again
    private static final long SERVICE_ITERATION_TIME = 5_000;
    // private static final long[] INDICES = new long[ConfigLevel.values().length];
    // private static final float[] LEFT_OVER = new float[INDICES.length];
    // private static final float[] CONFIG_PER = new float[INDICES.length];
    public MemoryManagementService() {
        super(-1);
        /*
        Launcher.registerStartup(() -> {
            for (int i = 0; i < INDICES.length; i++) {
                if (ConfigLevel.values()[i] == ConfigLevel.ALL) continue;
                CONFIG_PER[i] = ConfigHandler.getTypeCount(ConfigLevel.values()[i].getType()) / (float) 86_400_000;// 24 hours
            }
        });*/
    }
    @Override
    public void run() {
        // LISTS.forEach(ManagedList::manage);
        /* todo fix
        for (int i = 0; i < INDICES.length; i++) {
            if (ConfigLevel.values()[i] == ConfigLevel.ALL) continue;
            try{if (ConfigLevel.values()[i].getType().getMethod("manage").getDeclaringClass().equals(Configurable.class)) continue;
            } catch (NoSuchMethodException e) {Log.log("This should never happen", e);}
            LEFT_OVER[i] += CONFIG_PER[i];
            int count = (int) (LEFT_OVER[i] / 1);
            ConfigHandler.getTypeInstances(ConfigLevel.values()[i].getType(), INDICES[i], count).stream().filter(Objects::nonNull).forEach(Configurable::manage);
            INDICES[i] += count;
            LEFT_OVER[i] %= 1;
        }*/
    }
}
