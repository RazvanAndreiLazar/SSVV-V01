package ro.ubb.service.Mockito;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.Grade;
import ro.ubb.domain.Pair;
import ro.ubb.domain.Student;
import ro.ubb.repository.AssignmentXMLRepository;
import ro.ubb.repository.GradeXMLRepository;
import ro.ubb.repository.StudentXMLRepository;
import ro.ubb.service.Service;
import ro.ubb.validation.AssignmentValidator;
import ro.ubb.validation.GradeValidator;
import ro.ubb.validation.StudentValidator;
import ro.ubb.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Service_Mock {
    protected AutoCloseable closeable;
    @Mock
    protected StudentXMLRepository studentXmlRepo;
    @Mock
    protected AssignmentXMLRepository assignmentXmlRepo;
    @Mock
    protected GradeXMLRepository gradeXmlRepo;
    @InjectMocks
    protected Service service;

    private static final String STUDENTS_FILE = "studentsTest.xml";
    private static final String ASSIGNMENTS_FILE = "assignmentsTest.xml";
    private static final String GRADES_FILE = "gradesTest.xml";

    private static final String STUDENTS_MOCK_OBJ =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<Entities>\n" +
                    "    <student ID=\"1\">\n" +
                    "        <Nume>test</Nume>\n" +
                    "        <Grupa>934</Grupa>\n" +
                    "    </student>\n" +
                    "    <student ID=\"3\">\n" +
                    "        <Nume>test</Nume>\n" +
                    "        <Grupa>934</Grupa>\n" +
                    "    </student>\n" +
                    "</Entities>\n";

    private static final String ASSIGNMENTS_MOCK_OBJ =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<Entities>\n" +
                    "    <tema ID=\"1\">\n" +
                    "        <Descriere>wt</Descriere>\n" +
                    "        <Deadline>9</Deadline>\n" +
                    "        <Startline>7</Startline>\n" +
                    "    </tema>\n" +
                    "    <tema ID=\"3\">\n" +
                    "        <Descriere>wt</Descriere>\n" +
                    "        <Deadline>9</Deadline>\n" +
                    "        <Startline>7</Startline>\n" +
                    "    </tema>\n" +
                    "</Entities>\n";

    private static final String GRADES_MOCK_OBJ =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<Entities>\n" +
                    "    <nota IDStudent=\"1\" IDTema=\"1\">\n" +
                    "        <Nota>10.0</Nota>\n" +
                    "        <SaptamanaPredare>7</SaptamanaPredare>\n" +
                    "        <Feedback>good job</Feedback>\n" +
                    "    </nota>\n" +
                    "</Entities>\n";

    public void initFiles() throws FileNotFoundException {
        PrintWriter pwS = new PrintWriter(STUDENTS_FILE);
        pwS.write(STUDENTS_MOCK_OBJ);
        pwS.close();

        PrintWriter pwA = new PrintWriter(ASSIGNMENTS_FILE);
        pwA.write(ASSIGNMENTS_MOCK_OBJ);
        pwA.close();

        PrintWriter pwG = new PrintWriter(GRADES_FILE);
        pwG.write(GRADES_MOCK_OBJ);
        pwG.close();
    }

    public Service_Mock() {
        closeable = openMocks(this);
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Assignment> assignmentValidator = new AssignmentValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, STUDENTS_FILE);
        AssignmentXMLRepository fileRepository2 = new AssignmentXMLRepository(assignmentValidator, ASSIGNMENTS_FILE);
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, GRADES_FILE);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);

        try {
            initFiles();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    void oneTimeSetUp() throws FileNotFoundException {
        initFiles();
    }
    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Assignment> assignmentValidator = new AssignmentValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, STUDENTS_FILE);
        AssignmentXMLRepository fileRepository2 = new AssignmentXMLRepository(assignmentValidator, ASSIGNMENTS_FILE);
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, GRADES_FILE);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);

        try {
            initFiles();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
        initFiles();
    }

    public void shouldAddStudent(String id, String name, int group) {
        Student student = new Student(id, name, group);
        when(studentXmlRepo.save(any())).thenReturn(student);
        int result = service.saveStudent(id, name, group);
        assertEquals(0, result);
    }

    public void shouldAddAssignment(String id, String description, int deadlineWeek, int startWeek) {
        Assignment assignment = new Assignment(id, description, deadlineWeek, startWeek);
        when(assignmentXmlRepo.save(any())).thenReturn(assignment);
        int result = service.saveAssignment(id, description, deadlineWeek, startWeek);
        assertEquals(0, result);
    }

    public void shouldAddGrade(String studentId, String assignmentId, int value, int deliveryWeek, String feedback) {
        Grade grade = new Grade(new Pair<>(studentId, assignmentId), value, deliveryWeek, feedback);
        when(gradeXmlRepo.save(any())).thenReturn(grade);
        int result = service.saveGrade(studentId, assignmentId, value, deliveryWeek, feedback);
        assertEquals(0, result);
    }

    public void shouldNotAddStudent(String id, String name, int group) {
        Student student = new Student(id, name, group);
        when(studentXmlRepo.save(any())).thenReturn(student);
        int result = service.saveStudent(id, name, group);
        assertEquals(1, result);
    }

    public void shouldNotAddAssignment(String id, String description, int deadlineWeek, int startWeek) {
        Assignment assignment = new Assignment(id, description, deadlineWeek, startWeek);
        when(assignmentXmlRepo.save(any())).thenReturn(assignment);
        int result = service.saveAssignment(id, description, deadlineWeek, startWeek);
        assertEquals(1, result);
    }

    public void shoulNotdAddGrade(String studentId, String assignmentId, int value, int deliveryWeek, String feedback) {
        Grade grade = new Grade(new Pair<>(studentId, assignmentId), value, deliveryWeek, feedback);
        when(gradeXmlRepo.save(any())).thenReturn(grade);
        int result = service.saveGrade(studentId, assignmentId, value, deliveryWeek, feedback);
        assertEquals(1, result);
    }
}
