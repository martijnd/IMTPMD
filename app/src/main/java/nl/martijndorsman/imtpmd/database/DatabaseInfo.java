package nl.martijndorsman.imtpmd.database;

/**
 * Created by Martijn on 21/05/17.
 */

//Klasse die de tabellen en kolommen van de tabellen bevat

public class DatabaseInfo {
    //Tabellen
    public class CourseTables {
        public static final String Jaar1 = "Jaar1";
        public static final String Jaar2 = "Jaar2";
        public static final String Jaar3en4 = "Jaar3en4";
        public static final String Keuze = "Keuze";
    }
    // Kolommen. Elke tabel heeft dezelfde kolom maar gaat over een ander jaar
    public class CourseColumn {
        public static final String NAME = "NAME";
        public static final String ECTS = "ECTS";
        public static final String PERIOD = "PERIOD";
        public static final String GRADE = "GRADE";
    }
}
