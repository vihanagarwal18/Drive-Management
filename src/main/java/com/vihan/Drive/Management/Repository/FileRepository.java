package com.vihan.Drive.Management.Repository;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Entity.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileModel, String> {
    
    @Query("SELECT f FROM FileModel f WHERE f.id = :id AND f.userName = :userName " +
           "AND f.displayName = :displayName AND f.fileType = :fileType " +
           "AND f.internalPath = :internalPath AND f.externalPath = :externalPath")
    FileModel findFileByAttributes(@Param("id") String id,
                                 @Param("userName") String userName,
                                 @Param("displayName") String displayName,
                                 @Param("fileType") FileType fileType,
                                 @Param("internalPath") String internalPath,
                                 @Param("externalPath") String externalPath);
}
