package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CsvFileReaderImplTest {

    private static final String FILE_PATH_FOR_DATABASE =
            "src/test/resources/dataTest.csv";
    private static final String FILE_WITH_NOT_RIGHT_PATH =
            "src/main/report";
    private static final String HEADER = "fruit,quantity";
    private static final String INFO_FOR_DATABASE_1 = HEADER + System.lineSeparator()
            + "b,banana,20\n"
            + "b,apple,100\n"
            + "s,banana,100\n"
            + "p,banana,13\n"
            + "r,apple,10\n"
            + "p,apple,20\n"
            + "p,banana,5\n"
            + "s,banana,50";
    private final CsvFileReader fileReader = new CsvFileReaderImpl();

    @BeforeAll
    static void setUp() throws IOException {
        File directory = new File("src/test/resources");
        directory.mkdir();
        File file = new File(FILE_PATH_FOR_DATABASE);
        file.createNewFile();
    }

    @Test
    void readInformationFromDatabase_Ok() throws IOException {

        try (BufferedWriter writeDatabase =
                     new BufferedWriter(new FileWriter(FILE_PATH_FOR_DATABASE))) {
            writeDatabase.write(INFO_FOR_DATABASE_1);
        }

        List<String> expected = new ArrayList<>();
        expected.add(HEADER);
        expected.add("b,banana,20");
        expected.add("b,apple,100");
        expected.add("s,banana,100");
        expected.add("p,banana,13");
        expected.add("r,apple,10");
        expected.add("p,apple,20");
        expected.add("p,banana,5");
        expected.add("s,banana,50");
        StringBuilder expectedTextBuilder = new StringBuilder();
        for (String lines: expected) {
            expectedTextBuilder.append(lines);
        }
        StringBuilder actualTextBuilder = new StringBuilder();
        List<String> actual = fileReader.read(FILE_PATH_FOR_DATABASE);
        for (String lines: actual) {
            actualTextBuilder.append(lines);
        }
        assertLinesMatch(expectedTextBuilder.toString().lines(),
                actualTextBuilder.toString().lines());
    }

    @Test
    void readInformationFromDatabase_withNotCorrectPath_NotOk() throws IOException {
        try (BufferedWriter writeDatabase =
                     new BufferedWriter(new FileWriter(FILE_PATH_FOR_DATABASE))) {
            writeDatabase.write(INFO_FOR_DATABASE_1);
        }

        assertThrows(RuntimeException.class, () -> fileReader.read(FILE_WITH_NOT_RIGHT_PATH));
    }
}
