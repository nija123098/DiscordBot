package com.github.kaaz.emily.helping;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.annotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.util.FormatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Made by nija123098 on 8/3/2017.
 */
public class ConversionUnitsCommand extends AbstractCommand {
    private static final int PER_PAGE = 120;
    private static final List<List<String>> LINES;
    static {
        Set<String> units = ConversionCommand.UN_MAP.keySet();
        int pageCount = units.size() / PER_PAGE + 1;
        LINES = new ArrayList<>(pageCount);
        for (int i = 0; i < pageCount; i++) LINES.add(new ArrayList<>(PER_PAGE));
        AtomicInteger i = new AtomicInteger(), page = new AtomicInteger(0);
        units.forEach(s -> {
            if (i.incrementAndGet() > PER_PAGE) {
                i.set(1);
                page.incrementAndGet();
            }
            LINES.get(page.get()).add(s);
        });
    }
    public ConversionUnitsCommand() {
        super(ConversionCommand.class, "units", null, null, null, "Displays supported units");
    }
    @Command
    public void command(MessageMaker maker){
        LINES.forEach(page -> maker.getNewListPart().appendRaw(FormatHelper.getList(page)));
    }
}
