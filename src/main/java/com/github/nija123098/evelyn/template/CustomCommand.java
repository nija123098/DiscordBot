package com.github.nija123098.evelyn.template;

/**
 * Made by nija123098 on 8/13/2017.
 */
public class CustomCommand {
    private Template template;
    private String name, ragex;
    public CustomCommand(Template template, String name) {
        this.template = template;
        this.name = name;
    }
    public void setTemplate(Template template) {
        this.template = template;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setRagex(String ragex) {
        this.ragex = ragex;
    }
    public Template getTemplate() {
        return this.template;
    }
    public String getName() {
        return this.name;
    }
    public String getRagex() {
        return this.ragex;
    }
}
