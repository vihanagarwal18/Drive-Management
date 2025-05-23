package com.vihan.Drive.Management.Dto;

import com.vihan.Drive.Management.Constants.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
public class File {

    private String id;

    private String name;

    private FileType fileType;

    private User user;

    private String displayName;

    private String internalPath;

    private String externalPath;
}
