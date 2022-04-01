import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class ReadPDF {

    public static BigDecimal beginBalance = new BigDecimal(0.0);
    public static BigDecimal totalPayment = new BigDecimal(0.0);
    public static BigDecimal totalPurchase = new BigDecimal(0.0);

    public static void main(String[] args) {

        System.out.println("Read PDF starts here ...");

        try {
//            String filePath = "C:\\Users\\ceag9\\OneDrive\\Documents\\CEAG\\Personal\\Financial\\BankStatements\\2021\\Chase\\CEA-CC-4540\\20210607-statements-4540-.pdf";
//            String filePath = "C:\\Users\\ceag9\\OneDrive\\Documents\\CEAG\\Personal\\Financial\\BankStatements\\2021\\Chase\\CEA-CC-4540\\20210507-statements-4540-.pdf";
            String filePath = "/Users/carlosalvarez/Downloads/20220107-statements-4540-.pdf";
            PDDocument document = PDDocument.load(new File(filePath));

            //if (!document.isEncrypted()) {
            document.setAllSecurityToBeRemoved(true);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split(System.lineSeparator());
            int i = 1;
            for (String line: lines) {
//              System.out.println("Line[" + i++ + "] : [" + line + "]");
              processLine(line, i++);
            }
            System.out.println("Begin Balance: " + beginBalance);
            System.out.println("Total Payments: " + totalPayment);
            System.out.println("Total Purchases: " + totalPurchase);
            System.out.println("End Balance: " + beginBalance.subtract(totalPayment).add(totalPurchase));
            //}
            document.close();
        }
        catch (IOException ex) {
            System.err.println("Exception while trying to read pdf document - " + ex);
        }

        System.out.println("Read PDF ends here.");

    }

    public static void processLine(String line, int index) {
        getAccountNumber(line, index);
        getPreviousBalance(line, index);
        getDatePeriod(line, index);
        getPayment(line, index);
        getPurchase(line, index);
    }

    public static void getAccountNumber(String line, int index) {
        String accountNumberRegex = "(Account Number)(:\\s)*(?<AccountNumber>(\\d|\\s)*)";
        Pattern r = Pattern.compile(accountNumberRegex);
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Line[" + index + "] : Account Number: [" + m.group(3) + "]");
        }
    }

    public static void getPreviousBalance(String line, int index) {
        String paymentRegex = "^(Previous\\s+Balance)(\\s+)\\$?(?<amount>(\\d*\\,{0,1}\\d+\\.\\d+)$)";
        Pattern r = Pattern.compile(paymentRegex);
        Matcher m = r.matcher(line);
        if (m.find( )) {
            beginBalance = beginBalance.add(new BigDecimal(m.group(3).replaceAll(",", "")));
            System.out.println("Line[" + index + "] : Previous Balance: amount=[" + m.group(3) + "]");
        }
    }


    public static void getDatePeriod(String line, int index) {
        String paymentRegex = "^(Opening\\/Closing\\s+Date)(\\s+)\\$?(?<opening>(\\d+\\/\\d+\\/\\d+))\\s*\\-\\s*\\$?(?<closing>(\\d+\\/\\d+\\/\\d+))$";
        Pattern r = Pattern.compile(paymentRegex);
        Matcher m = r.matcher(line);
        if (m.find( )) {
//            beginBalance = beginBalance.add(new BigDecimal(m.group(3).replaceAll(",", "")));
            System.out.println("Line[" + index + "] : Date Period: opening=[" + m.group(4) + "] closing=[" + m.group(6) + "]");
        }
    }

    public static void getPayment(String line, int index) {
        String paymentRegex = "(?<mm>^\\d{2})\\/(?<dd>\\d{2})(\\s+)(?<description>(.)+)(\\s+)(\\-)(?<amount>(\\d*\\,{0,1}\\d*\\.\\d+)$)";
        Pattern r = Pattern.compile(paymentRegex);
        Matcher m = r.matcher(line);
        if (m.find( )) {
            totalPayment = totalPayment.add(new BigDecimal(m.group(8).replaceAll(",", "")));
            System.out.println("Line[" + index + "] : Payment: " + m.group(1) + "/" + m.group(2) + " Description: [" + m.group(4) + "] amount=[" + m.group(8) + "]");
        }
    }

    public static void getPurchase(String line, int index) {
        String paymentRegex = "(?<mm>^\\d{2})\\/(?<dd>\\d{2})(\\s+)(?<description>(.)+)(\\s+)(?<amount>(\\d*\\,{0,1}\\d*\\.\\d+)$)";
        Pattern r = Pattern.compile(paymentRegex);
        Matcher m = r.matcher(line);
        if (m.find( )) {
            totalPurchase = totalPurchase.add(new BigDecimal(m.group(7).replaceAll(",", "")));
            System.out.println("Line[" + index + "] : Purchase: " + m.group(1) + "/" + m.group(2) + " Description: [" + m.group(4) + "] amount=[" + m.group(7) + "]");
        }
    }

    public static void main2(String[] args) {

            try (PDDocument document = PDDocument.load(new File("/tmp/encrypt.pdf"), "password")) {
                document.setAllSecurityToBeRemoved(true);

                PDFTextStripper reader = new PDFTextStripper();
                String pageText = reader.getText(document);
                System.out.println(pageText);

            } catch (IOException e) {
                System.err.println("Exception while trying to read pdf document - " + e);
            }
        }


}



