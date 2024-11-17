package de.wolfpackrudel.nameandchatutils;

import de.wolfpackrudel.nameandchatutils.lang.Language;

public class TemplateHolder {

    private String globalMessageTemplate, privateMessageTemplate, name;

    public TemplateHolder(String name) {
        this(Language.getLang("globalMessageTemplate").toString(), Language.getLang("privateMessageTemplate").toString(), name);
    }

    public TemplateHolder(String globalMessageTemplate, String privateMessageTemplate, String name) {
        this.globalMessageTemplate = globalMessageTemplate;
        this.privateMessageTemplate = privateMessageTemplate;
        this.name = name;
    }

    public String getGlobalMessageTemplate() {
        return globalMessageTemplate;
    }

    public void setGlobalMessageTemplate(String globalMessageTemplate) {
        this.globalMessageTemplate = globalMessageTemplate;
    }

    public String getPrivateMessageTemplate() {
        return privateMessageTemplate;
    }

    public void setPrivateMessageTemplate(String privateMessageTemplate) {
        this.privateMessageTemplate = privateMessageTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TemplateHolder{" +
                "globalMessageTemplate='" + globalMessageTemplate + '\'' +
                ", privateMessageTemplate='" + privateMessageTemplate + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
