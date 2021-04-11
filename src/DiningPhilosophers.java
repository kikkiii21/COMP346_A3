import java.util.InputMismatchException;
import java.util.Scanner;
/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	public static int iPhilosophers;
	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;
	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;
	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		/*
		 * TODO:
		 * Should be settable from the command line
		 * or the default if no arguments supplied.
		 */
		// The user chooses the number of philosophers wanted for the dinning philosopher problem. the default value of 4 is used if the input
		// is not a positive integer or not an integer at all
		iPhilosophers=0;
		int numOfPhilo;
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the number of Philosophers: ");
		String input = scanner.nextLine();
		try
		{

			try {
				numOfPhilo = Integer.parseInt(input);
				if (numOfPhilo <= 0) {
					System.out.println("This is not a positive integer:" + input + "\n will use the default number of 4");
					iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
				} else
					iPhilosophers = numOfPhilo;
			}catch (NumberFormatException e){
				System.out.println("The default value will be used as the entered input is not in the proper number format. value entered:"+input);
				iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
			}


			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher[] aoPhilosophers = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
