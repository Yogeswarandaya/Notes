package com.wander.note.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.wander.note.config.DBConfiguration;
import com.wander.note.config.NoteConfiguration;

@SpringBootApplication
@Import({NoteConfiguration.class, DBConfiguration.class})
public class NotesAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesAppApplication.class, args);
	}
}
