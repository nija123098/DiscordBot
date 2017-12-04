package com.github.nija123098.evelyn.template;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simplifies templates for non-programmer users.
 *
 * @author nija123098
 * @since 1.0.0
 */
public class TemplatePrepossessing {
    private static final Map<String, String> REPLACEMENT_MAP = new HashMap<>();
    static {// this will have to be updated if TemplateHandler constants change
        add("user", "context{user}");
        add("user-mention", "function{context{user}|mention}");
        add("channel", "context{channel}");
        add("channel-mention", "function{context{channel}|mention}");
        add("guild", "context{guild}");
        add("total-users", "function{context{guild}|getTotalMemberCount}");
        add("shard", "function{context{shard}|getID}");
        add("user-number", "function{context{guilduser}|getJoinPosition}");
    }// todo programmatically determine some of these
    private static void add(String name, String function){
        REPLACEMENT_MAP.put("%" + name + "%", function);
    }

    /**
     * Replaces %my-arg% with appropriate the method calls for simplicity.
     *
     * @param input the argument to add appropriate method calls instead of.
     * @return the compilable template.
     */
    static String substitute(String input){
        AtomicReference<String> reference = new AtomicReference<>(input);
        REPLACEMENT_MAP.forEach((s, s2) -> reference.updateAndGet(in -> in.replace(s, s2)));
        return reference.get();
    }
}
