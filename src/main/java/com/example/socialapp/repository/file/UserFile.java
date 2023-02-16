/*package com.example.socialapp.repository.file;

import com.example.socialapp.domain.User;
import java.util.List;

public class UserFile extends AbstractFileRepository<Long, User> {
    public UserFile(String fileName) {
        super(fileName);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1),attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));
        user.setFirstName(attributes.get(1));
        user.setLastName(attributes.get(2));

        return user;
    }

    @Override
    protected String createEntityAsString(User entity){
        return entity.getId()+","+entity.getFirstName()+","+entity.getLastName()+'\n';
    }
}
*/