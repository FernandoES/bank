package bank;

import java.util.List;

public class TextIdentificators {
    public String header;
    public String emptyLine;
    public String saldo;
    public String openOperation;
    public String headerAddedColumn;

    private static TextIdentificators singleInstance = null;
    private static TextIdentificators IDENTIFICATORS_BEFORE_2026 =  new TextIdentificators("\"Buchungstag","", "\"Neuer Kontostand", "\"offen", "\"Stand in EUR\";");
    private static TextIdentificators IDENTIFICATORS_AFTER_2026 = new TextIdentificators("Buchungstag",";;;;;", "Neuer Kontostand", "offen", "Stand in EUR;");

    public static TextIdentificators setTextIdentificators(List<String> text) {
        if (TextIdentificators.determineIfCodedBefore2026(text)) {
          singleInstance = TextIdentificators.IDENTIFICATORS_BEFORE_2026;
        }
        singleInstance = TextIdentificators.IDENTIFICATORS_AFTER_2026;
        return TextIdentificators.getInstance();
    }

    public static synchronized TextIdentificators getInstance() {
        return singleInstance;
    }

    private TextIdentificators(String header, String emptyLine, String saldo, String openOperation, String headerAddedColumn) {
        this.header = header;
        this.emptyLine = emptyLine;
        this.saldo = saldo;
        this.openOperation = openOperation;
        this.headerAddedColumn = headerAddedColumn;
    }


    private static boolean determineIfCodedBefore2026(List<String> text) {
        if (text.indexOf(TextIdentificators.IDENTIFICATORS_BEFORE_2026.emptyLine) != -1) return true;
        if (text.indexOf(TextIdentificators.IDENTIFICATORS_AFTER_2026.emptyLine) != -1) return false;
        throw new java.lang.Error("The structure of the text could not be identified");
    }
}
