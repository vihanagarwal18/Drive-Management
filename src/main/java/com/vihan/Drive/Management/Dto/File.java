package com.vihan.Drive.Management.Dto;

import com.vihan.Drive.Management.Constants.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class File {

    private final String id;

    private final String name;

    private final FileType fileType;

    private final String displayName;

    private final String internalPath;

    private final String externalPath;
}
