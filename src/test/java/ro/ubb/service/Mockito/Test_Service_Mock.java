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
    private final Service_Mock serviceMock;

    public Test_Service_Mock(){
        serviceMock = new Service_Mock();
    }

    @Test
    public void saveStudentsTests(){
        serviceMock.shouldAddStudent("2", "Ana", 934);
        serviceMock.shouldNotAddStudent("2", "Ana", 934);
        serviceMock.shouldNotAddStudent("4", "Maria", 1100);
        serviceMock.shouldNotAddStudent("5", "Maria", 100);
    }

    @Test
    public void saveAssignmentsTests(){
        serviceMock.shouldAddAssignment("2", "assignment", 9, 7);
        serviceMock.shouldNotAddAssignment("2", "assignment", 9, 7);
        serviceMock.shouldNotAddAssignment("", "", 9, 7);
        serviceMock.shouldNotAddAssignment("2", "assignment", -2, 7);
    }

    @Test
    public void saveGradesTests(){
        serviceMock.shouldAddGrade("3", "3", 10, 7, "good");
        serviceMock.shoulNotdAddGrade("3", "3", 10, 7, "good");
    }

    @Test
    public void integrationMock(){
        serviceMock.shouldAddStudent("10", "Ana", 934);
        serviceMock.shouldAddAssignment("10", "assignment", 9, 7);
        serviceMock.shouldAddGrade("10", "10", 10, 7, "good");
    }


}
