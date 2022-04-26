package bank;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BankAccount {

    public List<BankOperation> operations;
    public List<MontlyReport> montlyReport;

    public String CSVHeader;
    public final String CSVHeaderExpansion = "\"Stand in EUR\";";
    public final String ExpansionName = "upgraded_";
    public final String ReportName = "montlyReport_";

    public BankAccount(List<String> textList, double saldo) {
        this.CSVHeader = textList.get(0);
        this.operations = textList.subList(1, textList.size()).stream().map(line -> BankOperation.createFromString(line)).collect(Collectors.toList());
        this.operations = fulfillWithCurrentSaldo(saldo, this.operations);
        this.montlyReport = MontlyReport.getAReportForEachMonth(this.operations);
    }

    public void printOperationsToFile(){
        printToFile(this.operations.stream().map(op -> op.toString()), this.CSVHeader + this.CSVHeaderExpansion, ExpansionName);
    }

    public void printMontlyReportTofile() {
        printToFile(this.montlyReport.stream().map(r -> r.printSingleMonthreportToString()), MontlyReport.HEADER, ReportName);
    }

    private static void printToFile(Stream<String> information, String header, String extensionName) {
        String infoToPrint = convertStringListToString(information,header);
        CSVLoader.printStringToFile(infoToPrint, extensionName);
    }
 
    private static List<BankOperation> fulfillWithCurrentSaldo(double lastSaldo, List<BankOperation> operations ) {
        BigDecimal saldoCurrentOperation = BigDecimal.valueOf(lastSaldo);
        for(BankOperation op: operations) {
            op.finalSaldo = saldoCurrentOperation.doubleValue();
            saldoCurrentOperation =  saldoCurrentOperation.subtract( BigDecimal.valueOf(op.saldoChange));
        }
        return operations;
    }

    private static String convertStringListToString(Stream<String> text, String header) {
        return text.reduce(header, (previous, current) -> previous + "\n" + current );
    }
}
