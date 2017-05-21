package com.github.kaaz.emily.fun.slot.packs;

import com.github.kaaz.emily.fun.slot.AbstractSlotPack;
import com.github.kaaz.emily.service.services.ScheduleService;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.Rand;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Made by nija123098 on 5/19/2017.
 */
public class SpaceSlotPack extends AbstractSlotPack {
    private static final List<String> EARTHS = Stream.of("earth_americas", "earth_africa", "earth_asia").map(EmoticonHelper::getChars).collect(Collectors.toList());
    public SpaceSlotPack() {
        super(new String[]{"space_invader", "alien", "robot", "whale", "sunflower", "earth_africa"}, 20, 20, 15, 10, 10, 5);
        ScheduleService.scheduleRepeat(180000, 180000, () -> this.chars[5] = EARTHS.get(Rand.getRand(EARTHS.size()-1)));
    }
}
