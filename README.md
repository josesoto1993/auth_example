# Autenticación y Autorización en Java

Este repositorio ofrece un vistazo detallado sobre cómo implementar la autenticación y la autorización en aplicaciones Java.

# Índice
1. [¿Qué es la Autenticación?](#qué-es-la-autenticación)
2. [¿Qué es la Autorización?](#qué-es-la-autorización)
3. [Diferencias entre Autenticación y Autorización](#diferencias-entre-autenticación-y-autorización)
3. [Filtrar llamadas REST](#filtrar-llamadas-rest)
4. [Aclaratorias y mejoras en producción](#Aclaraciones)

## ¿Qué es la Autenticación?

La autenticación es el proceso de verificar la identidad de un usuario, sistema o aplicación. En el contexto de una aplicación web, por ejemplo, se podría requerir que los usuarios ingresen un nombre de usuario y una contraseña para autenticarse antes de poder acceder a los recursos del sistema.

```java
public String authentication(UserLoginReq req) {
    User user = userRepository.findByUsernameAndPassword(req.getUsername(), req.getPassword())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Acceso denegado: usuario o contraseña incorrectos"
            ));

    // Se guarda el nuevo token
    user.setToken(newTokenFrom(req.getUsername(), req.getPassword()));
    user.setTokenValidDate(LocalDateTime.now().plusMinutes(TOKEN_VALID_MINUTES));

    User savedUser = userRepository.save(user);

    // Se retorna el token
    return savedUser.getToken();
}

```

## ¿Qué es la Autorización?

Una vez que un usuario se ha autenticado, la autorización es el proceso de determinar qué recursos o áreas tiene permiso para acceder o modificar. Esto generalmente se hace a través de roles o permisos que se asignan a los usuarios autenticados.

```java
public boolean authorizationAdmin(String token) {
    Role userRole = getUserRole(token);
    // Solo un admin puede entrar a area Admin
    return Role.ADMIN.equals(userRole);
}

public boolean authorizationUser(String token) {
    Role userRole = getUserRole(token);
    // Tanto un admin como un usuario pueden entrar a area de usuario
    return Role.ADMIN.equals(userRole) || Role.USER.equals(userRole);
}

private Role getUserRole(String token) {
    // Se busca si hay usuario para un determinado token
    Optional<User> dbUser = userRepository.findByToken(token);

    // Si no existe el usuario para el token, no es valido
    User foundUser = dbUser
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Acceso denegado: Token no encontrado en la base de datos"
            ));

    // Si el token esta expirado, no es valido
    if (foundUser.getTokenValidDate().isBefore(LocalDateTime.now())) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Acceso denegado: el token ha expirado");
    }
        
    // Si todo es valido, enviar los roles
    return foundUser.getRole();
}
```

## Diferencias entre Autenticación y Autorización

* Autenticación: Confirma quién eres.
* Autorización: Determina lo que estás permitido hacer.

En otras palabras, la autenticación se realiza antes de la autorización y es un requisito para llevar a cabo la autorización.

## Filtrar llamadas REST

Estos conceptos se deben aplicar a todas las llamadas REST, con lo que se debe aplicar un filtro que intersecta todas las llamadas (implementar Filter de javax / jakarta) y que revisa si se tienen los permisos para acceder.

```java
public void doFilter(ServletRequest request,
                     ServletResponse response,
                     FilterChain chain)
    throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    String token = req.getHeader(tokenHeader);
    if (isAnUserRequest(req)) {
        tokenService.checkIfCanAccessUser(token);
    }
    if (isAnAdminRequest(req)) {
        tokenService.checkIfCanAccessAdmin(token);
    }
    chain.doFilter(request, response);
}

private boolean isAnUserRequest(HttpServletRequest req) {
    return req.getRequestURI().contains("/user-area");
}

private boolean isAnAdminRequest(HttpServletRequest req) {
    return req.getRequestURI().contains("/admin-area");
}
```

## Aclaraciones

Esta app es solo de ejemplo sobre Autenticación y Autorización, no es para utilizar en producción ya que no esta manejando realmente tokens y no tiene un correcto manejo de errores, entre otras cosas. Es un simple ejemplo interactivo de las definiciones.
