import java.util.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
class Database{
    Connection con = null;
    PreparedStatement pst;
    Statement st;
    ResultSet rSet;
    Database()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");

        } catch (Exception e) {
            System.out.print(e);
        }
    }

    //bankdb table

    public String getname(String acno)
    {
        String fname,lname,name;
        String sql = "select * from bankdb where acc_num = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            rSet = pst.executeQuery();
            if (rSet.next()) {
                fname = rSet.getString(2);
                lname = rSet.getString(3);
                name = fname+" "+lname;
                pst.close();
                return name;
            }
             
        } catch (Exception e) {
            System.out.print(e);
        }
         return null;
    }

    public String getGender(String acno)
    {
        String gender;
        String sql = "select * from bankdb where acc_num = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            rSet = pst.executeQuery();
            if (rSet.next()) {
                gender = rSet.getString(4);
                pst.close();
                return gender;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
                    
    }
}
class function extends Database{
 
    public void insert(String acno,String fname,String lname,String gender,String mob,int age,double acc_bal,String pass,String pin,String date_time)
    {
        try {
            String sql = "insert into bankdb values(?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            pst.setString(2, fname);
            pst.setString(3, lname);
            pst.setString(4, gender);
            pst.setString(5, mob);
            pst.setInt(6, age);
            pst.setDouble(7, acc_bal);
            pst.setString(8, pass);
            pst.setString(9, pin);
            pst.setString(10, date_time);
            int res = pst.executeUpdate();
            pst.close();
            if(res!=0)
                System.out.println("Account opened successfully!");
            else
                System.out.print("Some error occured");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void showallrec()
    {
        try {
            st = con.createStatement();
            rSet = st.executeQuery("select * from bankdb");
            while (rSet.next()) {
            System.out.print("\n-----------------------------------------------------------------\n");
            System.out.print("Account Number: "+rSet.getString(1)+"\nName: "+rSet.getString(2)+" "+rSet.getString(3)+"\nGender: "+rSet.getString(4)+"\nMobile Number: "+rSet.getString(5)+"\nAge: "+rSet.getInt(6)+"\nAccount Balance: "+rSet.getDouble(7)+"\nAccount Opening Date: "+rSet.getString(10));
            System.out.print("\n-----------------------------------------------------------------\n");
            }
            st.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public boolean findUserDetails(String acno,String pass)
    {
        try {
            String sql = "select * from bankdb where acc_num = ? AND pass = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            pst.setString(2, pass);
            rSet = pst.executeQuery();
            if(rSet.next())
            {
                return true;
            }    
            else
                System.out.print("Incorrect Account Number or Password");
        } catch (Exception e) {
            System.out.print(e);
        }
        return false;
    }

    public void delete(String acno)
    {
       String sql = "delete from bankdb where acc_num = ?";
        try {
        pst = con.prepareStatement(sql);
        pst.setString(1, acno);
        int res = pst.executeUpdate();
        pst.close();
        if(res!=0)
            System.out.print("Account Deleted");
        else
            System.out.print("Account Number Not Found!");
       } catch (Exception e) {
        System.out.print(e);
       }

    }

    public void deposit(double amount,String acno)
    {
        String sql="update bankdb set account_bal = account_bal + ? where acc_num = ? ";
        try {
            pst = con.prepareStatement(sql);
            pst.setDouble(1, amount);
            pst.setString(2, acno);
            int res = pst.executeUpdate();
            pst.close();
            if(res!=0)
                System.out.println("Amount deposited successfully!");
            else
                System.out.print("Some error Occured");
        } catch (Exception e) {
            System.out.print(e);
        }
        
    }

    public void withdraw(double amount,String acno)
    {
        double temp = current_amount(acno);
        String sql="update bankdb set account_bal = account_bal - ? where acc_num = ?";
        if (amount<=temp) {
            try {
                pst = con.prepareStatement(sql);
                pst.setDouble(1, amount);
                pst.setString(2, acno);
                int res = pst.executeUpdate();
                pst.close();
                if(res!=0)
                    System.out.println("Amount withdrawed successfully!");
                else
                    System.out.print("Some error Occured");
            } catch (Exception e) {
            System.out.print(e);
            }
        }
        else
            System.out.print("Insufficient funds!!");
    }

    public Double current_amount(String acno)
    {
        double cur_amt;
        String sql = "select * from bankdb where acc_num=?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            rSet = pst.executeQuery();
            if(rSet.next())
            {
                cur_amt = rSet.getDouble(7);
                pst.close();
                return cur_amt;
            }
        } catch (Exception e) {
            System.out.print(e);
        }

        return null;
    }

    public void searchByAcc(String acno)
    {
        String sql = "select * from bankdb where acc_num = ? ";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            rSet = pst.executeQuery();
            if (rSet.next()) {
                System.out.print("\n-----------------------------------------------------------------\n");
                System.out.print("Account Number: "+rSet.getString(1)+"\nName: "+rSet.getString(2)+" "+rSet.getString(3)+"\nGender: "+rSet.getString(4)+"\nMobile Number: "+rSet.getString(5)+"\nAge: "+rSet.getInt(6)+"\nAccount Balance: "+rSet.getDouble(7)+"\nAccount Opening Date: "+rSet.getString(10));
                System.out.print("\n-----------------------------------------------------------------\n");
            }
            else
                System.out.print("Account Not Found!");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void searchByFname(String fname)
    {
        String sql = "select * from bankdb where fname like ?";
        try {
            pst = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, fname + "%");
            rSet = pst.executeQuery();
            if (Boolean.compare(rSet.next(), true)==-1) {
                System.out.print("Account Not Found!");
            }
            else{
                rSet.previous();
                while (rSet.next()) {
                    System.out.print("\n-----------------------------------------------------------------\n");
                    System.out.print("Account Number: "+rSet.getString(1)+"\nName: "+rSet.getString(2)+" "+rSet.getString(3)+"\nGender: "+rSet.getString(4)+"\nMobile Number: "+rSet.getString(5)+"\nAge: "+rSet.getInt(6)+"\nAccount Balance: "+rSet.getDouble(7)+"\nAccount Opening Date: "+rSet.getString(10));
                    System.out.print("\n-----------------------------------------------------------------\n");
                }
            }
            pst.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void searchByLname(String lname)
    {
        String sql = "select * from bankdb where lname like ?";
        try {
            pst = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            pst.setString(1, lname + "%");
            rSet = pst.executeQuery();
            if (Boolean.compare(rSet.next(), true)==-1) {
                System.out.print("Account Not Found!");
            }
            else{
                rSet.previous();
                while (rSet.next()) {
                    System.out.print("\n-----------------------------------------------------------------\n");
                    System.out.print("Account Number: "+rSet.getString(1)+"\nName: "+rSet.getString(2)+" "+rSet.getString(3)+"\nGender: "+rSet.getString(4)+"\nMobile Number: "+rSet.getString(5)+"\nAge: "+rSet.getInt(6)+"\nAccount Balance: "+rSet.getDouble(7)+"\nAccount Opening Date: "+rSet.getString(10));
                    System.out.print("\n-----------------------------------------------------------------\n");
                }
            }
            pst.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public String account_num()
    {
        String account_num;
        String sql = "select * from bankdb order by acc_num desc limit 1";
        try {
            st = con.createStatement();
            rSet = st.executeQuery(sql);
            if (rSet.next()) {
                account_num = rSet.getString(1);
                return account_num;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return null;
    }

    public boolean pinCheck(String acno,String pin)
    {
        String sql = "select * from bankdb where acc_num = ? and security_pin = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            pst.setString(2, pin);
            rSet = pst.executeQuery();
            if(rSet.next())
            {
                pst.close();
                return true;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return false;
    }

    public int size()
    {
        int size;
        String sql = "select count(acc_num) as total  from bankdb";
        try {
            st = con.createStatement();
            rSet = st.executeQuery(sql);
            if (rSet.next()) {
                size = rSet.getInt("total");
                st.close();
                return size;
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return 0;
    }
    
    public void close()
    {
        try {
            con.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}

class dateTime{
    public String DateTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime time1 = LocalDateTime.now();
        String time = dtf.format(time1);
        return time;
    }
}

class transaction{

    //Transaction table

    Connection con = null;
    PreparedStatement pst;
    Statement st;
    ResultSet rSet;
    transaction()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");

        } catch (Exception e) {
            System.out.print(e);
        }
    }

     public void InsertTransaction(String acno,double credit,double debit,String date_time,double current_amount)
    {
        String sql = "insert into transaction values(?,?,?,?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            pst.setDouble(2, credit);
            pst.setDouble(3, debit);
            pst.setString(4, date_time);
            pst.setDouble(5, current_amount);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void ShowTransaction(String acno)
    {
        String sql = "select * from transaction where acc_num = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            rSet = pst.executeQuery();
            System.out.print("\n\t\t---------------------------------------------------------------------------------");
            System.out.print("\n\t\t|\tCredit\t|\tDebit\t|\tDate and Time\t\t|Current Amount\t|");
            System.out.print("\n\t\t---------------------------------------------------------------------------------");
            while (rSet.next()) {
                System.out.print("\n\t\t|\t"+rSet.getDouble(2)+"\t|\t"+rSet.getDouble(3)+"\t|\t"+rSet.getString(4)+"\t|\t"+rSet.getDouble(5)+"\t|");
            }
            System.out.print("\n\t\t---------------------------------------------------------------------------------");
            pst.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void DeleteTransaction(String acno)
    {
        String sql = "delete from transaction where acc_num = ?";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, acno);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void close()
    {
        try {
            con.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }
}

class clear{
    public  static void clearConsole()
    {
        System.out.print("\033[H\033[2J");   
        System.out.flush();
    }
}

class Menu extends clear {
    Scanner sc = new Scanner(System.in);
    Admin ad = new Admin();
    dateTime t = new dateTime();
    function f = new function();
    transaction tr = new transaction();
        public  void mainMenu()
        {   
        int acno;
        if (f.account_num()!=null) {
            acno = Integer.parseInt(f.account_num())+1;
        }
        else
            acno = 10001;
        while (true) 
        {   
            System.out.print("\n\t\t<-------------- Welcome to JAVA BANK ---------------->\n");
            System.out.println("\n1. Open new account");
            System.out.println("2. User Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice)
             {
                case 1:
                    clearConsole();
                    System.out.print("\n\n\t\t<------------   Registration Form  ------------->\n\n");
                    System.out.print("Enter first name: ");
                    String fname=sc.next();
                    fname=fname.toUpperCase();
                    System.out.print("Enter last name: ");
                    String lname=sc.next();
                    lname=lname.toUpperCase();
                    System.out.print("Enter gender: ");
                    String gender=sc.next();
                    gender=gender.toUpperCase();
                    System.out.print("Enter mobile number: ");
                    String mob=sc.next();
                    System.out.print("Enter age: ");
                    int age=sc.nextInt();
                    System.out.print("Minimum opening balance should be 500 INR\nEnter opening balance: ");
                    double opening_bal=sc.nextDouble();
                    if((gender.equals("MALE"))||(gender.equals("M"))||(gender.equals("FEMALE"))||(gender.equals("F")))
                    {
                        if(mob.length()==10)
                        {
                            if((opening_bal>=500)&&(age>=18))
                            {
                                System.out.print("Create Password: ");
                                String pass = sc.next();
                                System.out.print("Create Four Digit Security Pin: ");
                                String pin = sc.next();
                                if(pin.length()==4)
                                {
                                    String accnum = String.valueOf(acno);
                                    f.insert(accnum, fname, lname, gender, mob, age, opening_bal, pass,pin,t.DateTime());
                                    System.out.println("\nYour Account number: "+acno);
                                    acno++;
                                }
                                else
                                    System.out.print("Security pin must contain 4 digits only");

                            }
                            else
                            {
                                System.out.print("Minimum opening balance should be 500 INR or Your age should be 18 or greater");
                            }
                        }
                        else
                        {
                            System.out.print("Mobile number should have 10 digits");
                        }
                    }
                    else
                    {
                        System.out.print("Enter gender Male/Female or M/F");
                    }
                    break;

                case 2:
                    clearConsole();
                    System.out.print("\n\t\t<-------------- Welcome to JAVA BANK ---------------->");
                    System.out.print("\n\n\t\t<----------------- Login page----------------->\n\n");
                    System.out.print("Enter Account number: ");
                    String tacno=sc.next();
                    System.out.print("Enter password: ");
                    String tpass=sc.next();
                    if (f.findUserDetails(tacno, tpass)) 
                    {
                       System.out.print("\nLogin successfully");
                       if((f.getGender(tacno).equals("MALE"))||(f.getGender(tacno).equals("M")))
                       {
                            System.out.print("\n\nWelcome Mr. "+f.getname(tacno)); 
                       }
                       else
                       {
                            System.out.print("\n\nWelcome Ms./Mrs. "+f.getname(tacno)); 
                       }
                       checklogin(tacno);

                       
                    } 
                    else
                    {
                        System.out.println("\nAccount not found!");
                    }            
                    break;
                case 3: 
                //Admin Menu
                    clearConsole();
                    String user_name,user_pass;
                    System.out.print("Enter Admin Username: ");
                    user_name=sc.next();
                    System.out.print("Enter Admin Password: ");
                    user_pass=sc.next();
                    if((user_name.equals(ad.getadmin_username()))&&(user_pass.equals(ad.getadmin_pass())))
                    {  
                       
                        while(true)
                       {
                            System.out.print("\n\t\tAdmin Menu");
                            System.out.print("\n\t<-------------------------->\n");
                            System.out.println("\n1. Show All Records");
                            System.out.println("2. Search Account");
                            System.out.println("3. Delete Account");
                            System.out.println("4. Exit");
                            System.out.print("Enter your choice: ");
                            int option = sc.nextInt();
                            if (option<=4) {
                                if(option==1){
                                    clearConsole();
                                    System.out.print("Records: "+f.size());
                                    f.showallrec();
                                }
                            
                                else if(option==2){
                                    clearConsole();
                                    AdminSearch();
                                }

                                else if(option==3){
                                    clearConsole();
                                    System.out.print("\nEnter account number: ");
                                    String temp = sc.next();
                                    f.delete(temp);
                                    tr.DeleteTransaction(temp);
                                }
                                else if(option==4){ 
                                    clearConsole();
                                    break;   
                                }   
                            }
                            else
                            {
                                System.out.print("Entered Wrong Choice");
                            }

                       }
                       
                    }
                    else
                    {
                        System.out.print("\nWrong Admin Username or Admin Password");
                    }
                    //Admin Menu Closed
                    break;

                case 4:
                    System.out.println("Exiting the banking system.");
                    sc.close();
                    f.close();
                    tr.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public  void checklogin(String acno)
    { 
        String pin;
        while (true)
        {
            System.out.println("\n\n1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Transaction History");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            int option = sc.nextInt();
            sc.nextLine(); 

            switch (option) 
            {
                case 1:
                    clearConsole();
                    System.out.print("\n\t\t<-------------- Welcome to JAVA BANK ---------------->");
                    System.out.print("\n\t\t<--------For depositng the money fill the following details-------->\n");
                    System.out.print("Enter deposit amount: ");
                    double depositAmount = sc.nextDouble();
                    System.out.print("Enter four digit Security Pin: ");
                    pin = sc.next();
                    if (f.pinCheck(acno, pin)) {
                        f.deposit(depositAmount, acno);
                        tr.InsertTransaction(acno, depositAmount, 0, t.DateTime(), f.current_amount(acno));
                        }
                    else
                        System.out.print("Invalid Pin!");
                    
                    break;

                case 2:
                    clearConsole();
                     System.out.print("\n\t\t<-------------- Welcome to JAVA BANK ---------------->"); 
                     System.out.print("\n\t\t<--------For withdrawing the money fill the following details-------->\n");
                     System.out.print("Enter withdrawal amount: ");
                     double withdrawAmount = sc.nextDouble();
                     System.out.print("Enter four digit Security Pin: ");
                     pin = sc.next();
                     if (f.pinCheck(acno, pin)) {
                        f.withdraw(withdrawAmount, acno);
                        tr.InsertTransaction(acno,0,withdrawAmount, t.DateTime(), f.current_amount(acno));  
                     }
                     else
                        System.out.print("Invalid Pin!");
                     
                    break;

                case 3:
                     clearConsole();
                     System.out.print("\n\t\t<-------------- Welcome to JAVA BANK ---------------->");
                     System.out.print("\n\t\t<--------For checking the balance fill the following details-------->\n");
                     System.out.print("Enter four digit Security Pin: ");
                     pin = sc.next();
                     if (f.pinCheck(acno, pin)) {
                        double amount = f.current_amount(acno);
                        System.out.println("Account Holder: " +f.getname(acno));
                        System.out.println("Balance: " + amount);
                     }
                     else
                        System.out.print("Invalid Pin!");
                    break;

                case 4: 
                     clearConsole();
                      System.out.print("\n\t\t<-------------- Welcome to JAVA BANK ---------------->\n");
                      System.out.print("Enter four digit Security Pin: ");
                      pin = sc.next();
                      if (f.pinCheck(acno, pin)) {
                        System.out.println("Account Holder: " +f.getname(acno));
                        System.out.println("Account Number: " +acno);
                        System.out.print("\n\t\t\t\t<------------- Transaction History ------------------->\n");
                        tr.ShowTransaction(acno);
                      }
                      else
                        System.out.print("Invalid Pin!");
                    break;
                    case 5:
                    System.out.println("Exiting the login system.");
                    clearConsole();
                    return;
                    
                 default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }

    }

    public void AdminSearch()
    {
        while (true) {
            System.out.print("\n\t\tAdmin Searching System");
            System.out.print("\n\t<---------------------------------------->\n");
            System.out.println("\n\n1. Search By Account");
            System.out.println("2. Search By First Name");
            System.out.println("3. Search By Last Name");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            int option = sc.nextInt();
            if (option==1) {
                clearConsole();
                System.out.print("\nEnter Account Number: ");
                String tempacno = sc.next();
                f.searchByAcc(tempacno);
            }
            else if (option==2) {
                clearConsole();
                System.out.print("\nEnter Your Initials: ");
                String tempfname = sc.next();
                tempfname = tempfname.toUpperCase();
                f.searchByFname(tempfname);
            }
            else if (option==3) {
                clearConsole();
                System.out.print("\nEnter Your Initials: ");
                String templname = sc.next();
                templname = templname.toUpperCase();
                f.searchByLname(templname);
            }
            else if (option==4) {
                clearConsole();
                break;
            }
            else
                System.out.print("Invalid Choice");
        }
    }

}

class Admin{ 
    private static String admin_username="ADMIN";
    private static String admin_pass="@Admin2600";  
    public String getadmin_username()
    {
        return admin_username;
    }

    public String getadmin_pass()
    {
        return admin_pass;
    }    
}

class BankSys
{
    public static void main(String[] args) {
        Menu m = new Menu();
        m.mainMenu();
    }
}