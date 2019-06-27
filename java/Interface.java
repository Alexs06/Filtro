import jdk.management.resource.internal.inst.DatagramDispatcherRMHooks;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Clase que despliega el menú del usuario
 */
public class Interface {

	private Controller controller;
	private BayesianFilter bf;

	/**
	 * Ejecuta el método controlador
	 * @param control
	 */
	public Interface(Controller control) {
		controller = control;
		bf = new BayesianFilter();
	}

	/**
	 * Recive los datos y opciones del usuario
	 * @throws IOException Excepción de java
	 */
	public void menu() throws IOException, MessagingException {
	    boolean nosabemoshacermenus = false;
		System.out.println("Welcome to Bayesian Filter By Jimmy \"Elputo\" Acuña & Luis \"Elpro\" Carvajal, please select an option.");
		System.out.print(" Option 1: Authenticate yourself \n Option 2: Exit \n");
		Scanner opt = new Scanner(System.in);
		int option = opt.nextInt();
		while (!nosabemoshacermenus) {
			switch (option) {
				case 1:
					//el usuario debe logearse primero
					System.out.println("Type your User name please:");
					Scanner usr = new Scanner(System.in);
					String user = usr.nextLine();
					bf.setUsrName(user);
					controller.logIn(user);
					System.out.println("Please select an option:");
					System.out.print(" Option 1: Configure \n Option 2: Train \n Option 3: Show Data \n Option 4: Get new mail \n Option 5: Log Out \n Option 6: Exit \n");
					Scanner op = new Scanner(System.in);
					int option2 = op.nextInt();
                        switch (option2) {

                                case 1:
                                    boolean nosabemoshacermenus2 = false;
                                    while(!nosabemoshacermenus2) {
                                    System.out.println("Please enter the spam probability");
                                    Scanner prob = new Scanner(System.in);
                                    float spamProb = prob.nextFloat();

                                    System.out.println("Please enter the spam threshold");
                                    Scanner thres = new Scanner(System.in);
                                    float trhesholdProb = prob.nextFloat();

                                    System.out.println("Please enter size of the training set");
                                    Scanner size = new Scanner(System.in);
                                    int trainSet = prob.nextInt();
                                    controller.configuration(spamProb, trhesholdProb, trainSet);
                                    nosabemoshacermenus2 = true;
                                    break;
                            }
                            case 2:
                                controller.train();
                                break;

                            case 3:
                                controller.showData();
                                System.out.println("Is at C:\\Users\\<user>\\Documents\\BayesianFilter\\Users" + user);
                                break;

                            case 4:
                                controller.getMail();
                                break;

                            case 5:
                                controller.logOut();
                                break;

                            case 6:
                                System.exit(0);
                                break;

                            default:
                                System.out.println("Please select a valid option.");
                                break;
                        }
				case 2:
					System.exit(1);
					nosabemoshacermenus = true;
					break;

				default:
					System.out.println("Please select a valid option.");
					break;
			}
		}
	}

}
