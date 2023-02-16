package com.example.socialapp.service;

import com.example.socialapp.Utils.Events.ChangeEventType;
import com.example.socialapp.Utils.Events.FriendshipStatus;
import com.example.socialapp.Utils.Events.UtilizatorChangeData;
import com.example.socialapp.Utils.Observer.Observable;
import com.example.socialapp.Utils.Observer.Observer;
import com.example.socialapp.domain.Friendship;
import com.example.socialapp.domain.Message;
import com.example.socialapp.domain.User;
import com.example.socialapp.domain.validators.Validator;
import com.example.socialapp.exceptions.DuplicateException;
import com.example.socialapp.exceptions.LackException;
import com.example.socialapp.repository.memory.InMemoryRepository;


import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.example.socialapp.Utils.Utils.*;

public class ServiceApp implements Observable<UtilizatorChangeData> {

    /**
     * Memory repository
     */
    private final InMemoryRepository<Long, User> repository;
    private final InMemoryRepository<Long, Friendship> friendships;
    private final InMemoryRepository<Long, Message> messages;
    private final Validator<User> validator;
    private final List<Observer<UtilizatorChangeData>> observers = new ArrayList<>();


    /**
     * Class constructor
     *
     * @param repository  - user repository
     * @param friendships - friendship repository
     */
    public ServiceApp(InMemoryRepository<Long, User> repository, InMemoryRepository<Long, Friendship> friendships, InMemoryRepository<Long, Message> messages, Validator<User> validator) {
        this.repository = repository;
        this.friendships = friendships;
        this.messages = messages;
        this.validator = validator;
    }


    /**
     * Method that returns entities with ID, id.
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return - entity
     */
    public User findOne(Long id) {
        return repository.findOne(id);
    }

