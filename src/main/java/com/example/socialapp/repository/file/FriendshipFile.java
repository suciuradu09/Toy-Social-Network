/*package com.example.socialapp.repository.file;

import com.example.socialapp.domain.Friendship;

import java.time.LocalDateTime;
import java.util.List;


public class FriendshipFile extends AbstractFileRepository<Long, Friendship> {
    public FriendshipFile(String fileName) {
        super(fileName);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Long id1 = Long.parseLong(attributes.get(1));
        Long id2 = Long.parseLong(attributes.get(2));
        LocalDateTime date = LocalDateTime.parse(attributes.get(3));
        Friendship friendship = new Friendship(id1, id2, date);
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        String date = entity.getDate().toString().split("\\.")[0];
        return entity.getId()+","+entity.getId1()+","+entity.getId2()+","+date+'\n';
    }
}
*/