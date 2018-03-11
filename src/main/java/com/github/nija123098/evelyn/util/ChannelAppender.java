package com.github.nija123098.evelyn.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.discordobjects.wrappers.Channel;
import com.github.nija123098.evelyn.launcher.Launcher;
import sx.blah.discord.handle.obj.IChannel;

import java.util.concurrent.atomic.AtomicReference;

public class ChannelAppender<E> extends UnsynchronizedAppenderBase<E> {
    private static final AtomicReference<IChannel> CHANNEL = new AtomicReference<>();
    private final Layout<E> layout;

    public ChannelAppender() {
        PatternLayout layout = new PatternLayout();
        layout.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        layout.setContext(new LoggerContext());
        layout.start();
        this.layout = (Layout<E>) layout;
    }

    @Override
    protected void append(E eventObject) {
        if (Launcher.isReady()) {
            if (CHANNEL.get() == null) {
                CHANNEL.set(Channel.getChannel(ConfigProvider.BOT_SETTINGS.loggingChannel()).channel());
            }
            ExceptionWrapper.wrap(() -> CHANNEL.get().sendMessage(FormatHelper.limitLength(this.layout.doLayout(eventObject), 2000)));
        }
    }
}
