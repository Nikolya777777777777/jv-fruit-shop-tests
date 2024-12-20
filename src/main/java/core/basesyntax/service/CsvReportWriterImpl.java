package core.basesyntax.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvReportWriterImpl implements CsvReportWriter {

    @Override
    public void write(String reportData, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(reportData);
        } catch (IOException e) {
            throw new RuntimeException("Can't write information to the file: " + fileName, e);
        }
    }
}
