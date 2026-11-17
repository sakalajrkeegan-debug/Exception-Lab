import java.io.*;
import java.util.*;
import java.util.logging.*;

public class MarksImporter {

    private static final Logger logger =
            Logger.getLogger(MarksImporter.class.getName());

    // Custom Exception
    static class DuplicateStudentException extends RuntimeException {
        public DuplicateStudentException(String message) {
            super(message);
        }
    }

    static void importMarks(String filename) throws IOException {

        Set<String> seen = new HashSet<>();

        // Try-with-resources automatically closes the reader.
        try (BufferedReader br =
                     new BufferedReader(new FileReader(filename))) {

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {

                lineNumber++;

                try {

                    // TODO 1: Check exactly two fields
                    String[] parts = line.split(",", -1);

                    if (parts.length != 2) {
                        throw new IllegalArgumentException(
                                "Invalid record format");
                    }

                    String studentID = parts[0].trim();
                    String markText = parts[1].trim();

                    if (studentID.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Student ID cannot be empty");
                    }

                    // TODO 2: Convert and validate mark
                    int mark = Integer.parseInt(markText);

                    if (mark < 0 || mark > 100) {
                        throw new IllegalArgumentException(
                                "Mark must be between 0 and 100");
                    }

                    // TODO 3: Detect duplicate student IDs
                    if (seen.contains(studentID)) {
                        throw new DuplicateStudentException(
                                "Duplicate student ID: " + studentID);
                    }

                    seen.add(studentID);

                    // TODO 4: Display accepted record
                    System.out.println(
                            "Accepted: " + studentID + " - " + mark);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Line " + lineNumber +
                                    ": Invalid numeric mark. Record skipped.");

                    logger.warning(
                            "NumberFormatException: " + e.getMessage());

                } catch (DuplicateStudentException e) {

                    System.out.println(
                            "Line " + lineNumber +
                                    ": " + e.getMessage() + ". Record skipped.");

                    logger.warning(
                            "DuplicateStudentException: " + e.getMessage());

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Line " + lineNumber +
                                    ": " + e.getMessage() + ". Record skipped.");

                    logger.warning(
                            "IllegalArgumentException: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {

        try {

            importMarks("absent.txt");
        } catch (FileNotFoundException e) {

            System.out.println(
                    "Marks file not found; check the file name.");

            logger.severe(
                    "FileNotFoundException: " + e.getMessage());

        } catch (IOException e) {

            System.out.println(
                    "An error occurred while reading the marks file.");

            logger.severe(
                    "IOException: " + e.getMessage());

        } finally {

            System.out.println("Import attempt finished");
        }
    }
}