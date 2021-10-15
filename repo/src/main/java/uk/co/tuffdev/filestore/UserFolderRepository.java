package uk.co.tuffdev.filestore;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface UserFolderRepository extends MongoRepository<UserFolder, String> {

    Set<UserFolder> getByUserIdAndUserRootDirIsTrue(String userId);

}
