import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private String newTokenFrom(String username, String password) {
        // Implementación para generar un nuevo token
        return "someGeneratedSuperSecretToken";
    }

    public User authUser(String username, String password) throws AccessDeniedException {
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new AccessDeniedException("Acceso denegado: usuario o contraseña incorrectos"));

        user.setToken(newTokenFrom(username, password));
        user.setTokenValidDate(LocalDateTime.now().plusMinutes(5));

        return userRepository.save(user);
    }

    public boolean authAdminArea(User user) throws AccessDeniedException {
        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());

        if (!dbUser.isPresent()) {
            throw new AccessDeniedException("Acceso denegado: usuario no encontrado en la base de datos");
        }

        User foundUser = dbUser.get();

        if (foundUser.getTokenValidDate().isBefore(LocalDateTime.now())) {
            throw new AccessDeniedException("Acceso denegado: el token ha expirado");
        }

        boolean isAuthenticated = user.getToken().equals(foundUser.getToken());
        boolean isAdmin = Role.ADMIN.equals(foundUser.getRole());

        return isAuthenticated && isAdmin;
    }
}
