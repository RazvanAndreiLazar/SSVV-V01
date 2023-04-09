package ro.ubb.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class AssignmentsTests {
    private static final String STUDENTS_FILE = "studentsTest.xml";
    private static final String ASSIGNMENTS_FILE = "assignmentsTest.xml";
    private static final String GRADES_FILE = "gradesTest.xml";

    private final Service service;

    public AssignmentsTests(){
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Assignment> assignmentValidator = new AssignmentValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, STUDENTS_FILE);
        AssignmentXMLRepository fileRepository2 = new AssignmentXMLRepository(assignmentValidator, ASSIGNMENTS_FILE);
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, GRADES_FILE);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @BeforeAll
    static void OneTimeSetup() throws IOException {
        PrintWriter pw = new PrintWriter(ASSIGNMENTS_FILE);

        pw.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<Entities>\n" +
                        "    \n" +
                        "</Entities>");

        pw.close();
    }

    @BeforeEach
    void Setup() throws IOException {
        PrintWriter pw = new PrintWriter(ASSIGNMENTS_FILE);

        pw.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<Entities>\n" +
                        "    \n" +
                        "</Entities>");

        pw.close();
    }

    int countAssignments(){
        int count = 0;

        for (Assignment a: service.findAllAssignments()) {
            count++;
        }

        return count;
    }

    @Test
    void NCAInvalidId() {
        int initialAssignmentCount = countAssignments();
        String id = "", description = "wt";
        int startWeek = 2, deadline = 4;

        int ret = service.saveAssignment(id, description, deadline, startWeek);
        int studentsC = countAssignments();

        assert ret == 1;
        assert initialAssignmentCount == studentsC;
    }

    @Test
    void NCAInvalidDescription(){
        int initialAssignmentCount = countAssignments();

        String id = "1", description = "";
        int startWeek = 3, deadline = 5;

        int ret = service.saveAssignment(id, description, deadline, startWeek);
        int studentsC = countAssignments();

        assert ret == 1;
        assert initialAssignmentCount == studentsC;
    }

    @Test
    void NCAInvalidDeadline(){
        int initialAssignmentCount = countAssignments();

        String id = "2", description = "wt";
        int startWeek = 4, deadline = -2;

        int ret = service.saveAssignment(id, description, deadline, startWeek);
        int studentsC = countAssignments();

        assert ret == 1;
        assert initialAssignmentCount == studentsC;
    }

    @Test
    void NCAInvalidStartWeek(){
        int initialAssignmentCount = countAssignments();

        String id = "3", description = "wt";
        int startWeek = -2, deadline = 7;

        int ret = service.saveAssignment(id, description, deadline, startWeek);
        int studentsC = countAssignments();

        assert ret == 1;
        assert initialAssignmentCount == studentsC;
    }

    @Test
    void PCA(){
        int initialAssignmentCount = countAssignments();

        String id = "4", description = "wt";
        int startWeek = 6, deadline = 8;

        int ret = service.saveAssignment(id, description, deadline, startWeek);
        int studentsC = countAssignments();

        assert ret == 0;
        assert initialAssignmentCount == studentsC - 1;
    }

    @Test
    void NCADuplicateId(){
        int initialAssignmentCount = countAssignments();

        String id = "5", description = "wt";
        int startWeek = 7, deadline = 9;

        int ret = service.saveAssignment(id, description, deadline, startWeek);
        int studentsC = countAssignments();

        assert ret == 0;
        assert initialAssignmentCount == studentsC -1;

        ret = service.saveAssignment(id, description, deadline, startWeek);
        studentsC = countAssignments();

        assert ret == 1;
        assert initialAssignmentCount == studentsC -1;
    }
}