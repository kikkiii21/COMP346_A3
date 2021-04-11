import java.util.Arrays;


/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	//attributes
	enum States{EATING,THINKING,HANGRY};
	States[] state;
	static Boolean talkingTurn;
	int numOfChopsticks =0;


	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		// same amount of chopsticks as philosophers
		numOfChopsticks = piNumberOfPhilosophers;
		// using the state of a philosopher to determine if a particular philosopher can eat based on the state of its neighbors.
		state = new States[piNumberOfPhilosophers];
		for(int i =0;i<piNumberOfPhilosophers;i++){
			state[i] = States.THINKING;
		}
		// used to determine if a philosopher is currently talking and if it needs to wait for his turn
		talkingTurn =true;
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		// Philosopher #1 is at index 0 in the state array therefore we identify the correct person with the id variable.
		// The testChopsticks method checks the state values of the neighbors of the philosopher in question. If both neighbors
		// are not in the EATING state, then the philosopher's state will change to EATING and will enter the eat method. Otherwise he will wait.

		int id = piTID-1;
		state[id] = States.HANGRY;
		testChopsticks(id);
		while (state[id] != States.EATING) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("Philosopher Pickup error");
				DiningPhilosophers.reportException(e);
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopsticks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// Philosopher #1 is at index 0 in the state array therefore we identify the correct person with the id variable.
		// Once a philosopher is done with the eat method, its state is updated to THINKING
		// TestChopsticks() method is used to notify the left and right neighbors if any are waiting
		int id = piTID-1;
		state[id] = States.THINKING;
		testChopsticks((id+numOfChopsticks -1)% numOfChopsticks);
		testChopsticks((id+1)% numOfChopsticks);

	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// The first philosopher entering this method will be allowed to access the talk method as the talkingTurn boolean is True.
		// Any subsequent philosopher will have to wait for the talkingTurn boolean to become True in the endTalk() method.

		while(!talkingTurn) {
			try {
					wait();

			} catch (InterruptedException e) {
				System.err.println("Someone talking error");
				DiningPhilosophers.reportException(e);
			}
		}
		talkingTurn =false;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// Makes the talkingTurn boolean True in order for a philosopher that is waiting can enter the talk() method.
		talkingTurn = true;
		notifyAll();
	}

	public synchronized void testChopsticks(int id)
	{
		// Checks the state status of the left and right neighbor of a philosopher and determines if the philosopher in question can eat.
		// 3 conditions needs to be met in order for a philosopher to enter the eat() method.
		// The state Eating implies that the neighbor is eating therefore the chopstick is not available.
		// The philosopher in question needs to be in the state HUNGRY, the left and right chopsticks are available, meaning left and right neighbor are not in the EATING status

		if( state[ ((id+ numOfChopsticks -1) % numOfChopsticks)] != States.EATING && state[((id+1) % numOfChopsticks)] != States.EATING && state[id] == States.HANGRY ){
			state[id] = States.EATING;
			notifyAll();
		}
	}
}

// EOF
