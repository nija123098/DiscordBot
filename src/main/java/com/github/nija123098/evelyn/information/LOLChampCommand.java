package com.github.nija123098.evelyn.information;
import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.command.AbstractCommand;
import com.github.nija123098.evelyn.command.ModuleLevel;
import com.github.nija123098.evelyn.command.annotations.Argument;
import com.github.nija123098.evelyn.command.annotations.Command;
import com.github.nija123098.evelyn.discordobjects.helpers.MessageMaker;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.EmoticonHelper;
import com.github.nija123098.evelyn.util.FormatHelper;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.LogColor;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.static_data.constant.ChampionListTags;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionSpell;
import net.rithms.riot.api.endpoints.static_data.dto.Image;
import net.rithms.riot.constant.Platform;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class LOLChampCommand extends AbstractCommand {
    private final RiotApi api;
    private final Map<String, Champion> dataChampionList = new HashMap<>();
    private String baseUrl = null;
    private static final String[] skillIndex = {"q", "w", "r", "s"};
    private static String SWORDS = EmoticonHelper.getChars("crossed_swords", true), EXPLOSION = EmoticonHelper.getChars("diamond_shape_with_a_dot_inside", true), DEFENSE = EmoticonHelper.getChars("shield", true), QUESTION_MARK = EmoticonHelper.getChars("question", true), P = EmoticonHelper.getChars("regional_indicator_p", true);
    static {
        for (int i = 0; i < skillIndex.length; i++) skillIndex[i] = EmoticonHelper.getChars("regional_indicator_" + skillIndex[i], true);
    }
    public LOLChampCommand() {
        super("lolchamp", ModuleLevel.INFO, null, null, "check out a league of legends champion");
        this.api = ConfigProvider.AUTH_KEYS.riotGamesToken() != null ? new RiotApi(new ApiConfig().setKey(ConfigProvider.AUTH_KEYS.riotGamesToken())) : null;
        try{baseUrl = String.format("http://ddragon.leagueoflegends.com/cdn/%s/img/", api.getDataVersions(Platform.EUW).get(0));
        } catch (RiotApiException e) {
            //don't print stack trace if token not found
            if (Objects.equals(ConfigProvider.AUTH_KEYS.riotGamesToken(),"na")){
                Log.log(LogColor.red("Could not load LOLChamp command. Token not found."));
            }else{
                throw new DevelopmentException(e);
            }
        }
    }
    private String getImage(Image img) {
        return baseUrl + img.getGroup() + "/" + img.getFull();
    }
    @Command
    public void execute(@Argument(info = "champion name") String args, MessageMaker maker) {
        if (this.api == null) throw new DevelopmentException("This command is currently unavailable");
        try {
            if (dataChampionList.isEmpty()) {
                Map<String, Champion> tmp = api.getDataChampionList(Platform.EUW, null, null, false, ChampionListTags.ALL).getData();
                for (Map.Entry<String, Champion> entry : tmp.entrySet()) {
                    dataChampionList.put(entry.getKey().toLowerCase(), entry.getValue());
                }
            }
            if (args == null) {
                maker.append("I need a champion name");
                return;
            }
            String key = null;
            String search = args.toLowerCase();
            if (dataChampionList.containsKey(search)) {
                key = search;
            } else {
                for (String fullKey : dataChampionList.keySet()) {
                    if (fullKey.contains(search)) {
                        key = fullKey;
                        break;
                    }
                }
            }
            if (key == null) {
                maker.append("Can't find a champion with the name: ").appendRaw(" " + args);
                return;
            }
            Champion c = dataChampionList.get(key);
            String description = c.getBlurb().replace("<br><br>", "\n") + "\n\n";
            maker.getAuthorName().appendRaw(c.getName());
            maker.getTitle().append(c.getTitle());
            maker.withAuthorIcon(getImage(c.getImage()));
            maker.withImage(getImage(c.getImage()));
            description += Joiner.on(", ").join(c.getTags());
            description += "\n\n";
            description += String.format("%s Attack\n", FormatHelper.makeStackedBar(5, c.getInfo().getAttack() / 2, SWORDS));
            description += String.format("%s Magic\n", FormatHelper.makeStackedBar(5, c.getInfo().getMagic() / 2, EXPLOSION));
            description += String.format("%s Defense\n", FormatHelper.makeStackedBar(5, c.getInfo().getDefense() / 2, DEFENSE));
            description += String.format("%s Difficulty\n", FormatHelper.makeStackedBar(5, c.getInfo().getDifficulty() / 2, QUESTION_MARK));
            description += "\n**Abilities**\n\n**" + P + " " + c.getPassive().getName() + "**\n";
            description += c.getPassive().getSanitizedDescription() + "\n\n";
            int skillNum = 0;
            for (ChampionSpell spell : c.getSpells()) {
                description += "**" + skillIndex[skillNum] + " " + spell.getName() + "**\n";
                description += spell.getDescription().replace("<br><br>", "\n") + "\n";
                description += "\n";
                skillNum++;
            }
            maker.append(description);
        } catch (RiotApiException e) {
            maker.mustEmbed().withColor(new Color(255, 0, 0));
            maker.getHeader().clear().append("Sorry, we are in the process of updating our API key!");
            if (!e.getMessage().contains("403")) Log.log("Exception loading Riot information", e);
        }
    }
}