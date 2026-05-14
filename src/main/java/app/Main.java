package app;

import app.dao.StudentDao;
import app.entity.Homework;
import app.entity.Student;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        StudentDao dao = new StudentDao();

        System.out.println("\nSave test:");
        Student student = Student.builder()
                .firstName("StudentName")
                .lastName("StudentSurname")
                .email("student@example.com")
                .build();

        Homework hw1 = Homework.builder()
                .description("HW 1")
                .deadline(LocalDate.now().plusDays(7))
                .mark(0)
                .build();

        Homework hw2 = Homework.builder()
                .description("HW 2")
                .deadline(LocalDate.now().plusDays(14))
                .mark(0)
                .build();

        student.addHomework(hw1);
        student.addHomework(hw2);

        dao.save(student);
        Long savedId = student.getId();
        System.out.printf("Student saved: %s%n", student);

        System.out.println("\nFind by id test:");
        Student byId = dao.findById(savedId);
        System.out.printf("Student found by id: %s%n", byId);

        System.out.println("\nFind by email test:");
        Student byEmail = dao.findByEmail("student@example.com");
        System.out.printf("Student found by email: %s%n", byEmail);

        System.out.println("\nUpdate test:");
        student.setLastName("UpdatedSurname");
        Student updated = dao.update(student);
        System.out.printf("Updated student: %s%n", updated);

        System.out.println("\nFind student homework test:");
        dao.findById(savedId).getHomeworks().forEach(System.out::println);

        System.out.println("\nFind all students test:");
        dao.findAll().forEach(System.out::println);

        System.out.println("\nDelete test:");
        boolean deleted = dao.deleteById(savedId);
        System.out.printf("Student deleted: %b%n", deleted);
    }
}
