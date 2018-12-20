package com.github.nija123098.evelyn.moderation.temporary;

import com.github.nija123098.evelyn.config.AbstractConfig;
import com.github.nija123098.evelyn.config.ConfigCategory;
import com.github.nija123098.evelyn.discordobjects.wrappers.Category;
import com.github.nija123098.evelyn.discordobjects.wrappers.Guild;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class TemporaryChannelCategoryConfig extends AbstractConfig<Category, Guild> {
    public TemporaryChannelCategoryConfig() {
        super("temp_channel_category", "Co-op Channel Category", ConfigCategory.GAME_TEMPORARY_CHANNELS, guild -> guild.getCategories().stream().filter(category -> category.getName().contains("temp") || category.getName().contains("game")).findAny().orElse(null), "The channel category that temporary channels will be generated under if enabled");
    }
}
