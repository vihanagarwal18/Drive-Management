package com.vihan.Drive.Management.Repository;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Entity.FileModel;
import com.vihan.Drive.Management.Entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileModel, String> {

    Optional<FileModel> findByIdAndUser(String id, UserModel user);

    Optional<FileModel> findByIdAndUserAndDisplayName(String id, UserModel user, String displayName);

    @Query("SELECT f FROM FileModel f WHERE f.id = :id AND f.user = :user AND f.displayName = :displayName " +
           "AND f.fileType = :fileType AND f.internalPath = :internalPath AND f.externalPath = :externalPath")
    Optional<FileModel> findByAllParameters(
            @Param("id") String id,
            @Param("user") UserModel user,
            @Param("displayName") String displayName,
            @Param("fileType") FileType fileType,
            @Param("internalPath") String internalPath,
            @Param("externalPath") String externalPath);

    List<FileModel> findByUser(UserModel user);

    List<FileModel> findByUserAndInternalPath(UserModel user, String internalPath);

    List<FileModel> findByUserAndFileType(UserModel user, FileType fileType);

    List<FileModel> findByUserAndInternalPathAndFileType(UserModel user, String internalPath, FileType fileType);

    Optional<FileModel> findByS3Key(String s3Key);
}
