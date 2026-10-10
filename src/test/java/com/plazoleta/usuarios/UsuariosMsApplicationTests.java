package com.plazoleta.usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "jwt.secret=clave-solo-para-pruebas-con-al-menos-32-caracteres")
class UsuariosMsApplicationTests {

	@Test
	void contextLoads() {
	}

}
