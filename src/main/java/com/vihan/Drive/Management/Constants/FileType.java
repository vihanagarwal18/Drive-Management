package com.vihan.Drive.Management.Constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum FileType {
    IMAGE("image"),
    PDF("pdf"),
    WORD("word"),
    EXCEL("excel"),
    POWERPOINT("powerpoint"),
    TEXT("text"),
    AUDIO("audio"),
    VIDEO("video");

    private String fileTypeDisplayName;
}
