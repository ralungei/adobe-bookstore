package com.adobe.bookstore.helper;

import com.adobe.bookstore.model.Order;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class CSVHelper {

    public static ByteArrayInputStream ordersToCSV(List<Order> orders) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {

            List<String> data = Arrays.asList(
                    "Identifier",
                    "Status",
                    "Order Identifier -  Quantity"
            );
            csvPrinter.printRecord(data);
            
            for (Order order : orders) {
                data = Arrays.asList(
                        String.valueOf(order.getId()),
                        order.getStatus(),
                        String.valueOf(order.getOrderProducts())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to import data to CSV file: " + e.getMessage());
        }
    }
}