package com.github.nija123098.evelyn.template;

/**
 * @author nija123098
 * @since 1.0.0
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
