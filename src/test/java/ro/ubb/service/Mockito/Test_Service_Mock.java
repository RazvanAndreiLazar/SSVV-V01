package ro.ubb.service.Mockito;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import ro.ubb.domain.Assignment;
import ro.ubb.domain.Grade;
import ro.ubb.domain.Student;
import ro.ubb.repository.AssignmentXMLRepository;
import ro.ubb.repository.GradeXMLRepository;
import ro.ubb.repository.StudentXMLRepository;
import ro.ubb.service.Service;
import ro.ubb.validation.AssignmentValidator;
import ro.ubb.validation.GradeValidator;
import ro.ubb.validation.StudentValidator;
import ro.ubb.validation.Validator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Test_Service_Mock {
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
                    "    <student ID=\"2\">\n" +
                    "        <Nume>test</Nume>\n" +
                    "        <Grupa>934</Grupa>\n" +
                    "    </student>\n" +
                    "    <student ID=\"6\">\n" +
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
                    "    <tema ID=\"2\">\n" +
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
                    "    <nota IDStudent=\"2\" IDTema=\"1\">\n" +
                    "        <Nota>10.0</Nota>\n" +
                    "        <SaptamanaPredare>7</SaptamanaPredare>\n" +
                    "        <Feedback>good job</Feedback>\n" +
                    "    </nota>\n" +
                    "    <nota IDStudent=\"6\" IDTema=\"1\">\n" +
                    "        <Nota>10.0</Nota>\n" +
                    "        <SaptamanaPredare>7</SaptamanaPredare>\n" +
                    "        <Feedback>good job</Feedback>\n" +
                    "    </nota>\n" +
                    "</Entities>\n";
    private Service_Mock serviceMock;
    @Mock
    private Service service;

    public Test_Service_Mock(){
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Assignment> assignmentValidator = new AssignmentValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, STUDENTS_FILE);
        AssignmentXMLRepository fileRepository2 = new AssignmentXMLRepository(assignmentValidator, ASSIGNMENTS_FILE);
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, GRADES_FILE);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);

        try {
            PrintWriter pwS = null;
            pwS = new PrintWriter(STUDENTS_FILE);
            pwS.write(STUDENTS_MOCK_OBJ);
            pwS.close();

            PrintWriter pwA = new PrintWriter(ASSIGNMENTS_FILE);
            pwA.write(ASSIGNMENTS_MOCK_OBJ);
            pwA.close();

            PrintWriter pwG = new PrintWriter(GRADES_FILE);
            pwG.write(GRADES_MOCK_OBJ);
            pwG.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.service = (Service)Mockito.mock(Service.class);
        serviceMock = new Service_Mock(service);
    }

    @Before
    public void setup(){
        try {
            PrintWriter pwS = new PrintWriter(STUDENTS_FILE);
            pwS.write(STUDENTS_MOCK_OBJ);
            pwS.close();

            PrintWriter pwA = new PrintWriter(ASSIGNMENTS_FILE);
            pwA.write(ASSIGNMENTS_MOCK_OBJ);
            pwA.close();

            PrintWriter pwG = new PrintWriter(GRADES_FILE);
            pwG.write(GRADES_MOCK_OBJ);
            pwG.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_driver_saveStudent(){
        System.out.println("Test driver for save Student Mock");
        try{
            Mockito.verify(serviceMock.saveStudent("3", "Ana", 935)).equals(true);
            Assert.assertEquals(0, serviceMock.saveStudent("3", "Ana", 935));
            Assert.assertEquals(1, serviceMock.saveStudent("3", "Ana", 935));
            Assert.assertEquals(1, serviceMock.saveStudent("3", "Ana", 110));
            Assert.assertEquals(1, serviceMock.saveStudent("3", "Ana", 1000));
            Assert.assertEquals(4, serviceMock.countStudents());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void test_driver_saveAssignment(){
        System.out.println("Test driver for save Assignment Mock");
        try{
            Assert.assertEquals(true, service.saveAssignment("4", "mock", 9, 6));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test_driver_saveGrade(){
        System.out.println("Test driver for save Grade Mock");
        try{
            Assert.assertEquals(0, service.saveGrade("2","2", 10, 8, "gj"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        this.service = null;
    }
}
