package bank;

public class Main {
    public static void main(String[] args) {
        CSVLoader loader = new CSVLoader();
        BankAccount accountOperations = loader.LoadOperations();
        accountOperations.printOperationsToFile();
        accountOperations.printMontlyReportTofile();
    }
}
