class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user based on the incoming stuff.
     */
    public final User createUser(String username, String password) {
        LOGGER.info("Creating new user with username:[{}]", username);
        userRepository.save(new User(username, password));
    }

    protected void deleteUser(String username) {
        LOGGER.info("Deleting user with username:[{}]", username);
        userRepository.deleteByUsername(username);
    }

    void disableUser(User user) {
        LOGGER.info("Disabling user with username:[{}]", user.username);
        if (user.enabled == true) {
            user.enabled = false;
            userRepository.save(user);
        } else {
            logUnableToDisableUser();
        }
    }

    private static void logUnableToDisableUser() {
        LOGGER.info("Unable to disable disabled user");
    }

}

class User {
    public String username;
    public String password;
    public boolean enabled;
}

interface UserRepository {
    save(User user);

    deleteByUsername(String username);
}
