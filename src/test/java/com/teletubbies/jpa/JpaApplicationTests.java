package com.teletubbies.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JpaApplicationTests {

	// Si esta prueba falla, casi siempre es el mapeo: ddl-auto=validate encontró
	// una entidad que no coincide con su tabla. Lee el mensaje del error.
	@Test
	void contextLoads() {
	}

}
