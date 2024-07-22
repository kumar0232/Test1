import java.util.*;

interface Authenticatable {
    boolean authenticate(String password);
}

class Account {
    String name, acc_type;
    int Acc_num, Acc_Balance;

    Account(String n, int acc_num, int b, String a_t) {
        name = n;
        Acc_num = acc_num;
        Acc_Balance = b;
        acc_type = a_t;
    }
}

class CustomerAccount extends Account implements Authenticatable {
    private final String password;
    private String street;
    private String city;

    CustomerAccount(String n, String a_t, String password, String street, String city, int initialBalance) {
        super(n, generateAccountNumber(), initialBalance, a_t);
        this.password = password;
        this.street = street;
        this.city = city;
    }

    void display_details() {
        System.out.printf("%-20s %-15d %-15d %-15s %-20s %-15s\n", name, Acc_num, Acc_Balance, acc_type, street, city);
    }

    void deposit(int money) {
        Acc_Balance += money;
    }

    int withdraw(int amount) {
        if (amount <= Acc_Balance) {
            Acc_Balance -= amount;
            System.out.println("Withdraw Successful!!\nYour Current Balance: " + Acc_Balance);
        } else {
            System.out.println("Insufficient balance. Withdrawal canceled.");
        }
        return Acc_Balance;
    }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    private static int generateAccountNumber() {
        return (int) ((Math.random() * 9000) + 1000);
    }
}

class Employee {
    String name, position;
    int emp_id;

    Employee(String n, int id, String pos) {
        name = n;
        emp_id = id;
        position = pos;
    }

    void display_details() {
        System.out.printf("%-20s %-15d %-15s\n", name, emp_id, position);
    }
}

class Admin {
    void viewProfiles(List<CustomerAccount> accounts, List<Employee> employees) {
        System.out.println("Customer Accounts:");
        System.out.printf("%-20s %-15s %-15s %-15s %-20s %-15s\n", "Name", "Account Number", "Balance", "Account Type", "Street", "City");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        for (CustomerAccount account : accounts) {
            account.display_details();
        }
        System.out.println("--------------------------------------------------------------------------------------------------------\n");

        System.out.println("Employee Profiles:");
        System.out.printf("%-20s %-15s %-15s\n", "Name", "Employee ID", "Position");
        System.out.println("---------------------------------------------------");
        for (Employee employee : employees) {
            employee.display_details();
        }
        System.out.println("---------------------------------------------------");
    }
}

public class Main {
    public static void main(String[] args) {
        List<CustomerAccount> accounts = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        Admin admin = new Admin();

        Scanner in = new Scanner(System.in);
        Scanner strng = new Scanner(System.in);
        int userChoice;
        boolean quit = false;

        do {
            System.out.println("1. Customer Register");
            System.out.println("2. Customer Login");
            System.out.println("3. Employee Login");
            System.out.println("4. Admin Login");
            System.out.println("0. Quit");
            System.out.print("Enter Your Choice: ");
            userChoice = in.nextInt();

            switch (userChoice) {
                case 1 -> customerRegister(accounts, strng);
                case 2 -> customerLogin(accounts, in, strng);
                case 3 -> employeeLogin(employees, in, strng);
                case 4 -> admin.viewProfiles(accounts, employees);
                case 0 -> quit = true;
                default -> System.out.println("Wrong Choice.");
            }
            System.out.println("\n");
        } while (!quit);
        System.out.println("Thanks!");

        in.close();
        strng.close();
    }

    private static void customerRegister(List<CustomerAccount> accounts, Scanner strng) {
        System.out.println("Customer Registration:");
        System.out.print("Enter your Name: ");
        String user_name = strng.nextLine();
        System.out.print("Enter Account Type: ");
        String type = strng.nextLine();
        System.out.print("Enter Password: ");
        String password = strng.nextLine();
        System.out.print("Enter Street: ");
        String street = strng.nextLine();
        System.out.print("Enter City: ");
        String city = strng.nextLine();
        System.out.print("Set an initial balance: ");
        int initialBalance = strng.nextInt();

        CustomerAccount user = new CustomerAccount(user_name, type, password, street, city, initialBalance);
        accounts.add(user);
        System.out.println("\n\tYour Account Details\n\tDon't Forget Account Number\n");
        System.out.println("**************************");
        user.display_details();
    }

