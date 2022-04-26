package bank;

import java.time.YearMonth;
import java.util.Date;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.math.BigDecimal;

public class MontlyReport {
    double totalDeposit;
    double totalExpenses;
    YearMonth currentMonthAndYear;
    public static final String HEADER = "\"year\";\"month\";\"totalExpenses\";\"totalIncomes\";\"savedAmont\"";

    public MontlyReport(List<BankOperation> operations, YearMonth currentMonthAndYear) {
        List<BankOperation> expenseOperations = operations.stream().filter(op -> op.saldoChange < 0).collect(Collectors.toList());
        List<BankOperation> depositOperations = operations.stream().filter(op -> op.saldoChange > 0).collect(Collectors.toList());
        this.totalExpenses = addValuesInStream(expenseOperations.stream().map(op -> -op.saldoChange));
        this.totalDeposit = addValuesInStream(depositOperations.stream().map(op -> op.saldoChange));

        this.currentMonthAndYear = currentMonthAndYear;
    }

    public static List<MontlyReport> getAReportForEachMonth(List<BankOperation> operations) {
        List<YearMonth> usedMonths = operations.stream().map(op -> getYearMonthFromDate(op.availabilityDate)).distinct().collect(Collectors.toList());
        return usedMonths.stream().map((YearMonth m) -> {
            List<BankOperation> currentMonthOperations =  operations.stream().filter(op -> dateInYearMonth(op.availabilityDate, m)).collect(Collectors.toList());
            return new MontlyReport(currentMonthOperations, m);
        }).collect(Collectors.toList());
    }

    public String printSingleMonthreportToString() {
        return "\"" +
            Integer.toString(this.currentMonthAndYear.getYear()) + "\";\""  +
            this.currentMonthAndYear.getMonth()  + "\";\"" +
            Double.toString(this.totalExpenses)  + "\";\"" +
            Double.toString(this.totalDeposit)  + "\";\"" +
            BigDecimal.valueOf(this.totalDeposit).subtract(BigDecimal.valueOf(this.totalExpenses)).toString() + "\"";
        
    }

    private static boolean dateInYearMonth(Date date, YearMonth yearMonth) {
        return getYearMonthFromDate(date).equals(yearMonth);
    }

    private static YearMonth getYearMonthFromDate(Date date) {
        return YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }

    private static double addValuesInStream(Stream<Double> values) {
        return values.map(v -> BigDecimal.valueOf(v)).reduce(BigDecimal.valueOf(0), (subtotal, element) -> subtotal.add(element)).doubleValue();
    }
}
