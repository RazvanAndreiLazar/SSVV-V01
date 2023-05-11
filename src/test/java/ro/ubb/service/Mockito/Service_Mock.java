package ro.ubb.service.Mockito;

import ro.ubb.domain.Assignment;
import ro.ubb.domain.Grade;
import ro.ubb.domain.Pair;
import ro.ubb.domain.Student;
import ro.ubb.repository.AssignmentXMLRepository;
import ro.ubb.repository.GradeXMLRepository;
import ro.ubb.repository.StudentXMLRepository;
import ro.ubb.service.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class Service_Mock {
    private StudentXMLRepository studentXmlRepo;
    private AssignmentXMLRepository assignmentXmlRepo;
    private GradeXMLRepository gradeXmlRepo;

    private Service service;

    public Service_Mock(Service service) {
        this.service = service;
    }

    public Service_Mock(StudentXMLRepository studentXmlRepo, AssignmentXMLRepository assignmentXmlRepo, GradeXMLRepository gradeXmlRepo) {
        this.studentXmlRepo = studentXmlRepo;
        this.assignmentXmlRepo = assignmentXmlRepo;
        this.gradeXmlRepo = gradeXmlRepo;
    }

    public int countStudents(){
        return service.countStudents();
    }
    public int countAssignments(){
        return service.countAssignments();
    }

    public int countGrades(){
        int count = 0;

        for (Grade g: this.findAllGrades()) {
            count++;
        }

        return count;
    }
    public Iterable<Student> findAllStudents() { return studentXmlRepo.findAll(); }

    public Iterable<Assignment> findAllAssignments() { return assignmentXmlRepo.findAll(); }

    public Iterable<Grade> findAllGrades() { return gradeXmlRepo.findAll(); }

    public Boolean saveStudent(String id, String name, int group) {
        return service.saveStudent(id, name, group) == 0;
    }

    public Boolean saveAssignment(String id, String description, int deadline, int startWeek) {
       return service.saveAssignment(id, description, deadline, startWeek) == 0;
    }

    public int saveGrade(String idStudent, String idAssignment, double gradeVal, int deliveryWeek, String feedback) {
        return service.saveGrade(idStudent, idAssignment, gradeVal, deliveryWeek, feedback);
    }

    public int deleteStudent(String id) {
        Student result = studentXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int deleteAssignment(String id) {
        Assignment result = assignmentXmlRepo.delete(id);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int updateStudent(String id, String numeNou, int grupaNoua) {
        Student studentNou = new Student(id, numeNou, grupaNoua);
        Student result = studentXmlRepo.update(studentNou);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int updateAssignment(String id, String newDescription, int newDeadline, int newStartWeek) {
        Assignment newAssignment = new Assignment(id, newDescription, newDeadline, newStartWeek);
        Assignment result = assignmentXmlRepo.update(newAssignment);

        if (result == null) {
            return 0;
        }
        return 1;
    }

    public int extendDeadline(String id, int numberOfWeeks) {
        Assignment assignment = assignmentXmlRepo.findOne(id);

        if (assignment != null) {
            LocalDate date = LocalDate.now();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int currentWeek = date.get(weekFields.weekOfWeekBasedYear());

            if (currentWeek >= 39) {
                currentWeek = currentWeek - 39;
            } else {
                currentWeek = currentWeek + 12;
            }

            if (currentWeek <= assignment.getDeadlineWeek()) {
                int deadlineNou = assignment.getDeadlineWeek() + numberOfWeeks;
                return updateAssignment(assignment.getID(), assignment.getDescription(), deadlineNou, assignment.getStartWeek());
            }
        }
        return 0;
    }

    public void createStudentFile(String idStudent, String idAssignment) {
        Grade grade = gradeXmlRepo.findOne(new Pair<>(idStudent, idAssignment));

        gradeXmlRepo.createFile(grade);
    }
}
