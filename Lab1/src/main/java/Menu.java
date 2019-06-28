import java.util.Scanner;

public class Menu {
    Authenticator ac;
    public void menu() {

    }

    public void showAuthenticatorMenu(){
        int opc;
        Scanner sc = new Scanner(System.in);
        System.out.println("Authenticator Menu");
        System.out.println("Do you want to:");
        System.out.println("1.Login");
        System.out.println("2.Logout");
        opc=sc.nextInt();
        switch (opc){
            case 1:

                break;
            case 2:

                break;
            default:
                System.out.println("Set a correct value: ");
                break;
        }
    }
    public void showConfigurationMenu(){
        Configuration con=new Configuration();
        Scanner sc = new Scanner(System.in);
        String ans="";
        int opc=0;
        System.out.println("Configuration Menu");
        System.out.println("1.Spam probability");
        System.out.println("2.Spam Threshold probability");
        System.out.println("3.Training Size");
        opc =sc.nextInt();

        switch (opc){
            case 1:
                System.out.println("The default spam probability value is 3.0");
                System.out.println("Do you want to change it? Y/N");
                ans =sc.nextLine();
                //
                //
                if(ans == "Y"|| ans =="y") {
                    double prob;
                    System.out.println("Set the new probability: ");
                    prob=sc.nextDouble();
                    con.setSpamProbability(prob);
                }
                break;
            case 2:
                System.out.println("The default threshold probability value is 9.0");
                System.out.println("Do you want to change it? Y/N");
                ans =sc.nextLine();
                //
                //
                if(ans == "Y"|| ans =="y") {
                    double prob;
                    System.out.println("Set the new probability: ");
                    prob=sc.nextDouble();
                    con.setspamThreshold(prob);
                }

                break;
            case 3:
                System.out.println("Set the new Training Size (interger):");
                int size = sc.nextInt();
                con.setTrainingSize(size);
                break;
            default:
                System.out.println("Set a correct value: ");
                break;
        }

    }
    public void showFilterMenu(){

    }
}
