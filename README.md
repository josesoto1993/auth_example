# Autenticación y Autorización en Java

Este repositorio ofrece un vistazo detallado sobre cómo implementar la autenticación y la autorización en aplicaciones Java.

# Índice
1. [¿Qué es la Autenticación?](#qué-es-la-autenticación)
2. [¿Qué es la Autorización?](#qué-es-la-autorización)
3. [Diferencias entre Autenticación y Autorización](#diferencias-entre-autenticación-y-autorización)
4. [Aclaratorias y mejoras en producción](#Aclaraciones)

## ¿Qué es la Autenticación?

La autenticación es el proceso de verificar la identidad de un usuario, sistema o aplicación. En el contexto de una aplicación web, por ejemplo, se podría requerir que los usuarios ingresen un nombre de usuario y una contraseña para autenticarse antes de poder acceder a los recursos del sistema.

```
public User authentication(String username, String password) throws AccessDeniedException {
    Optional<User> optionalUser = userRepository.findUserForUsernameAndPassword(username, password);
    
    if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        // Se añade algun método para mantener el auth, en este caso token
        user.setToken(newTokenFrom(username, password));
        return user;
    } else {
        throw new AccessDeniedException("Acceso denegado: usuario o contraseña incorrectos");
    }
}

```

## ¿Qué es la Autorización?

Una vez que un usuario se ha autenticado, la autorización es el proceso de determinar qué recursos o áreas tiene permiso para acceder o modificar. Esto generalmente se hace a través de roles o permisos que se asignan a los usuarios autenticados.

```
public boolean authorization(User user) throws AccessDeniedException {
    Optional<User> dbUser = userRepository.findUserForUsername(user.getUsername());
    
    if (!dbUser.isPresent()) {
        throw new AccessDeniedException("Acceso denegado: usuario no encontrado en la base de datos");
    }
    
    boolean isAuthenticated = user.getToken().equals(dbUser.get().getToken());
    boolean isAdmin = Roles.ADMIN.equals(user.getRole());
    
    return isAuthenticated && isAdmin;
}
```

## Diferencias entre Autenticación y Autorización

* Autenticación: Confirma quién eres.
* Autorización: Determina lo que estás permitido hacer.

En otras palabras, la autenticación se realiza antes de la autorización y es un requisito para llevar a cabo la autorización.

## Aclaraciones

Esta app es solo de ejemplo sobre Autenticación y Autorización, no es para utilizar en producción ya que no esta manejando realmente tokens y no tiene un correcto manejo de errores, entre otras cosas. Es un simple ejemplo interactivo de las definiciones.
