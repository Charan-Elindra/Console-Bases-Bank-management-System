import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Scanner;

abstract class Account {

    private String name;
    private String aadhaar;
    private String mobile;
    private String email;
    private String password;
    private String accountNumber;
    private String cardNumber;
    private String cvv;
    private String expiry;
    private String cardStatus;
    private double balance;

    private Transaction[] transactions = new Transaction[50];
    private int transactionCount = 0;

    public Account(String name, String aadhaar, String mobile, String email, String password) {
        this.name = name;
        this.aadhaar = aadhaar;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
    }

    public String getName(){
	 return name;
    }
    public void setName(String name){
	 this.name = name; 
   }
    public String getAadhaar(){
	 return aadhaar; 
    }
    public void setAadhaar(String aadhaar){
	 this.aadhaar = aadhaar; 
    }
    public String getMobile(){
	 return mobile;
     }
    public void setMobile(String mobile){
	 this.mobile = mobile;
    }
    public String getEmail(){
	 return email;
    }
    public void setEmail(String email){
	 this.email = email;
    }
    public String getPassword(){
	 return password;
     }
    public void setPassword(String password){
	 this.password = password;
     }
    public String getAccountNumber(){
	 return accountNumber;
     }
    public void setAccountNumber(String accountNumber){
	  this.accountNumber = accountNumber;
     }
    public String getCardNumber(){
	 return cardNumber;
    }
    public void setCardNumber(String cardNumber){
	 this.cardNumber = cardNumber; 
    }
    public String getCvv(){
	 return cvv;
    }
    public void setCvv(String cvv){
	 this.cvv = cvv;
     }
    public String getExpiry(){
	 return expiry;
     }
    public void setExpiry(String expiry){
	 this.expiry = expiry;
    }
    public String getCardStatus(){
	 return cardStatus;
     }
    public void setCardStatus(String cardStatus){
	 this.cardStatus = cardStatus;
    }
    public double getBalance(){
	 return balance;
    }
    public void setBalance(double balance){
	 this.balance = balance;
    }
    public abstract String getAccountType();
    public abstract double getMinimumBalance();

    public void addTransaction(Transaction t) {
        if (transactionCount < 50) {
            transactions[transactionCount] = t;
            transactionCount++;
        }
    }

    public Transaction[] getTransactions(){
	 return transactions;
    }
    public int getTransactionCount(){
	 return transactionCount;
   }
}

class SavingsAccount extends Account {
    public SavingsAccount(String name, String aadhaar, String mobile, String email, String password){
        super(name, aadhaar, mobile, email, password);
    }
    public String getAccountType(){
	 return "Savings Account";
    }
    public double getMinimumBalance(){
	 return 1000; 
    }
}

class CurrentAccount extends Account {
    public CurrentAccount(String name, String aadhaar, String mobile, String email, String password) {
        super(name, aadhaar, mobile, email, password);
    }
    public String getAccountType(){
	 return "Current Account";
    }
    public double getMinimumBalance(){
         return 5000;
    }
}

class Transaction {
    private String date;
    private String time;
    private String type;
    private double amount;
    private String mode;

    public Transaction(String date, String time, String type, double amount, String mode) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.amount = amount;
        this.mode = mode;
    }

    public String getDate(){
	 return date;
     }
    public String getTime(){
	 return time;
     }
    public String getType(){
	 return type;
    }
    public double getAmount(){
	 return amount; 
    }
    public String getMode(){
	 return mode; 
    }
}
class MaskingThread extends Thread {
    private volatile boolean stopFlag;
    private final char maskChar = '*';

    public MaskingThread(String prompt) {
        System.out.print(prompt);
    }

    public void run() {
        int originalPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        stopFlag = true;
        while (stopFlag) {
            System.out.print("\010" + maskChar);
            System.out.flush();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        Thread.currentThread().setPriority(originalPriority);
    }

    public synchronized void stopMasking() {
        this.stopFlag = false;
    }
}

class BankSystem {

    Scanner sc = new Scanner(System.in);
    Random rand = new Random();

    Account[] accounts = new Account[50];
    int accountCount = 0;

    static final int MAX_ATTEMPTS = 3;

    static final String RESET = "\u001B[0m";
    static final String CLEAR = "\u001B[2J";
    static final String HOME = "\u001B[H";
    static final String BOLD = "\u001B[1m";
    static final String SEA_BLUE = "\u001B[38;2;0;170;220m";
    static final String WHITE  = "\u001B[97m";
    static final String GREEN  = "\u001B[92m";
    static final String YELLOW = "\u001B[93m";
    static final String RED    = "\u001B[91m";

    public static void main(String[] args) {
        printBanner();
        BankSystem bank = new BankSystem();
        bank.addDefaultUsers();
        bank.mainMenu();
    }

