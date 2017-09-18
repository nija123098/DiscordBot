package com.github.nija123098.evelyn.favor;

import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ContextType;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.config.ConfigHandler;
import com.github.nija123098.evelyn.config.GuildUser;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordClient;
import com.github.nija123098.evelyn.discordobjects.wrappers.DiscordPermission;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;
import com.github.nija123098.evelyn.discordobjects.wrappers.Role;
import com.github.nija123098.evelyn.exeption.ArgumentException;
import com.github.nija123098.evelyn.exeption.PermissionsException;
import com.github.nija123098.evelyn.favor.configs.EarnRankConfig;
import com.github.nija123098.evelyn.favor.configs.balencing.FavorRankEquationConfig;
import com.github.nija123098.evelyn.helping.CalculateCommand;
import com.github.nija123098.evelyn.service.services.ScheduleService;
import com.github.nija123098.evelyn.util.GraphicsHelper;
import com.google.common.util.concurrent.AtomicDouble;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import sx.blah.discord.util.RoleBuilder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SetupRanksCommand extends AbstractCommand {
    static final Map<Guild, Runnable> TASK_MAP = new HashMap<>();
    public SetupRanksCommand() {
        super("setupranks", ModuleLevel.ADMINISTRATIVE, "setup ranks", null, "Sets up the ranks for autoranking");
    }
    @Command
    public void command(MessageMaker maker, Guild guild, @Argument(optional = true, info = "raise above default roles", replacement = ContextType.NONE) Boolean raise, @Argument(optional = true, info = "the lowest color", replacement = ContextType.NONE) Color low, @Argument(optional = true, info = "the highest color", replacement = ContextType.NONE) Color high, @Argument(info = "rank equation x == rank, y == favor required") String equation){
        if (!DiscordClient.getOurUser().getPermissionsForGuild(guild).contains(DiscordPermission.MANAGE_ROLES)) throw new PermissionsException("I need to be able to manage roles for this");
        equation = ConfigHandler.setSetting(FavorRankEquationConfig.class, guild, equation);
        AtomicDouble greatest = new AtomicDouble(0);
        guild.getUsers().stream().map(user -> GuildUser.getGuildUser(guild, user)).forEach(guildUser -> {
            float f = FavorHandler.getFavorAmount(guildUser);
            if (greatest.get() < f) greatest.set(f);
        });
        Expression expression = new ExpressionBuilder(equation).operator(CalculateCommand.OPERATORS).variable("x").build();
        ArrayList<Float> requirement = new ArrayList<>(50);
        while (true){
            if (requirement.size() > 50) throw new ArgumentException("That would make over 50 roles.  I'm not doing that by my self");
            float val = (float) expression.setVariable("x", requirement.size()).evaluate();
            if (val > greatest.get()) break;
            requirement.add(val);
        }
        maker.append("The bot will make " + requirement.size() + " roles to fulfil this request." +
                "\nYou have 5 minutes to do `@Evelyn setupranks approve`" +
                "\nYou must name these, it's not too hard.  (I'll change this soon)" +
                "\nRanks will update for a user when their favor level changes, which happens fairly often.");
        ScheduleService.ScheduledTask task = ScheduleService.schedule(300_000, () -> TASK_MAP.remove(guild));
        TASK_MAP.put(guild, () -> {
            task.cancel();
            List<Runnable> roleMaking = new ArrayList<>(requirement.size());
            AtomicInteger i = new AtomicInteger();
            for (; i.get() < requirement.size(); i.incrementAndGet()) {
                int index = i.get();
                roleMaking.add(() -> {
                    float value = requirement.get(index);
                    ConfigHandler.setSetting(EarnRankConfig.class, Role.getRole(new RoleBuilder(guild.guild()).withColor(GraphicsHelper.getGradient((float) index / (requirement.size() - 1), high == null ? Color.GREEN : high, low == null ? Color.BLUE : low)).setHoist(raise == null ? false : raise).build()), value);
                });
            }
            TASK_MAP.remove(guild);
            for (int j = roleMaking.size() - 1; j > -1; --j) roleMaking.get(j).run();
        });
    }
}
