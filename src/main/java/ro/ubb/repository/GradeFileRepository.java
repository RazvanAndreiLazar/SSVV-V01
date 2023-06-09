package ro.ubb.repository;
import ro.ubb.domain.*;
import ro.ubb.validation.*;

import java.io.*;
import java.util.stream.Collectors;

public class GradeFileRepository extends AbstractFileRepository<Pair<String, String>, Grade> {

    public GradeFileRepository(Validator<Grade> validator, String filename) {
        super(validator, filename);
        loadFromFile();
    }

    protected void loadFromFile() {
        try (BufferedReader buffer = new BufferedReader(new FileReader(filename))) {
            buffer.lines().collect(Collectors.toList()).forEach(line -> {
                String[] result = line.split("#");
                Grade grade = new Grade(new Pair(result[0], result[1]), Double.parseDouble(result[2]),
                        Integer.parseInt(result[3]), result[4]);
                try {
                    super.save(grade);
                } catch (ValidationException ve) {
                    ve.printStackTrace();
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected void writeToFile(Grade grade) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(grade.getID().getObject1() + "#" + grade.getID().getObject2() + "#" + grade.getValue() + "#"
                    + grade.getDeliveryWeek() + "#" + grade.getFeedback() + "\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected void writeToFileAll() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            super.entities.values().forEach(nota -> {
                try {
                    bw.write(nota.getID().getObject1() + "#" + nota.getID().getObject2() + "#" + nota.getValue()
                            + "#" + nota.getDeliveryWeek() + "#" + nota.getFeedback() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

//    protected void createFile(Nota notaObj) {
//        String idStudent = notaObj.getID().getObject1();
//        StudentValidator sval = new StudentValidator();
//        TemaValidator tval = new TemaValidator();
//        StudentXMLRepository srepo = new StudentXMLRepository(sval, "studenti.txt");
//        TemaXMLRepository trepo = new TemaXMLRepository(tval, "teme.txt");
//
//        Student student = srepo.findOne(idStudent);
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(student.getNume() + ".txt", false))) {
//            super.findAll().forEach(nota -> {
//                if (nota.getID().getObject1().equals(idStudent)) {
//                    try {
//                        bw.write("Tema: " + nota.getID().getObject2() + "\n");
//                        bw.write("Nota: " + nota.getNota() + "\n");
//                        bw.write("Predata in saptamana: " + nota.getSaptamanaPredare() + "\n");
//                        bw.write("Deadline: " + trepo.findOne(nota.getID().getObject2()).getDeadline() + "\n");
//                        bw.write("Feedback: " + nota.getFeedback() + "\n\n");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//    }
}