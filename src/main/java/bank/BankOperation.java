package bank;

import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;

public class BankOperation {
    public Date bookingDate;
    public Date availabilityDate;
    public String act;
    public String bookingText;
    public double saldoChange;
    public double finalSaldo;

    final static private String DATEFORMAT = "dd.MM.yyyy";

    private BankOperation( Date bookingDate, Date availabilityDate, String act, String bookingText, double saldoChange) {
        this.bookingDate = bookingDate;
        this.availabilityDate = availabilityDate;
        this.act = act;
        this.bookingText = bookingText;
        this.saldoChange = saldoChange;
    }

    public void computeAddedInformation() {
        
    }

    public static BankOperation createFromString(String text) {
        
        String[] values = text.split(";");        
        Date currentBookingDate = convertStringToDate(cleanQuoutes(values[0]));
        Date currentAvailabilityDate = convertStringToDate(cleanQuoutes(values[1]));
        String currentAct = cleanQuoutes(values[2]);
        String currentBookingText = cleanQuoutes(values[3]);
        double currentSaldoChange = Double.parseDouble(cleanQuoutes(values[4]).replace(".","").replace(",","."));

        return new BankOperation(currentBookingDate, currentAvailabilityDate, currentAct, currentBookingText, currentSaldoChange);
    }

    public String toString() {
        SimpleDateFormat formater = new SimpleDateFormat(DATEFORMAT);
        return "\"" + formater.format(bookingDate) + "\";\"" + 
            formater.format(availabilityDate) + "\";\"" + 
            act + "\";\"" + 
            bookingText + "\";\"" + 
            saldoChange + "\";\"" + 
            finalSaldo + "\"";
    }

    private static String cleanQuoutes(String quoutedText) {
        return quoutedText.substring(
            quoutedText.startsWith("\"") ? 1 : 0,
            quoutedText.endsWith("\"") ? quoutedText.length() - 1 : quoutedText.length()
        );
    }

    private static Date convertStringToDate(String dateText) {
        Date date = new Date();
        try {
            return new SimpleDateFormat(DATEFORMAT).parse(dateText.replace("\"", ""));
        }
        catch(ParseException pe){
            System.out.println("An error occurred parsing a date.");
            pe.printStackTrace();
        }
        return date;
    }
}
