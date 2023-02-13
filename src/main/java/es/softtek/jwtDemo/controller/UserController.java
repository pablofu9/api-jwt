package es.softtek.jwtDemo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import es.softtek.jwtDemo.BBDD.BD;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.softtek.jwtDemo.dto.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

/*
El método login(...) interceptará las peticiones POST al endpoint /user
y recibirá como parámetros el usuario y contraseña.
Como se puede observar, para este ejemplo no se realiza ninguna validación de usuario y contraseña,
por lo que para cualquier valor de dichos parámetros dejaremos paso. Obviamente, para un proyecto real,
en este punto deberíamos autenticar el usuario contra nuestra base de datos
o contra cualquier proveedor externo.
 */

	/**
	 *
	 * Tenemos que hacer lo que hacemos en este metodo de abajo pero en vez de con un if y metiendolo a pelo
	 * debemos hacer una BBDD con una tabla usuarios, y comprobar si el usuario es valido, si lo es,
	 * nos devolvera el token.
	 */



	@PostMapping("/user")
	public User login(@RequestParam("user") String username, @RequestParam("password") String pwd) throws SQLException {
		Connection cnn = BD.abrirConexion();
		PreparedStatement st = cnn.prepareStatement("SELECT * FROM users WHERE nombre = ? AND pwd = ?");
		st.setString(1, username);
		st.setString(2, pwd);
		ResultSet rs = st.executeQuery();
		if(rs.next()){
			String token = getJWTToken(username);
			User user = new User();
			user.setUser(username);
			user.setToken(token);
			return user;

		}else{
			return null;
		}




	}

	//Utilizamos el método getJWTToken(...) para construir el token,
	// delegando en la clase de utilidad Jwts que incluye información sobre su expiración
	// y un objeto de GrantedAuthority de Spring que, como veremos más adelante,
	// usaremos para autorizar las peticiones a los recursos protegidos.

	private String getJWTToken(String username) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
}


