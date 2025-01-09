package pe.gob.bcrp.upi.process.util;

import org.apache.camel.component.file.GenericFile;

import java.util.Comparator;

public class FileSorter<T> implements Comparator<GenericFile<T>> {


    @Override
    public int compare(GenericFile<T> o1, GenericFile<T> o2) {

        if (o1.getFileName().contains("ORDER") && o2.getFileName().contains("ORDER")) {
            return 0;
        } else if ((o1.getFileName().contains("ORDER") && o2.getFileName().contains("SHIPMENT")) ||
                (o1.getFileName().contains("ORDER") && o2.getFileName().contains("TRANSACTION")) ||
                (o1.getFileName().contains("TRANSACTION") && o2.getFileName().contains("SHIPMENT"))) {
            return -1;
        } else if ((o1.getFileName().contains("SHIPMENT") && o2.getFileName().contains("ORDER")) ||
                o1.getFileName().contains("TRANSACTION") && o2.getFileName().contains("ORDER") ||
                o1.getFileName().contains("SHIPMENT") && o2.getFileName().contains("TRANSACTION")) {
            return 1;
        }
        return 0;
    }
}
