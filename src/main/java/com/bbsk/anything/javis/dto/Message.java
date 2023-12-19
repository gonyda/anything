package com.bbsk.anything.javis.dto;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@ToString
public class Message {
    private String role;
    private String content;

    public void setContent(String content) {
        this.content = StringUtils.replace(content, "\n", "<br />");
    }
}
