package ro.ubb.service;

import org.junit.jupiter.api.Test;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.Grade;
import ro.ubb.domain.Student;
import ro.ubb.repository.AssignmentXMLRepository;
import ro.ubb.repository.GradeXMLRepository;
import ro.ubb.repository.StudentXMLRepository;
import ro.ubb.validation.AssignmentValidator;
import ro.ubb.validation.GradeValidator;
import ro.ubb.validation.StudentValidator;
import ro.ubb.validation.Validator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private Service service;

    public ServiceTest(){
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Assignment> assignmentValidator = new AssignmentValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students.xml");
        AssignmentXMLRepository fileRepository2 = new AssignmentXMLRepository(assignmentValidator, "assignments.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "grades.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @Test
    void saveStudent() {
        int initialStudentsC = 0;
        for (Student s: service.findAllStudents()
             ) {
            initialStudentsC++;
        }

        assert service.saveStudent("2", "test", 934) == 0;

        int studentsC = 0;
        for (Student s: service.findAllStudents()
        ) {
            studentsC++;
        }
        assert initialStudentsC == studentsC - 1;

        initialStudentsC = studentsC;
        assert service.saveStudent("", "test", 934) == 1;


        studentsC = 0;
        for (Student s: service.findAllStudents()
        ) {
            studentsC++;
        }

        assert initialStudentsC == studentsC;
    }
}