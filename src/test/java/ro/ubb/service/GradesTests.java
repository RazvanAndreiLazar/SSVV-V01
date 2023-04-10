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

public class GradesTests {
    private static final String STUDENTS_FILE = "studentsTest.xml";
    private static final String ASSIGNMENTS_FILE = "assignmentsTest.xml";
    private static final String GRADES_FILE = "gradesTest.xml";

    private static final String gradeFileReset = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<Entities>\n" +
            "    \n" +
            "</Entities>";

    private static final String assignmentFileReset = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<Entities>\n" +
            "    <tema ID=\"5\">\n" +
            "        <Descriere>wt</Descriere>\n" +
            "        <Deadline>4</Deadline>\n" +
            "        <Startline>1</Startline>\n" +
            "    </tema>\n" +
            "</Entities>\n";

    private static final String studentFileReset = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<Entities>\n" +
            "    <student ID=\"6\">\n" +
            "        <Nume>test</Nume>\n" +
            "        <Grupa>934</Grupa>\n" +
            "    </student>\n" +
            "</Entities>\n";

    private final Service service;

    public GradesTests(){
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Assignment> assignmentValidator = new AssignmentValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, STUDENTS_FILE);
        AssignmentXMLRepository fileRepository2 = new AssignmentXMLRepository(assignmentValidator, ASSIGNMENTS_FILE);
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, GRADES_FILE);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    private void clearStudentFile() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(STUDENTS_FILE);
        pw.write(studentFileReset);
        pw.close();
    }

    private void clearAssignmentFile() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(ASSIGNMENTS_FILE);
        pw.write(assignmentFileReset);
        pw.close();
    }

    private void clearGradeFile() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(GRADES_FILE);
        pw.write(gradeFileReset);
        pw.close();
    }

    @BeforeEach
    void Setup() throws IOException {
        clearGradeFile();
        clearAssignmentFile();
        clearStudentFile();
    }

    int countAssignments(){
        int count = 0;

        for (Assignment a: service.findAllAssignments()) {
            count++;
        }

        return count;
    }

    int countStudents(){
        int count = 0;

        for (Student s: service.findAllStudents()) {
            count++;
        }

        return count;
    }

    int countGrades(){
        int count = 0;

        for (Grade g: service.findAllGrades()) {
            count++;
        }

        return count;
    }

    @Test
    void PCA() {
        int initialAssignmentCount = countAssignments();

        int ret = service.saveAssignment("1", "wt", 4, 1);
        int assignmentCount = countAssignments();

        assert ret == 0;
        assert initialAssignmentCount == assignmentCount-1;
    }

    @Test
    void PCS(){
        int initialStudentsCount = countStudents();

        int ret = service.saveStudent("2", "test", 934);
        int studentsCount = countStudents();

        assert ret == 0;
        assert initialStudentsCount == studentsCount - 1;
    }

    @Test
    void PCG(){
        int initialGradeCount = countGrades();

        int ret = service.saveGrade("6", "5", 10, 4, "good job");
        int gradeCount = countGrades();

        assert ret == 0;
        assert initialGradeCount == gradeCount - 1;
    }

    @Test
    void PCAllEntities(){
        int initialAssignmentCount = countAssignments();
        int initialStudentsCount = countStudents();
        int initialGradeCount = countGrades();

        int retAssignment = service.saveAssignment("1", "wt", 4, 1);
        int retStudent = service.saveStudent("2", "test", 934);
        int retGrade = service.saveGrade("2", "1", 10, 4, "good job");

        int gradeCount = countGrades();
        int studentsCount = countStudents();
        int assignmentCount = countAssignments();

        assert  retAssignment == 0;
        assert  retStudent == 0;
        assert  retGrade == 0;
        assert initialAssignmentCount == assignmentCount -1;
        assert initialStudentsCount == studentsCount -1;
        assert initialGradeCount == gradeCount -1;
    }

}
