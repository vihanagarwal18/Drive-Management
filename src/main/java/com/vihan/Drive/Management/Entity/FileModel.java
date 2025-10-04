package com.vihan.Drive.Management.Entity;

import com.vihan.Drive.Management.Constants.FileType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "files",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_file_id", columnNames = "id"),
        @UniqueConstraint(name = "uk_file_path_user", columnNames = {"internal_path", "user_id", "name"})
    },
    indexes = {
        @Index(name = "idx_file_user_id", columnList = "user_id"),
        @Index(name = "idx_file_type", columnList = "file_type"),
        @Index(name = "idx_file_name", columnList = "name"),
        @Index(name = "idx_file_display_name", columnList = "display_name")
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileModel {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @NotBlank(message = "File name is required")
    @Size(max = 255, message = "File name cannot exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "File type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 20)
    private FileType fileType;

    @NotNull(message = "User is required")
    @ManyToOne
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_file_user_id")
    )
    private UserModel user;

    @Size(max = 255, message = "Display name cannot exceed 255 characters")
    @Column(name = "display_name")
    private String displayName;

    @NotBlank(message = "Internal path is required")
    @Size(max = 1024, message = "Internal path cannot exceed 1024 characters")
    @Column(name = "internal_path", nullable = false, length = 1024)
    private String internalPath;

    @Size(max = 1024, message = "External path cannot exceed 1024 characters")
    @Column(name = "external_path", length = 1024)
    private String externalPath;
    
    @Column(name = "s3_key", length = 1024)
    private String s3Key;
    
    @Column(name = "content_type", length = 100)
    private String contentType;
    
    @Column(name = "size")
    private Long size;
    
    @Column(name = "is_public")
    private Boolean isPublic = false;
}