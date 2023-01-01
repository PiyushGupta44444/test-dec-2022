package com.example.testContainers;

import com.example.testContainers.Entity.Student;
import com.example.testContainers.repository.StudentRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TestContainersApplicationTests {
	@Container
	private static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:latest");
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void givenStudents_whenGetAllStudents_thenListOfStudents() throws Exception {
		System.out.println(mySQLContainer.getDatabaseName());
		System.out.println(mySQLContainer.getUsername());
		System.out.println(mySQLContainer.getPassword());
		System.out.println(mySQLContainer.getJdbcUrl());
		List<Student> students=List.of(Student.builder().name("Piyush").city("Ludhiana").build());
		studentRepository.saveAll(students);

		ResultActions response =mockMvc.perform(MockMvcRequestBuilders.get("/api/students"));
		response.andExpect(MockMvcResultMatchers.status().isOk());
		response.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(students.size())));
	}

}
