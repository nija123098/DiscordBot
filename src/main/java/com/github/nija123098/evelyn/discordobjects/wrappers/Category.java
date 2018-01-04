package com.github.nija123098.evelyn.discordobjects.wrappers;

import com.github.nija123098.evelyn.config.ConfigLevel;
import com.github.nija123098.evelyn.config.Configurable;
import com.github.nija123098.evelyn.discordobjects.ExceptionWrapper;
import com.github.nija123098.evelyn.perms.BotRole;
import com.github.nija123098.evelyn.service.services.MemoryManagementService;
import sx.blah.discord.handle.obj.ICategory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author nija123098
 * @since 1.0.0
 */
public class Category implements Configurable {
    private static final Map<ICategory, Category> MAP = new MemoryManagementService.ManagedMap<>(150000);
    public static Category getCategory(String id){
        try {
            return getCategory((ICategory) DiscordClient.getAny(client -> client.getCategoryByID(Long.valueOf(id))));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Category getCategory(ICategory iCategory){
        if (iCategory == null) return null;
        return MAP.computeIfAbsent(iCategory, r -> new Category(iCategory));
    }
    static List<Category> getCategories(List<ICategory> categories){
        List<Category> cats = new ArrayList<>(categories.size());
        categories.forEach(iMessage -> cats.add(getCategory(iMessage)));
        return cats;
    }
    private final transient AtomicReference<ICategory> reference;
    private String ID;
    protected Category() {
        this.reference = new AtomicReference<>(DiscordClient.getAny(client -> client.getCategoryByID(Long.parseLong(ID))));
    }
    Category(ICategory category) {
        this.reference = new AtomicReference<>(category);
        this.ID = category.getStringID();
        this.registerExistence();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category other = (Category) o;
        return this.category().equals(other.category());
    }

    public ICategory category(){
        return this.reference.get();
    }

    @Override
    public int hashCode() {
        return category().hashCode();
    }


    @Override
    public String getID() {
        return this.ID;
    }

    @Override
    public ConfigLevel getConfigLevel() {
        return ConfigLevel.CATEGORY;
    }

    @Override
    public void checkPermissionToEdit(User user, Guild guild) {
        BotRole.GUILD_TRUSTEE.checkRequiredRole(user, guild);
    }

    public void delete() {
        ExceptionWrapper.wrap(() -> category().delete());
    }

    public boolean isDeleted() {
        return category().isDeleted();
    }

    public List<Channel> getChannels() {
        return Channel.getChannels(category().getChannels());
    }

    public List<VoiceChannel> getVoiceChannels() {
        return VoiceChannel.getVoiceChannels(category().getVoiceChannels());
    }

    public Guild getGuild() {
        return Guild.getGuild(category().getGuild());
    }

    public int getPosition() {
        return category().getPosition();
    }

    public void changePosition(int i) {
        ExceptionWrapper.wrap(() -> category().changePosition(i));
    }

    @Override
    public String getName() {
        return category().getName();
    }

    public void setName(String s) {
        ExceptionWrapper.wrap(() -> category().changeName(s));
    }

    public boolean isNSFW() {
        return category().isNSFW();
    }

    public void setNSFW(boolean b) {
        ExceptionWrapper.wrap(() -> category().changeNSFW(b));
    }

    public EnumSet<DiscordPermission> getModifiedPermissions(User user) {
        return DiscordPermission.getDiscordPermissions(category().getModifiedPermissions(user.user()));
    }

    public EnumSet<DiscordPermission> getModifiedPermissions(Role role) {
        return DiscordPermission.getDiscordPermissions(category().getModifiedPermissions(role.role()));
    }

    public Map<User, PermOverride> getUserOverrides() {
        return PermOverride.getUserMap(category().getUserOverrides());
    }

    public Map<User, PermOverride> getRoleOverrides() {
        return PermOverride.getUserMap(category().getRoleOverrides());
    }

    public void removePermissionsOverride(User user) {
        ExceptionWrapper.wrap(() -> category().removePermissionsOverride(user.user()));
    }

    public void removePermissionsOverride(Role role) {
        ExceptionWrapper.wrap(() -> category().removePermissionsOverride(role.role()));
    }

    public void overrideRolePermissions(Role role, EnumSet<DiscordPermission> toAdd, EnumSet<DiscordPermission> toRemove) {
        ExceptionWrapper.wrap(() -> category().overrideRolePermissions(role.role(), DiscordPermission.getPermissions(toAdd), DiscordPermission.getPermissions(toRemove)));
    }

    public void overrideUserPermissions(User user, EnumSet<DiscordPermission> toAdd, EnumSet<DiscordPermission> toRemove) {
        ExceptionWrapper.wrap(() -> category().overrideUserPermissions(user.user(), DiscordPermission.getPermissions(toAdd), DiscordPermission.getPermissions(toRemove)));
    }

    public Shard getShard() {
        return Shard.getShard(category().getShard());
    }
}
