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
    private static final List<String> EARTHS = Stream.of("earth_americas", "earth_africa", "earth_asia").map(s -> EmoticonHelper.getChars(s, false)).collect(Collectors.toList());
    public SpaceSlotPack() {
        super(new String[]{"space_invader", "alien", "robot", "whale", "sunflower", "earth_africa"}, 20, 15, 15, 5, 5, 3);
        ScheduleService.scheduleRepeat(180000, 180000, () -> this.chars[5] = Rand.getRand(EARTHS, false));
    }
    @Override
    public int[][] getTable() {
        int[][] ints = super.getTable();
        ints[Rand.getRand(ints.length - 1)][0] = -1;
        return ints;
    }
    @Override
    public String getChar(int i) {
        return i == -1 ? EmoticonHelper.getChars("cow", false) : super.getChar(i);
    }
}
