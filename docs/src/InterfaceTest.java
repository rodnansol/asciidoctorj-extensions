interface UserService {

    public static final String ERROR_CODE = '123';

    /**
     * Creates a new user based on the incoming stuff.
     */
    User createUser(String username, String password);

    void deleteUser(String username);

    void deleteUser(String username, String reason);

    /**
     * Disables the incoming user.
     *
     * @param user user to disable.
     */
    void disableUser(User user);
}