    static void enableAnsiOnWindows() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
        }
    }

    static void printBanner() {

        enableAnsiOnWindows();

        System.out.print(CLEAR + HOME);

        int consoleWidth = 160;

        String[] title = {
    "███╗   ███╗ █████╗ ███╗   ██╗ █████╗      ██████╗  █████╗ ███╗   ██╗██╗  ██╗",
    "████╗ ████║██╔══██╗████╗  ██║██╔══██╗     ██╔══██╗██╔══██╗████╗  ██║██║ ██╔╝",
    "██╔████╔██║███████║██╔██╗ ██║███████║     ██████╔╝███████║██╔██╗ ██║█████╔╝ ",
    "██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║     ██╔══██╗██╔══██║██║╚██╗██║██╔═██╗ ",
    "██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║     ██████╔╝██║  ██║██║ ╚████║██║  ██╗",
    "╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝     ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝"
        };

        System.out.println();
        System.out.println();
        System.out.println();

        for (String line : title) {

            int padding = (consoleWidth - line.length()) / 2;

            for (int i = 0; i < padding; i++)
                System.out.print(" ");

            System.out.println(BOLD + SEA_BLUE + line + RESET);
        }

        System.out.println();

        String line =
    "==============================================================================================================";

        int pad = (consoleWidth - line.length()) / 2;

        for (int i = 0; i < pad; i++)
            System.out.print(" ");

        System.out.println(BOLD + SEA_BLUE + line + RESET);

        String s1 = "YOUR TRUST  |  OUR RESPONSIBILITY";
        pad = (consoleWidth - s1.length()) / 2;
        for (int i = 0; i < pad; i++)
            System.out.print(" ");
        System.out.println(BOLD + SEA_BLUE + s1 + RESET);

        String s2 = "SECURE BANKING  | SAFE TRANSACTIONS |  DIGITAL EXCELLENCE";
        pad = (consoleWidth - s2.length()) / 2;
        for (int i = 0; i < pad; i++)
            System.out.print(" ");
        System.out.println(BOLD + SEA_BLUE + s2 + RESET);

        String s3 = "YOUR MONEY IS ALWAYS SAFE WITH MANA BANK";
        pad = (consoleWidth - s3.length()) / 2;
        for (int i = 0; i < pad; i++)
            System.out.print(" ");
        System.out.println(BOLD + SEA_BLUE + s3 + RESET);

        int bottomPad = (consoleWidth - line.length()) / 2;

        for (int i = 0; i < bottomPad; i++)
            System.out.print(" ");

        System.out.println(BOLD + SEA_BLUE + line + RESET);
        System.out.println();
        System.out.println();
    }

    static String repeat(char c, int n) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) s.append(c);
        return s.toString();
    }

    static void centerPrint(String text) {

        int width = 160;

        String plain = text.replaceAll("\u001B\\[[;\\d]*m", "");

        int pad = (width - plain.length()) / 2;

        for (int i = 0; i < pad; i++)
            System.out.print(" ");

        System.out.print(text);
    }

    static void centerPrintln(String text) {

        int width = 160;

        String plain = text.replaceAll("\u001B\\[[;\\d]*m", "");

        int pad = (width - plain.length()) / 2;

        for (int i = 0; i < pad; i++)
            System.out.print(" ");

        System.out.println(text);
    }

    static String stripAnsi(String s) {
        return s.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    static String centerText(String text) {
        int width = 160;
        String plain = stripAnsi(text);
        int pad = (width - plain.length()) / 2;
        return repeat(' ', pad) + text;
    }

    static final String BOX_DIVIDER = "###DIVIDER###";

    static void printBox(String title, String[] lines, String borderColor, String titleColor) {

        int contentWidth = stripAnsi(title).length();
        for (String l : lines) {
            if (l.equals(BOX_DIVIDER)) continue;
            contentWidth = Math.max(contentWidth, stripAnsi(l).length());
        }
        contentWidth += 2;

        int innerWidth = contentWidth + 2;

        centerPrintln(BOLD + borderColor + "╔" + repeat('═', innerWidth) + "╗" + RESET);

        String titlePlain = stripAnsi(title);
        int titlePad = contentWidth - titlePlain.length();
        int titleLeftPad = titlePad / 2;
        int titleRightPad = titlePad - titleLeftPad;
        centerPrintln(BOLD + borderColor + "║ " + RESET +
                repeat(' ', titleLeftPad) + BOLD + titleColor + title + RESET + repeat(' ', titleRightPad) +
                " " + BOLD + borderColor + "║" + RESET);

        centerPrintln(BOLD + borderColor + "╠" + repeat('═', innerWidth) + "╣" + RESET);

        for (String l : lines) {
            if (l.equals(BOX_DIVIDER)) {
                centerPrintln(BOLD + borderColor + "╠" + repeat('═', innerWidth) + "╣" + RESET);
                continue;
            }
            int pad = contentWidth - stripAnsi(l).length();
            if (pad < 0) pad = 0;
            centerPrintln(BOLD + borderColor + "║ " + RESET + l + repeat(' ', pad) + " " + BOLD + borderColor + "║" + RESET);
        }

        centerPrintln(BOLD + borderColor + "╚" + repeat('═', innerWidth) + "╝" + RESET);
    }

    void addDefaultUsers() {
        Account acc1 = new SavingsAccount("Rahul Kumar", "492817364052", "9382746150", "rahul@gmail.com", "rahul@123");
        acc1.setBalance(5000);
        acc1.setAccountNumber("58273941605");
        giveNewCard(acc1);
        accounts[accountCount] = acc1;
        accountCount++;

        Account acc2 = new CurrentAccount("Priya Sharma", "573920481675", "8529637140", "priya@gmail.com", "priya@123");
        acc2.setBalance(8000);
        acc2.setAccountNumber("69417258302");
        giveNewCard(acc2);
        accounts[accountCount] = acc2;
        accountCount++;
    }

    void giveNewCard(Account acc) {
        acc.setCardNumber(generateCardNumber());
        acc.setCvv(generateCVV());
        acc.setExpiry(generateExpiry());
        acc.setCardStatus("ACTIVE");
    }

    void printMainMenu() {

        int width = 160;

        String border = "╔══════════════════════════════════════════════════════════════════════╗";
        String border2 = "╚══════════════════════════════════════════════════════════════════════╝";

        int pad = (width - border.length()) / 2;

        for (int i = 0; i < pad; i++) System.out.print(" ");
        System.out.println(BOLD + SEA_BLUE + border + RESET);

        String[] menu = {

        "║                                                                      ║",
        "║                      WELCOME TO MANA BANK                            ║",
        "║                                                                      ║",
        "║                          1. SIGN UP                                  ║",
        "║                                                                      ║",
        "║                          2. LOGIN                                    ║",
        "║                                                                      ║",
        "║                          3. EXIT                                     ║",
        "║                                                                      ║",
        "║                                                                      ║"

        };

        for (String s : menu) {
            for (int i = 0; i < pad; i++)
                System.out.print(" ");

            if (s.contains("WELCOME"))
                System.out.println(BOLD + WHITE + s + RESET);

            else if (s.contains("SIGN"))
                System.out.println(BOLD + GREEN + s + RESET);

            else if (s.contains("LOGIN"))
                System.out.println(BOLD + YELLOW + s + RESET);

            else if (s.contains("EXIT"))
                System.out.println(BOLD + RED + s + RESET);

            else
                System.out.println(BOLD + SEA_BLUE + s + RESET);
        }

        for (int i = 0; i < pad; i++) System.out.print(" ");
        System.out.println(BOLD + SEA_BLUE + border2 + RESET);

        System.out.println();
    }

    void mainMenu() {
        while (true) {
            System.out.println();
            printMainMenu();

            int choice = readChoice();

            switch (choice) {
                case 1:
                    signUp();
                    break;
                case 2:
                    loginMenu();
                    break;
                case 3:
                    System.out.println();
                    centerPrintln(BOLD + SEA_BLUE + "Thank you for banking with MANA BANK. Goodbye!" + RESET);
                        return;
                default:
                    centerPrintln(BOLD + RED + "Invalid choice. Please select 1, 2, or 3." + RESET);
            }
        }
    }

    void signUp() {
        System.out.println();
        centerPrintln(BOLD + WHITE + "SIGN UP" + RESET);

        int type = chooseAccountType();

        System.out.println();
        centerPrintln(BOLD + WHITE + "User Details" + RESET);

        String name = readName();
        if (name == null) {
            centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
            return;
        }

        String aadhaar = readAadhaar();
        if (aadhaar == null) {
            centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
            return;
        }

        String mobile = readMobile();
        if (mobile == null) {
            centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
            return;
        }

        String email = readEmail();
        if (email == null) {
            centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
            return;
        }

        String password = readPassword();
        if (password == null) {
            centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
            return;
        }

        Account acc;
        if (type == 1) {
            acc = new SavingsAccount(name, aadhaar, mobile, email, password);
        } else {
            acc = new CurrentAccount(name, aadhaar, mobile, email, password);
        }

        double deposit = readInitialDeposit(acc);
        if (deposit < 0) {
            centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
            return;
        }

        if (!otpCheck()) {
            centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
            return;
        }

        acc.setBalance(deposit);
        acc.setAccountNumber(generateAccountNumber());
        giveNewCard(acc);

        if (accountCount < 50) {
            accounts[accountCount] = acc;
            accountCount++;
        }

        System.out.println();
        centerPrintln(BOLD + GREEN + "| ACCOUNT CREATED SUCCESSFULLY |" + RESET);
        centerPrintln(BOLD + WHITE + "WELCOME TO THE MANA BANK FAMILY" + RESET);
        centerPrintln(BOLD + YELLOW + "YOU ARE NOW AN OFFICIAL MEMBER OF MANA BANK" + RESET);
        centerPrintln(BOLD + SEA_BLUE + "WE ARE HONORED TO SERVE YOU" + RESET);
        System.out.println();

        printBox("YOUR ACCOUNT DETAILS", new String[] {
            BOLD + YELLOW + "Account Number : " + WHITE + acc.getAccountNumber() + RESET,
            BOLD + YELLOW + "Account Type   : " + WHITE + acc.getAccountType() + RESET,
            BOLD + YELLOW + "Balance        : " + GREEN + "Rs." + money(acc.getBalance()) + RESET,
            BOX_DIVIDER,
            BOLD + YELLOW + "Card Number    : " + WHITE + acc.getCardNumber() + RESET,
            BOLD + YELLOW + "Expiry         : " + WHITE + acc.getExpiry() + RESET,
            BOLD + YELLOW + "CVV            : " + WHITE + acc.getCvv() + RESET,
            BOLD + YELLOW + "Card Status    : " + GREEN + acc.getCardStatus() + RESET
        }, SEA_BLUE, WHITE);

        System.out.println();
        centerPrintln(BOLD + SEA_BLUE + "Returning to Main Menu." + RESET);
    }

    int chooseAccountType() {
        while (true) {
            System.out.println();
            centerPrintln(BOLD + WHITE + "Select Account Type" + RESET);
            centerPrintln(BOLD + GREEN + "1. Savings Account " + RESET);
            centerPrintln(BOLD + YELLOW + "2. Current Account " + RESET);
            int choice = readChoice();
            if (choice == 1 || choice == 2) {
                return choice;
            }
            centerPrintln(BOLD + RED + "Invalid choice. Please select 1 or 2." + RESET);
        }
    }

    double readInitialDeposit(Account acc) {
        double minimum = acc.getMinimumBalance();
        System.out.println();
        centerPrintln(BOLD + WHITE + "Minimum balance for " + acc.getAccountType() + " is Rs." + money(minimum) + RESET);
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String input = ask("Enter Initial Deposit : ");
            if (input.isEmpty() || !isOnlyDigits(input)) {
                centerPrintln(BOLD + RED + "Invalid Deposit." + RESET);
                centerPrintln(BOLD + RED + "Only digits are allowed (no letters or symbols)." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            double amount = Double.parseDouble(input);
            if (amount < minimum) {
                centerPrintln(BOLD + RED + "Invalid Deposit." + RESET);
                centerPrintln(BOLD + RED + "Initial deposit must be at least Rs." + money(minimum) + "." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            return amount;
        }
        centerPrintln(BOLD + RED + "Maximum attempts reached." + RESET);
        return -1;
    }

    String readName() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            centerPrint(BOLD + YELLOW + "Enter Full Name : " + RESET);
            String name = sc.nextLine().trim();
            if (name.length() < 3 || !isOnlyLetters(name)) {
                centerPrintln(BOLD + RED + "Invalid Name." + RESET);
                centerPrintln(BOLD + RED + "Name must contain at least 3 characters and only alphabets/spaces." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            return name;
        }
        centerPrintln(BOLD + RED + "Maximum attempts reached." + RESET);
        return null;
    }

    String readAadhaar() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String aadhaar = ask("Enter Aadhaar Number : ");
            if (aadhaar.length() != 12 || !isOnlyDigits(aadhaar)) {
                centerPrintln(BOLD + RED + "Invalid Aadhaar Number." + RESET);
                centerPrintln(BOLD + RED + "Aadhaar Number must contain exactly 12 digits." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            if (aadhaarExists(aadhaar)) {
                centerPrintln(BOLD + RED + "Invalid Aadhaar Number." + RESET);
                centerPrintln(BOLD + RED + "This Aadhaar Number is already registered." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            if (hasBadPattern(aadhaar)) {
                centerPrintln(BOLD + RED + "Invalid Aadhaar Number." + RESET);
                centerPrintln(BOLD + RED + "Aadhaar Number cannot contain a sequential or repeating digit pattern." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            return aadhaar;
        }
        centerPrintln(BOLD + RED + "Maximum attempts reached." + RESET);
        return null;
    }

    String readMobile() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String mobile = ask("Enter Mobile Number : ");
            if (!isValidMobile(mobile)) {
                centerPrintln(BOLD + RED + "Invalid Mobile Number." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            if (mobileExists(mobile)) {
                centerPrintln(BOLD + RED + "Invalid Mobile Number." + RESET);
                centerPrintln(BOLD + RED + "This Mobile Number is already registered." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            if (hasBadPattern(mobile)) {
                centerPrintln(BOLD + RED + "Invalid Mobile Number." + RESET);
                centerPrintln(BOLD + RED + "Mobile Number cannot contain a sequential or repeating digit pattern." + RESET);
                showAttemptsLeft(attempt);
                continue;
            }
            return mobile;
        }
        centerPrintln(BOLD + RED + "Maximum attempts reached." + RESET);
        return null;
    }

    String readEmail() {
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String email = ask("Enter Email : ");
            if (isValidEmail(email)) {
                return email;
            }
            centerPrintln(BOLD + RED + "Invalid Email." + RESET);
            centerPrintln(BOLD + RED + "Email must look like example: koushik@gmail.com" + RESET);
            centerPrintln(BOLD + RED + "(username max 10 characters, one @, one . after @, and a recognized provider like gmail.com/yahoo.com/outlook.com)" + RESET);
            showAttemptsLeft(attempt);
        }
        centerPrintln(BOLD + RED + "Maximum attempts reached." + RESET);
        return null;
    }

    void showAttemptsLeft(int attemptJustUsed) {
        int left = MAX_ATTEMPTS - attemptJustUsed;
        if (left > 0) {
            centerPrintln(BOLD + YELLOW + "Please try again. Attempts remaining: " + left + RESET);
        }
    }

    boolean isValidMobile(String mobile) {
        if (!isOnlyDigits(mobile)) {
            return false;
        }
        if (mobile.length() == 10 && mobile.charAt(0) >= '6' && mobile.charAt(0) <= '9') {
            return true;
        }
        if (mobile.length() == 11 && mobile.charAt(0) == '0'
                && mobile.charAt(1) >= '6' && mobile.charAt(1) <= '9') {
            return true;
        }
        return false;
    }

    static final String[] ALLOWED_EMAIL_DOMAINS ={"gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "icloud.com", "protonmail.com"};

    boolean isValidEmail(String email) {
        int at = email.indexOf('@');
        if (at < 1 || at > 10) {
            return false;
        }
        if (email.indexOf('@', at + 1) != -1) {
            return false;
        }

        String local = email.substring(0, at);
        String domain = email.substring(at + 1);

        for (int i = 0; i < local.length(); i++) {
            char c = local.charAt(i);
            boolean letter = (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
            boolean digit = (c >= '0' && c <= '9');
            boolean underscore = (c == '_');
            if (!letter && !digit && !underscore) {
                return false;
            }
        }

        int dotCount = 0;
        for (int i = 0; i < domain.length(); i++) {
            if (domain.charAt(i) == '.') {
                dotCount++;
            }
        }
        if (dotCount != 1) {
            return false;
        }

        for (String allowed : ALLOWED_EMAIL_DOMAINS) {
            if (domain.equalsIgnoreCase(allowed)) {
                return true;
            }
        }
        return false;
    }

    String readPassword() {
        centerPrintln(BOLD + WHITE + "Password must have at least 8 characters, with" + RESET);
        centerPrintln(BOLD + WHITE + "one capital letter, one digit and one special character." + RESET);
        for (int attempt = 1; attempt <= 3; attempt++) {
            String pwd = askPassword("Enter Password : ");
            if (!isStrongPassword(pwd)) {
                centerPrintln(BOLD + RED + "Weak Password." + RESET);
                centerPrintln(BOLD + RED + "Use at least 8 characters with a capital letter, a digit and a special character." + RESET);
                if (attempt < 3) {
                    centerPrintln(BOLD + YELLOW + "Please try again. Attempts remaining: " + (3 - attempt) + RESET);
                }
                continue;
            }
            String confirm = askPassword("Re-enter Password : ");
            if (pwd.equals(confirm)) {
                return pwd;
            }
            centerPrintln(BOLD + RED + "Passwords do not match." + RESET);
            if (attempt < 3) {
                centerPrintln(BOLD + YELLOW + "Please try again. Attempts remaining: " + (3 - attempt) + RESET);
            }
        }
        centerPrintln(BOLD + RED + "Maximum attempts reached." + RESET);
        return null;
    }

    boolean isStrongPassword(String pwd) {
        if (pwd.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (int i = 0; i < pwd.length(); i++) {
            char c = pwd.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                hasCapital = true;
            } else if (c >= '0' && c <= '9') {
                hasDigit = true;
            } else if (!(c >= 'a' && c <= 'z')) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasDigit && hasSpecial;
    }

    boolean otpCheck() {
        String otp = generateOtp();
        System.out.println();
        centerPrintln(BOLD + GREEN + "Generated OTP : " + otp + " " + RESET);
        for (int attempt = 1; attempt <= 3; attempt++) {
            String entered = ask("Enter OTP : ");
            if (entered.equals(otp)) {
                return true;
            }
            centerPrintln(BOLD + RED + "Invalid OTP." + RESET);
            if (attempt < 3) {
                centerPrintln(BOLD + YELLOW + "Please try again. Attempts remaining: " + (3 - attempt) + RESET);
            }
        }
        return false;
    }

    void loginMenu() {
        while (true) {
            System.out.println();
            centerPrintln(BOLD + SEA_BLUE + "======================" + RESET);
            centerPrintln(BOLD + WHITE + "LOGIN" + RESET);
            centerPrintln(BOLD + SEA_BLUE + "======================" + RESET);
            centerPrintln(BOLD + GREEN + "1. User Login" + RESET);
            centerPrintln(BOLD + YELLOW + "2. Admin Login" + RESET);
            centerPrintln(BOLD + RED + "3. Back" + RESET);

            int choice = readChoice();
            switch (choice) {
                case 1:
                    userLogin();
                    break;
                case 2:
                    adminLogin();
                    break;
                case 3:
                    return;
                default:
                    centerPrintln(BOLD + RED + "Invalid choice. Please select 1, 2, or 3." + RESET);
            }
        }
    }

    void userLogin(){
        System.out.println();
        String mobile = ask("Mobile Number : ");
        String pwd = askPassword("Password : ");

        Account acc = findByMobile(mobile);
        if (acc == null || !acc.getPassword().equals(pwd)) {
            centerPrintln(BOLD + RED + "Account Details Didn't Exist." + RESET);
            centerPrintln(BOLD + RED + "Please Sign Up or Try Again." + RESET);
            return;
        }

        if (!otpCheck()){
            centerPrintln(BOLD + SEA_BLUE + "Returning to Login Menu." + RESET);
            return;
        }

        System.out.println();
        centerPrintln(BOLD + GREEN + "LOGIN SUCCESSFUL.." + RESET);
        centerPrintln(BOLD + WHITE + "WELCOME BACK TO MANA BANK" + RESET);
        userDashboard(acc);
    }

    void adminLogin(){
        for (int attempt = 1; attempt <= 3; attempt++){
            System.out.println();
            String user = ask("Username : ");
            String pwd = askPassword("Password : ");

            if (user.equals("admin") && pwd.equals("admin@123")) {
                System.out.println();
                centerPrintln(BOLD + GREEN + "ADMIN LOGIN SUCCESSFUL.." + RESET);
                adminPanel();
                return;
            }
            centerPrintln(BOLD + RED + "Invalid admin credentials." + RESET);
            if (attempt < 3) {
                centerPrintln(BOLD + YELLOW + "Please try again. Attempts remaining: " + (3 - attempt) + RESET);
            }
        }
        centerPrintln(BOLD + RED + "Maximum attempts reached. Returning to Login Menu." + RESET);
    }

    void adminPanel() {
        while (true) {
            System.out.println();
            centerPrintln(BOLD + SEA_BLUE + "══════════════════════════════════════════" + RESET);
            centerPrintln(BOLD + WHITE + "MANA BANK - ADMIN DASHBOARD" + RESET);
            centerPrintln(BOLD + SEA_BLUE + "══════════════════════════════════════════" + RESET);
            centerPrintln(BOLD + GREEN + "1. View All Accounts" + RESET);
            centerPrintln(BOLD + RED + "2. Logout" + RESET);

            int choice = readChoice();
            if (choice == 1) {
                viewAllAccounts();
            } else if (choice == 2) {
                centerPrintln(BOLD + SEA_BLUE + "Logging out of Admin Panel." + RESET);
                return;
            } else {
                centerPrintln(BOLD + RED + "Invalid choice. Please select 1 or 2." + RESET);
            }
        }
    }

    void viewAllAccounts() {
        System.out.println();
        centerPrintln(BOLD + WHITE + "Total Registered Accounts : " + GREEN + accountCount + RESET);
        System.out.println();

        for (int i = 0; i < accountCount; i++) {
            Account a = accounts[i];
            printBox("ACCOUNT #" + (i + 1), new String[] {
                BOLD + YELLOW + "Account Holder : " + WHITE + a.getName() + RESET,
                BOLD + YELLOW + "Account Number : " + WHITE + a.getAccountNumber() + RESET,
                BOLD + YELLOW + "Aadhaar        : " + WHITE + a.getAadhaar() + RESET,
                BOLD + YELLOW + "Mobile         : " + WHITE + a.getMobile() + RESET,
                BOLD + YELLOW + "Email          : " + WHITE + a.getEmail() + RESET,
                BOLD + YELLOW + "Account Type   : " + WHITE + a.getAccountType() + RESET,
                BOLD + YELLOW + "Balance        : " + GREEN + "Rs." + money(a.getBalance()) + RESET,
                BOLD + YELLOW + "Card Status    : " + WHITE + a.getCardStatus() + RESET
            }, SEA_BLUE, WHITE);
            System.out.println();
        }
    }

    void userDashboard(Account acc) {
        while (true) {
            System.out.println();
            centerPrintln(BOLD + SEA_BLUE + "====================================" + RESET);
            centerPrintln(BOLD + WHITE + "WELCOME " + acc.getName() + RESET);
            centerPrintln(BOLD + SEA_BLUE + "====================================" + RESET);
            centerPrintln(BOLD + GREEN + "1. Add Money" + RESET);
            centerPrintln(BOLD + YELLOW + "2. Fund Transfer" + RESET);
            centerPrintln(BOLD + WHITE + "3. Check Balance" + RESET);
            centerPrintln(BOLD + WHITE + "4. Transaction History" + RESET);
            centerPrintln(BOLD + SEA_BLUE + "5. Cards" + RESET);
            centerPrintln(BOLD + RED + "6. Logout" + RESET);

            int choice = readChoice();
            switch (choice) {
                case 1:
                    addMoney(acc);
                    break;
                case 2:
                    fundTransfer(acc);
                    break;
                case 3:
                    checkBalance(acc);
                    break;
                case 4:
                    transactionHistory(acc);
                    break;
                case 5:
                    cardsMenu(acc);
                    break;
                case 6:
                    centerPrintln(BOLD + SEA_BLUE + "Logging out. Session ended." + RESET);
                    return;
                default:
                    centerPrintln(BOLD + RED + "Invalid choice. Please select 1-6." + RESET);
            }
        }
    }

    void addMoney(Account acc) {
        while (true) {
            System.out.println();
            centerPrintln(BOLD + GREEN + "1. PhonePe" + RESET);
            centerPrintln(BOLD + YELLOW + "2. Debit Card" + RESET);
            centerPrintln(BOLD + RED + "3. Back" + RESET);
            int choice = readChoice();

            if (choice == 1) {
                String upi = ask("Enter UPI ID : ");
                if (!isValidUpi(upi)) {
                    centerPrintln(BOLD + RED + "Invalid UPI ID." + RESET);
                    centerPrintln(BOLD + RED + "UPI ID must look like example: koushik@gmail.com@upi" + RESET);
                    continue;
                }

                String linkedEmail = upi.substring(0, upi.length() - 4);
                Account from = findByEmail(linkedEmail);
                if (from == null) {
                    centerPrintln(BOLD + RED + "Invalid UPI ID." + RESET);
                    centerPrintln(BOLD + RED + "No account is linked to this UPI ID." + RESET);
                    return;
                }
                if (from == acc) {
                    centerPrintln(BOLD + RED + "You cannot use your own UPI ID to add money to your account." + RESET);
                    return;
                }

                double amount = readAmount("Enter Amount : ");
                if (amount > from.getBalance()) {
                    centerPrintln(BOLD + RED + "Insufficient balance in the linked account." + RESET);
                    return;
                }

                System.out.println();
                printBox("PAYING VIA UPI", new String[] {
                    BOLD + YELLOW + "UPI ID         : " + WHITE + upi + RESET,
                    BOLD + YELLOW + "Linked Account : " + WHITE + from.getAccountNumber() + RESET
                }, SEA_BLUE, WHITE);

                if (!confirmYes("Confirm (Y/N) : ")) {
                    centerPrintln(BOLD + YELLOW + "Transaction cancelled." + RESET);
                    return;
                }
                from.setBalance(from.getBalance() - amount);
                saveTransaction(from, "DEBIT", amount, "PhonePe to " + acc.getAccountNumber());
                creditMoney(acc, amount, "PhonePe from " + from.getAccountNumber());
                return;
            }else if (choice == 2) {
                String cardNum = ask("Card Number : ");
                String expiry = ask("Expiry (MM/YY) : ");
                String cvv = ask("CVV : ");
                double amount = readAmount("Amount : ");

                if (!cardNum.equals(acc.getCardNumber())
                        || !expiry.equals(acc.getExpiry())
                        || !cvv.equals(acc.getCvv())) {
                    centerPrintln(BOLD + RED + "Invalid Card Details." + RESET);
                    return;
                }
                if (acc.getCardStatus().equals("BLOCKED")) {
                    centerPrintln(BOLD + RED + "Card is Blocked." + RESET);
                    centerPrintln(BOLD + RED + "Transaction Failed." + RESET);
                    return;
                }

                System.out.println();
                printBox("CARD USED FOR THIS TRANSACTION", new String[] {
                    BOLD + YELLOW + "Card Number : " + WHITE + acc.getCardNumber() + RESET,
                    BOLD + YELLOW + "Expiry      : " + WHITE + acc.getExpiry() + RESET,
                    BOLD + YELLOW + "CVV         : " + WHITE + acc.getCvv() + RESET
                }, SEA_BLUE, WHITE);

                creditMoney(acc, amount, "Debit Card");
                return;
            } else if (choice == 3) {
                return;
            } else {
                centerPrintln(BOLD + RED + "Invalid choice. Please select 1, 2, or 3." + RESET);
            }
        }
    }

    void showAccountNumbers(Account exclude) {
        centerPrintln(BOLD + WHITE + "Available Account Numbers:" + RESET);
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i] != exclude) {
                centerPrintln(BOLD + SEA_BLUE + accounts[i].getAccountNumber() + RESET);
            }
        }
    }

    void creditMoney(Account acc, double amount, String mode) {
        acc.setBalance(acc.getBalance() + amount);
        saveTransaction(acc, "CREDIT", amount, mode);
        centerPrintln(BOLD + GREEN + "Rs." + money(amount) + " Added Successfully." + RESET);
        centerPrintln(BOLD + GREEN + "Current Balance : Rs." + money(acc.getBalance()) + RESET);
    }

    void fundTransfer(Account sender) {
        System.out.println();
        showAccountNumbers(sender);
        String receiverAccNum = ask("Receiver Account Number : ");
        Account receiver = findByAccountNumber(receiverAccNum);
        if (receiver == null) {
            centerPrintln(BOLD + RED + "Invalid Account Number." + RESET);
            centerPrintln(BOLD + RED + "Receiver does not exist." + RESET);
            return;
        }
        if (receiver == sender) {
            centerPrintln(BOLD + RED + "You cannot transfer funds to your own account." + RESET);
            return;
        }

        double amount = readAmount("Transfer Amount : ");
        if (amount > sender.getBalance()) {
            centerPrintln(BOLD + RED + "Insufficient balance." + RESET);
            return;
        }

        if (!confirmYes("Confirm (Y/N) : ")) {
            centerPrintln(BOLD + YELLOW + "Transfer cancelled." + RESET);
            return;
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        saveTransaction(sender, "DEBIT", amount, "Transfer to " + receiver.getAccountNumber());
        saveTransaction(receiver, "CREDIT", amount, "Transfer from " + sender.getAccountNumber());

        centerPrintln(BOLD + GREEN + "Rs." + money(amount) + " transferred successfully." + RESET);
    }

    void checkBalance(Account acc) {
        System.out.println();
        printBox("ACCOUNT BALANCE", new String[] {
            BOLD + YELLOW + "Account Holder  : " + WHITE + acc.getName() + RESET,
            BOLD + YELLOW + "Account Number  : " + WHITE + acc.getAccountNumber() + RESET,
            BOLD + YELLOW + "Current Balance : " + GREEN + "Rs." + money(acc.getBalance()) + RESET
        }, SEA_BLUE, WHITE);
    }

    static final int COL_DATE = 12;
    static final int COL_TIME = 8;
    static final int COL_TYPE = 8;
    static final int COL_AMOUNT = 14;

    String transactionRow(String date, String time, String type, String amount, String mode){
        return String.format("%-" + COL_DATE + "s%-" + COL_TIME + "s%-" + COL_TYPE + "s%-" + COL_AMOUNT + "s%s",
                date, time, type, amount, mode);
    }

    void transactionHistory(Account acc) {
        System.out.println();
        int count = acc.getTransactionCount();

        String header = transactionRow("Date", "Time", "Type", "Amount", "Mode/Reference");

        if (count == 0) {
            printBox("TRANSACTION HISTORY", new String[] {
                BOLD + WHITE + header + RESET,
                BOX_DIVIDER,
                BOLD + YELLOW + "No transactions yet." + RESET
            }, SEA_BLUE, WHITE);
            return;
        }

        String[] rows = new String[count];
        printTransactions(acc.getTransactions(), count - 1, rows, 0);

        String[] lines = new String[count + 2];
        lines[0] = BOLD + WHITE + header + RESET;
        lines[1] = BOX_DIVIDER;
        for (int i = 0; i < count; i++) {
            lines[i + 2] = rows[i];
        }

        printBox("TRANSACTION HISTORY", lines, SEA_BLUE, WHITE);
    }

    void printTransactions(Transaction[] txns, int index, String[] rows, int pos) {
        if (index < 0) {
            return;
        }
        Transaction t = txns[index];
        String row = transactionRow(t.getDate(), t.getTime(), t.getType(),
                "Rs." + money(t.getAmount()), t.getMode());
        rows[pos] = BOLD + WHITE + row + RESET;
        printTransactions(txns, index - 1, rows, pos + 1);
    }

    void cardsMenu(Account acc){
        while (true){
            System.out.println();
            centerPrintln(BOLD + WHITE + "1. Show Card Details" + RESET);
            if (acc.getCardStatus().equals("BLOCKED")){
                centerPrintln(BOLD + GREEN + "2. Unblock Card" + RESET);
            }else{
                centerPrintln(BOLD + RED + "2. Block Card" + RESET);
            }
            centerPrintln(BOLD + YELLOW + "3. Back" + RESET);
            int choice = readChoice();

            if(choice == 1) {
                showCardDetails(acc);
            }else if (choice == 2) {
                toggleCardBlock(acc);
            }else if (choice == 3){
                return;
            }else{
                centerPrintln(BOLD + RED + "Invalid choice. Please select 1, 2, or 3." + RESET);
            }
        }
    }

    void showCardDetails(Account acc) {
        System.out.println();
        printBox("CARD DETAILS", new String[] {
            BOLD + YELLOW + "Card Number : " + WHITE + acc.getCardNumber() + RESET,
            BOLD + YELLOW + "Expiry      : " + WHITE + acc.getExpiry() + RESET,
            BOLD + YELLOW + "Status      : " + WHITE + acc.getCardStatus() + RESET
        }, SEA_BLUE, WHITE);

        System.out.println();
        centerPrintln(BOLD + WHITE + "Before viewing CVV, please verify your identity." + RESET);
        centerPrintln(BOLD + GREEN + "1. Verify with Password" + RESET);
        centerPrintln(BOLD + YELLOW + "2. Verify with OTP" + RESET);
        int choice = readChoice();

        boolean verified;
        if (choice == 1){
            verified = passwordCheck(acc);
        }else if (choice == 2){
            verified = otpCheck();
        }else{
            centerPrintln(BOLD + RED + "Invalid choice." + RESET);
            return;
        }

        if(verified){
            centerPrintln(BOLD + GREEN + "CVV : " + acc.getCvv() + RESET);
        }else{
            centerPrintln(BOLD + RED + "Verification failed. CVV cannot be displayed." + RESET);
        }
    }

    boolean passwordCheck(Account acc){
        for (int attempt = 1; attempt <= 3; attempt++){
            String pwd = askPassword("Enter Password : ");
            if (pwd.equals(acc.getPassword())) {
                return true;
            }
            centerPrintln(BOLD + RED + "Incorrect password." + RESET);
            if (attempt < 3){
                centerPrintln(BOLD + YELLOW + "Please try again. Attempts remaining: " + (3 - attempt) + RESET);
            }
        }
        return false;
    }

     void toggleCardBlock(Account acc) {
        if (acc.getCardStatus().equals("BLOCKED")) {
            unblockCard(acc);
        } else {
            blockCard(acc);
        }
    }

    void blockCard(Account acc) {
        if (confirmYes("Are you sure you want to block your card? (Y/N) : ")) {
            acc.setCardStatus("BLOCKED");
            centerPrintln(BOLD + RED + "Card Blocked Successfully." + RESET);
        } else {
            centerPrintln(BOLD + YELLOW + "Action cancelled." + RESET);
        }
    }

    void unblockCard(Account acc) {
        if (confirmYes("Are you sure you want to unblock your card? (Y/N) : ")) {
            acc.setCardStatus("ACTIVE");
            centerPrintln(BOLD + GREEN + "Card Unblocked Successfully." + RESET);
        } else {
            centerPrintln(BOLD + YELLOW + "Action cancelled." + RESET);
        }
    }

    String ask(String message) {
        centerPrint(BOLD + YELLOW + message + RESET);
        return sc.next();
    }

    String askPassword(String message) {
        MaskingThread masker = new MaskingThread(centerText(BOLD + YELLOW + message + RESET));
        masker.start();

        StringBuilder input = new StringBuilder();
        try {
            int c;
            while ((c = System.in.read()) != -1) {
                if (c == '\n') {
                    break;
                }
                if (c == '\r') {
                    continue;
                }
                if (c == 127 || c == 8) {
                    if (input.length() > 0) {
                        input.deleteCharAt(input.length() - 1);
                    }
                    continue;
                }
                input.append((char) c);
            }
        } catch (IOException e) {
        } finally {
            masker.stopMasking();
        }
        System.out.println();
        return input.toString();
    }

    boolean confirmYes(String message) {
        String answer = ask(message);
        return answer.equalsIgnoreCase("Y");
    }

    int readChoice() {
        while (true) {
            System.out.println();
            centerPrint(BOLD + YELLOW + "Enter Choice : " + RESET);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                sc.nextLine();
                return value;
            }
            centerPrintln(BOLD + RED + "Invalid input. Please enter a number." + RESET);
            sc.next();
        }
    }

    double readAmount(String message) {
        while (true) {
            centerPrint(BOLD + YELLOW + message + RESET);
            if (sc.hasNextDouble()) {
                double value = sc.nextDouble();
                if (value > 0) {
                    return value;
                }
                centerPrintln(BOLD + RED + "Invalid amount. Amount must be greater than zero." + RESET);
            } else {
                centerPrintln(BOLD + RED + "Invalid amount. Please enter a numeric value." + RESET);
                sc.next();
            }
        }
    }

    boolean aadhaarExists(String aadhaar) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAadhaar().equals(aadhaar)) {
                return true;
            }
        }
        return false;
    }

    boolean mobileExists(String mobile) {
        return findByMobile(mobile) != null;
    }

    Account findByMobile(String mobile) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getMobile().equals(mobile)) {
                return accounts[i];
            }
        }
        return null;
    }

    Account findByEmail(String email) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getEmail().equalsIgnoreCase(email)) {
                return accounts[i];
            }
        }
        return null;
    }

    Account findByAccountNumber(String accNum) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equals(accNum)) {
                return accounts[i];
            }
        }
        return null;
    }

    void saveTransaction(Account acc, String type, double amount, String mode) {
        LocalDateTime now = LocalDateTime.now();
        String date = pad(now.getDayOfMonth()) + "-" + pad(now.getMonthValue()) + "-" + now.getYear();
        String time = pad(now.getHour()) + ":" + pad(now.getMinute());
        acc.addTransaction(new Transaction(date, time, type, amount, mode));
    }

    String pad(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return "" + value;
    }

    String money(double amount) {
        if (amount == (long) amount) {
            return "" + (long) amount;
        }
        return String.format("%.2f", amount);
    }

    boolean isOnlyDigits(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    boolean isOnlyLetters(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                return false;
            }
        }
        return true;
    }

    boolean isValidUpi(String upi) {
        if (upi.length() < 5) {
            return false;
        }
        if (!upi.endsWith("@upi")) {
            return false;
        }
        String front = upi.substring(0, upi.length() - 4);
        return isValidEmail(front);
    }

   boolean hasBadPattern(String digits) {
    return isSequential(digits, 4) || isAllSame(digits);
}
    boolean isSequential(String digits, int runLength) {
        for (int i = 0; i + runLength <= digits.length(); i++) {
            boolean increasing = true;
            boolean decreasing = true;
            for (int j = 1; j < runLength; j++) {
                if (digits.charAt(i + j) != digits.charAt(i + j - 1) + 1) {
                    increasing = false;
                }
                if (digits.charAt(i + j) != digits.charAt(i + j - 1) - 1) {
                    decreasing = false;
                }
            }
            if (increasing || decreasing) {
                return true;
            }
        }
        return false;
    }

    boolean isAllSame(String digits){
        for (int i = 1; i < digits.length(); i++){
            if (digits.charAt(i) != digits.charAt(0)){
                return false;
            }
        }
        return true;
    }

    String generateSafeNumber(int length, int runLength) {
        String num;
        do {
            num = "";
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    num = num + (1 + rand.nextInt(9));
                } else {
                    num = num + rand.nextInt(10);
                }
            }
        } while (isSequential(num, runLength) || isAllSame(num)
                || findByAccountNumber(num) != null);
        return num;
    }

    String generateOtp() {
        return generateSafeNumber(4, 4);
    }

    String generateAccountNumber() {
        return generateSafeNumber(11, 4);
    }

    String generateCardNumber() {
        return generateSafeNumber(16, 4);
    }

    String generateCVV() {
        return generateSafeNumber(3, 3);
    }

    String generateExpiry() {
        LocalDate expiry = LocalDate.now().plusYears(5);
        return pad(expiry.getMonthValue()) + "/" + pad(expiry.getYear() % 100);
    }
}