    private static void customerLogin(List<CustomerAccount> accounts, Scanner in, Scanner strng) {
        System.out.println("Customer Login:");
        System.out.println("1. Deposit money");
        System.out.println("2. Withdraw money");
        System.out.println("3. Check balance");
        System.out.println("4. Display Account Details");
        System.out.println("0. Back to main menu");
        System.out.print("Enter Your Choice: ");
        int customerChoice = in.nextInt();

        switch (customerChoice) {
            case 1 -> {
                if (accounts.isEmpty()) {
                    System.out.println("No accounts created yet. Please register first.");
                    break;
                }
                System.out.print("Enter Account Number: ");
                int accountNumberDeposit = in.nextInt();
                CustomerAccount depositAccount = findAccount(accounts, accountNumberDeposit);
                if (depositAccount == null) {
                    System.out.println("Account not found.");
                    break;
                }
                System.out.print("Enter Password: ");
                String inputPassword = strng.nextLine();
                if (depositAccount.authenticate(inputPassword)) {
                    System.out.print("Enter Amount Of Money: ");
                    int depositAmount = in.nextInt();
                    depositAccount.deposit(depositAmount);
                    System.out.println("Deposit Successful!!");
                } else {
                    System.out.println("Wrong Password.");
                }
            }
            case 2 -> {
                if (accounts.isEmpty()) {
                    System.out.println("No accounts created yet. Please register first.");
                    break;
                }
                System.out.print("Enter Account Number: ");
                int accountNumberWithdraw = in.nextInt();
                CustomerAccount withdrawAccount = findAccount(accounts, accountNumberWithdraw);
                if (withdrawAccount == null) {
                    System.out.println("Account not found.");
                    break;
                }
                System.out.print("Enter Password: ");
                String inputPasswordWithdraw = strng.nextLine();
                if (withdrawAccount.authenticate(inputPasswordWithdraw)) {
                    System.out.print("Enter Amount Of Money to Withdraw: ");
                    int withdrawAmount = in.nextInt();
                    withdrawAccount.withdraw(withdrawAmount);
                } else {
                    System.out.println("Wrong Password.");
                }
            }
            case 3 -> {
                if (accounts.isEmpty()) {
                    System.out.println("No accounts created yet. Please register first.");
                    break;
                }
                System.out.print("Enter Account Number: ");
                int accountNumberCheck = in.nextInt();
                CustomerAccount checkAccount = findAccount(accounts, accountNumberCheck);
                if (checkAccount == null) {
                    System.out.println("Account not found.");
                    break;
                }
                System.out.println("Your Current Balance: " + checkAccount.Acc_Balance);
            }
            case 4 -> {
                if (accounts.isEmpty()) {
                    System.out.println("No accounts created yet. Please register first.");
                    break;
                }
                System.out.print("Enter Account Number: ");
                int accountNumberDisplay = in.nextInt();
                CustomerAccount displayAccount = findAccount(accounts, accountNumberDisplay);
                if (displayAccount == null) {
                    System.out.println("Account not found.");
                    break;
                }
                displayAccount.display_details();
            }
            case 0 -> System.out.println("Returning to main menu.");
            default -> System.out.println("Wrong Choice.");
        }
    }

    private static void employeeLogin(List<Employee> employees, Scanner in, Scanner strng) {
        System.out.println("Employee Login:");
        System.out.println("1. Create Employee Profile");
        System.out.println("2. Display Employee Details");
        System.out.println("0. Back to main menu");
        System.out.print("Enter Your Choice: ");
        int employeeChoice = in.nextInt();

        switch (employeeChoice) {
            case 1 -> {
                System.out.print("Enter Employee Name: ");
                String employeeName = strng.nextLine();
                System.out.print("Enter Employee ID: ");
                int employeeID = in.nextInt();
                System.out.print("Enter Position: ");
                String position = strng.nextLine();
                Employee employee = new Employee(employeeName, employeeID, position);
                employees.add(employee);
                System.out.println("\n\tEmployee Profile Created\n");
                System.out.println("**************************");
                employee.display_details();
            }
            case 2 -> {
                if (employees.isEmpty()) {
                    System.out.println("No employee profiles created yet. Please create a profile first.");
                    break;
                }
                System.out.print("Enter Employee ID: ");
                int employeeID = in.nextInt();
                Employee displayEmployee = findEmployee(employees, employeeID);
                if (displayEmployee == null) {
                    System.out.println("Employee not found.");
                    break;
                }
                displayEmployee.display_details();
            }
            case 0 -> System.out.println("Returning to main menu.");
            default -> System.out.println("Wrong Choice.");
        }
    }

    private static CustomerAccount findAccount(List<CustomerAccount> accounts, int accountNumber) {
        for (CustomerAccount account : accounts) {
            if (account.Acc_num == accountNumber) {
                return account;
            }
        }
        return null;
    }

    private static Employee findEmployee(List<Employee> employees, int employeeID) {
        for (Employee employee : employees) {
            if (employee.emp_id == employeeID) {
                return employee;
            }
        }
        return null;
    }
}
