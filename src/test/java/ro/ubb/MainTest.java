package ro.ubb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest
{private static final String STUDENTS_FILE = "studentsTest.xml";
    private static final String ASSIGNMENTS_FILE = "assignmentsTest.xml";
    private static final String GRADES_FILE = "gradesTest.xml";

    private final Service service;

    public MainTest(){
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Assignment> assignmentValidator = new AssignmentValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, STUDENTS_FILE);
        AssignmentXMLRepository fileRepository2 = new AssignmentXMLRepository(assignmentValidator, ASSIGNMENTS_FILE);
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, GRADES_FILE);

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @BeforeEach
    void Setup() throws IOException {
        PrintWriter pw = new PrintWriter(STUDENTS_FILE);

        pw.write(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                        "<Entities>\n" +
                        "    \n" +
                        "</Entities>");

        pw.close();

        service.findAllStudents();
    }

    int countStudents(){
        int count = 0;

        for (Student s: service.findAllStudents()
        ) {
            count++;
        }

        return count;
    }

    @Test
    void saveCorrectStudent() {
        int initialStudentsC = countStudents();

        int ret = service.saveStudent("2", "test", 934);
        int studentsC = countStudents();

        assert ret == 0;
        assert initialStudentsC == studentsC - 1;
    }

    @Test
    void testBadIdStudent(){
        int initialStudentsC = countStudents();

        int ret = service.saveStudent("", "test", 934);
        int studentsC = countStudents();

        assert ret == 1;
        assert initialStudentsC == studentsC;
    }

    @Test
    void testExistingIdStudent(){
        assert service.saveStudent("2", "test", 934) == 0;
        int initialStudentsC = countStudents();

        int ret = service.saveStudent("2", "test2", 934);
        int studentsC = countStudents();

        assert ret == 1;
        assert initialStudentsC == studentsC;
    }

    @Test
    void testSmallGroupStudent(){
        int initialStudentsC = countStudents();

        int ret = service.saveStudent("5", "test", 110);
        int studentsC = countStudents();

        assert ret == 1;
        assert initialStudentsC == studentsC;
    }

    @Test
    void testBigGroupStudent(){
        int initialStudentsC = countStudents();

        int ret = service.saveStudent("7", "test", 1000);
        int studentsC = countStudents();

        assert ret == 1;
        assert initialStudentsC == studentsC;
    }

    @Test
    void testIdBoundryAddStudent(){
        int initialStudentsC = countStudents();
        List<String> idsToBeTested = new ArrayList<>(Arrays.asList(
                " ","1.5", "abc", "null"
        ));
        List<String> errorLog = new ArrayList<>();
        int ret;

        for(String id : idsToBeTested){
            ret = service.saveStudent(id, "test", 934);
            int studentsC = countStudents();

            if(ret == 1 || initialStudentsC == studentsC){
                errorLog.add("Test failed for value " + id + "\n");
            }
        }
        assert errorLog.isEmpty();
    }

    @Test
    void testNameBoundryAddStudent(){
        int initialStudentsC = countStudents();
        List<String> namesToBeTested = new ArrayList<>(Arrays.asList(
                " ","'", "#", "null", "Student1"
        ));
        List<String> errorLog = new ArrayList<>();
        int ret;
        int id = 1;
        for(String name : namesToBeTested){
            ret = service.saveStudent(Integer.toString(id), name, 934);
            int studentsC = countStudents();

            id++;
            if(ret == 1 || initialStudentsC == studentsC){
                errorLog.add("Test failed for value " + name + "\n");
            }
        }
        assert errorLog.isEmpty();
    }

    @Test
    void testGorupBoundryAddStudent(){
        int initialStudentsC = countStudents();
        List<Integer> negativeGroupsToBeTested = new ArrayList<>(Arrays.asList(110, 938));
        List<Integer> positiveGroupsToBeTested = new ArrayList<>(Arrays.asList(111, 560, 937));
        List<String> errorLog = new ArrayList<>();
        int ret;
        int id = 1;

        for(Integer group : negativeGroupsToBeTested){
            ret = service.saveStudent(Integer.toString(id), "test", group);

            id++;
            if(ret == 0){
                errorLog.add("Test failed for value " + group + "\n");
            }
        }

        for(Integer group : positiveGroupsToBeTested){
            ret = service.saveStudent(Integer.toString(id), "test", group);

            id++;
            if(ret == 1){
                errorLog.add("Test failed for value " + group + "\n");
            }
        }
        assert errorLog.isEmpty();
    }

    @Test
    void saveCorrectAssignment() {
        String id = "1";
        String description = "grade description";
        int deadline = 5;
        int startWeek = 4;
        int result = service.saveAssignment(id, description, deadline, startWeek);
        assert result == 0;
    }

    @Test
    void saveInvalidAssignment(){
        String id = "1";
        String description = "grade description";
        int deadline = 234;
        int startWeek = 4;
        int result = service.saveAssignment(id, description, deadline, startWeek);
        assert result == 1;
    }
}