    public User findByUsername(String username) {
        for (User user : repository.findAll()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Method that returns all the entities from the container
     *
     * @return - all entities
     */
    public Iterable<User> findAll() {
        return repository.findAll();
    }

    /**
     * Method for user adding
     *
     * @param firstName - first name of user
     * @param lastName  - last name of user
     */
    public void saveUser(String firstName, String lastName, String username, String password) {
        Long maxId = (long) -1;
        for (User user : repository.findAll()) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        maxId++;
        byte[] salt = null;
        String stringDigestPassword = null;
        String stringSalt = null;
        try {
            salt = getSalt();
            byte[] byteDigestPassword = getSHA(password, salt);
            stringDigestPassword = bytesToHex(byteDigestPassword);
            stringSalt = bytesToHex(salt);

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        User user = new User(firstName, lastName, username, stringDigestPassword, stringSalt);
        user.setId(maxId);
        validator.validate(user);
        UtilizatorChangeData event = new UtilizatorChangeData(ChangeEventType.ADD, user);
        notifyObservers(event);
        repository.save(user);
    }

    public void saveMessage(Long from, Long to, String message) {
        Long maxId = (long) -1;
        for (Message mess : messages.findAll()) {
            if (mess.getId() > maxId) {
                maxId = mess.getId();
            }
        }
        maxId++;
        Message message_to_save = new Message(from, to, message, LocalDateTime.now());
        message_to_save.setId(maxId);
        messages.save(message_to_save);
    }

    /**
     * Deletes the entity with specified ID
     *
     * @param id - entities id, searched in the container
     *           id must be not null
     * @return - null if deletion completed successfully, otherwise throws LackException
     */
    public User delete(Long id) {
        if (!repository.exists(id)) {
            throw new LackException("User with specified ID doesn't exist!\n");
        }
        User usr = findOne(id);
        validator.validateID(usr);
        Iterable<Friendship> friendshipsList = friendships.findAll();
        for (Friendship friendship : friendshipsList) {
            if (friendship.getId1().equals(id) || friendship.getId2().equals(id)) {
                friendships.delete(friendship.getId());
            }
        }
        notifyObservers(new UtilizatorChangeData(ChangeEventType.DELETE, usr));
        return repository.delete(id);
    }

    public User update(Long id, String firstname, String lastname, String username, String password) {
        if (repository.exists(id)) {
            User user = new User(firstname, lastname, username, password, repository.findOne(id).getSalt());
            user.setId(id);
            repository.update(user);
            return user;
        }
        throw new LackException("User with specified ID doesn't exist!\n");
    }

    /**
     * Add friends of a user in a List of users
     *
     * @param id - user's id
     * @return - list of friends
     */
    public List<User> getUserFriends(Long id) {
        List<User> friends = new ArrayList<>();
        Iterable<Friendship> friendshipsList = friendships.findAll();
        for (Friendship friendship : friendshipsList) {
            if (friendship.getId1().equals(id)) {
                friends.add(findOne(friendship.getId2()));
            }
            if (friendship.getId2().equals(id)) {
                friends.add(findOne(friendship.getId1()));
            }
        }
        return friends;
    }

    /**
     * Class for adding a friend to a user
     *
     * @param id       - user id
     * @param idFriend - friend id
     * @return - friend, else if the same, exception
     */
    public User saveFriend(Long id, Long idFriend) {
        User user = repository.findOne(id);
        User friend = repository.findOne(idFriend);

        if (Objects.equals(friend, user)) {
            throw new DuplicateException("Cannot friend the same user!\n");
        }
        List<User> userFriends = getUserFriends(id);
        for (User person : userFriends) {
            if (Objects.equals(person, friend))
                throw new DuplicateException("Already friended " + friend.getId());
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        Friendship friendship = new Friendship(id, idFriend, localDateTime);

        Long maxId = (long) -1;
        for (Friendship f : friendships.findAll()) {
            if (f.getId() > maxId) {
                maxId = f.getId();
            }
        }

        friendship.setId(maxId + 1);
        friendships.save(friendship);

        return friend;
    }

    /**
     * Class for deleting a friend from a user
     *
     * @param id       - user id
     * @param idFriend - friend id
     * @return - friend, else if the same, exception
     */
    public User deleteFriend(Long id, Long idFriend) {
        User user = repository.findOne(id);
        User friend = repository.findOne(idFriend);


        if (Objects.equals(friend, user)) {
            throw new DuplicateException("Cannot unfriend the same user!\n");
        }
        List<User> userFriends = getUserFriends(id);
        List<User> friendFriends = getUserFriends(idFriend);
        if (!userFriends.remove(friend)) {
            throw new LackException("Not friends!\n");
        }
        if (!friendFriends.remove(user)) {
            throw new LackException("Not friends!\n");
        }

        Iterable<Friendship> friendshipList = friendships.findAll();
        for (Friendship friendship : friendshipList) {
            if (friendship.getId1().equals(id) && friendship.getId2().equals(idFriend) ||
                    friendship.getId1().equals(idFriend) && friendship.getId2().equals(id)) {
                friendships.delete(friendship.getId());
            }
        }
        return friend;
    }

    public void updateFriendshipStatus(Long id) {
        List<Friendship> friendshipList = this.getAllFriendships();
        for (Friendship friendship : friendshipList) {
            if (friendship.getId().equals(id)) {
                LocalDateTime localDateTime = LocalDateTime.now();
                friendship.setDate(localDateTime);
                friendship.setStatus(FriendshipStatus.ACCEPTED);
                friendships.update(friendship);
                break;
            }
        }
    }

    /**
     * Method for returning friend list of a user
     *
     * @param id - user id
     * @return list of user friends, else throws exception
     */
    public List<User> friendList(Long id) {
        List<User> userFriends = getUserFriends(id);
        if (userFriends.size() == 0) {
            throw new LackException("User is lonely!\n");
        }
        return userFriends;
    }

    /**
     * Method for returning all friendships
     *
     * @return - list of friendships
     */
    public List<Friendship> getAllFriendships() throws LackException {
        List<Friendship> friendshipList = new ArrayList<>();
        for (Friendship friendship : friendships.findAll()) {
            friendshipList.add(friendship);
        }
        if (friendshipList.size() == 0) {
            throw new LackException("No friendships found!\n");
        }
        return friendshipList;
    }

    public List<Message> getAllMessages() {
        List<Message> messageList = new ArrayList<>();
        for (Message message : messages.findAll()) {
            messageList.add(message);
        }
        return messageList;
    }

    public Friendship findFriendship(Long id) {
        return friendships.findOne(id);
    }

    public void deleteFriendship(Long id) {
        friendships.delete(id);
    }

    /**
     * Method finds the total of connected components from
     * the graph
     *
     * @return - total number of connected components
     */
    public int numberOfCommunities() {
        Iterable<User> users = repository.findAll();
        int nrOfUsers = 0;
        int nrOfCommunities = 0;
        for (User ignored : users) {
            nrOfUsers++;
        }
        int[][] matrix = new int[2 * nrOfUsers][2 * nrOfUsers];
        int[] visited = new int[nrOfUsers];
        for (int i = 0; i < nrOfUsers; i++) {
            visited[i] = 0;
        }
        for (int i = 0; i < nrOfUsers; i++) {
            for (int j = 0; j < nrOfUsers; j++) {
                matrix[i][j] = 0;
            }
        }
        for (User user : users) {
            for (User usr : friendList(user.getId())) {
                matrix[user.getId().intValue()][usr.getId().intValue()] = 1;
            }
        }
        for (int index = 0; index < nrOfUsers; index++) {
            if (visited[index] == 0) {
                dfs(index, visited, matrix, nrOfUsers);
                nrOfCommunities++;
            }
        }
        return nrOfCommunities;
    }

    /**
     * Method uses DFS to find the total number of connected components
     * from the friendship graph
     * frequency is used to find the most popular of them
     *
     * @return - list of most popular users
     */
    /*public List<User> mostSocial() {
        Iterable<User> users = repository.findAll();
        int nrOfUsers = 0;
        int nrOfCommunities = 1;
        for (User ignored : users) {
            nrOfUsers++;
        }
        int[][] matrix = new int[2 * nrOfUsers][2 * nrOfUsers];
        int[] visited = new int[nrOfUsers];
        for (int i = 0; i < nrOfUsers; i++) {
            visited[i] = 0;
        }
        for (int i = 0; i < nrOfUsers; i++) {
            for (int j = 0; j < nrOfUsers; j++) {
                matrix[i][j] = 0;
            }
        }
        for (User user : users) {
            for (User usr : friendList(user.getId())) {
                matrix[user.getId().intValue()][usr.getId().intValue()] = 1;
            }
        }
        for (int i = 0; i < nrOfUsers; i++) {
            if (visited[i] == 0) {
                dfs1(i, visited, matrix, nrOfUsers, nrOfCommunities);
                nrOfCommunities++;
            }
        }
        nrOfCommunities--;
        List<User> mostSocialUsers = new ArrayList<>();
        int[] frequency = new int[2 * nrOfCommunities];
        Arrays.fill(frequency, 0, 2 * nrOfCommunities, 0);
        int maxi = -1, maxim = -1;
        for (int index = 0; index < nrOfUsers; index++) {
            frequency[visited[index]]++;
        }
        for (int index = 0; index < nrOfCommunities; index++) {
            if (frequency[index] > maxi) {
                maxim = index;
                maxi = frequency[index];
            }
        }
        for (int index = 0; index < nrOfUsers; index++) {
            if (visited[index] == maxim) {
                for (User usr : users) {
                    if (usr.getId() == index) {
                        mostSocialUsers.add(usr);
                    }
                }
            }
        }

        return mostSocialUsers;
    }
    */
    /**
     * Depth First Search algorithm
     *
     * @param x         - start node
     * @param visited   - evidence of visited nodes
     * @param matrix    - adjacency matrix
     * @param nrOfUsers - number of nodes in the graph
     */
    private void dfs(int x, int[] visited, int[][] matrix, int nrOfUsers) {
        visited[x] = 1;
        for (int i = 0; i < nrOfUsers; i++) {
            if (matrix[x][i] == 1 && visited[i] == 0) {
                dfs(i, visited, matrix, nrOfUsers);
            }
        }
    }

    /**
     * Depth First Search algorithm that remembers connected component number
     *
     * @param x         - start node
     * @param visited   - evidence of visited nodes
     * @param matrix    - adjacency matrix
     * @param nrOfUsers - number of nodes in the graph
     */
    private void dfs1(int x, int[] visited, int[][] matrix, int nrOfUsers, int community_number) {
        visited[x] = community_number;
        for (int i = 0; i < nrOfUsers; i++) {
            if (matrix[x][i] == 1 && visited[i] == 0) {
                dfs1(i, visited, matrix, nrOfUsers, community_number);
            }
        }
    }


    @Override
    public void addObserver(Observer<UtilizatorChangeData> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<UtilizatorChangeData> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(UtilizatorChangeData t) {
        observers.forEach(x -> x.update(t));
    }


}
