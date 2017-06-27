package com.github.kaaz.emily.information;

import com.github.kaaz.emily.command.AbstractCommand;
import com.github.kaaz.emily.command.ModuleLevel;
import com.github.kaaz.emily.command.anotations.Command;
import com.github.kaaz.emily.discordobjects.helpers.MessageMaker;
import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.EmoticonHelper;
import com.github.kaaz.emily.util.FormatHelper;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.static_data.constant.ChampData;
import net.rithms.riot.api.endpoints.static_data.dto.Champion;
import net.rithms.riot.api.endpoints.static_data.dto.ChampionSpell;
import net.rithms.riot.api.endpoints.static_data.dto.Image;
import net.rithms.riot.constant.Region;

import java.util.HashMap;
import java.util.Map;

public class LoLChampCommand extends AbstractCommand {
    private final RiotApi api;
    private final Map<String, Champion> dataChampionList = new HashMap<>();
    private String gameVersion = null;
    private String baseUrl = null;
    private static final String[] skillIndex = {EmoticonHelper.getChars("regional_indicator_q"), EmoticonHelper.getChars("regional_indicator_w"), EmoticonHelper.getChars("regional_indicator_r"), EmoticonHelper.getChars("regional_indicator_s")};
    private static String SWORDS = EmoticonHelper.getChars("crossed_swords"), EXPLOSION = EmoticonHelper.getChars("diamond_shape_with_a_dot_inside"), DEFENSE = EmoticonHelper.getChars("shield"), QUESTION_MARK = EmoticonHelper.getChars("question"), P = EmoticonHelper.getChars("regional_indicator_p");
    public LoLChampCommand() {
        super("lolchamp", ModuleLevel.INFO, null, null, "check out a league of legends champion");
        this.api = BotConfig.RIOT_GAMES_TOKEN != null ? new RiotApi(new ApiConfig().setKey(BotConfig.RIOT_GAMES_TOKEN)) : null;
    }
    private String getImage(Image img) {
        return baseUrl + img.getGroup() + "/" + img.getFull();
    }
    @Command
    public void execute(String args, MessageMaker maker) {
        if (this.api == null) throw new DevelopmentException("This command is currently unavailable");
        try {
            if (gameVersion == null) {
                gameVersion = api.getDataVersions(Region.EUW).get(0);
                baseUrl = String.format("http://ddragon.leagueoflegends.com/cdn/%s/img/", gameVersion);
            }
            if (dataChampionList.isEmpty()) {
                Map<String, Champion> tmp = api.getDataChampionList(Region.EUW, null, null, false, ChampData.ALL).getData();
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
            maker.getHeader().clear().append("I can't do that right now");
            e.printStackTrace();
        }
    }
}