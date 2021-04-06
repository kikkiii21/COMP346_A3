import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	Boolean someoneTalking =false;
	int numOfPhilosopher=0;

	//conditions
//	Lock lock = new ReentrantLock();
//	Condition[] self;
	//methods, see below

	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		// same amount of chopsticks as philosophers

		state = new States[piNumberOfPhilosophers];
		//self = new Condition[piNumberOfPhilosophers];
		numOfPhilosopher = piNumberOfPhilosophers;
		//initialize chopsticks to true as they are all available at the start of dinner
		for(int i =0;i<piNumberOfPhilosophers;i++){
			state[i] = States.THINKING;
			//self[i] =lock.newCondition();
		}

		System.out.println(Arrays.toString(state));
		//System.out.println(Arrays.toString(self));

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
		// ...
		//separate picking up of the chopsticks based on even/odd philosopher? For example even philo picks
		// up the left chopstick first and then the right and vice versa for the odd philo

		int id = piTID-1;
		state[id] = States.HANGRY;
		testChopsticks(piTID);
		try {
			if (state[id] != States.EATING) wait();
		}catch(InterruptedException e){
			System.err.println("Philosopher Pickup error");
			DiningPhilosophers.reportException(e);
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopsticks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		// ...
		int id = piTID-1;
		state[id] = States.THINKING;
		testChopsticks((id+(numOfPhilosopher-1)) % numOfPhilosopher);
		testChopsticks(id+1 % numOfPhilosopher);

	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		// ...
//		while(someoneTalking) {
//			try {
//					this.wait();
//			} catch (InterruptedException e) {
//				System.err.println("Someone talking error");
//				DiningPhilosophers.reportException(e);
//			}
//		}
//		someoneTalking =true;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		// ...
//		someoneTalking = false;
//		this.notifyAll();
	}

	public synchronized void testChopsticks(int piTID){

		int id = piTID-1;
		if( state[ ((id-1+numOfPhilosopher) % numOfPhilosopher)] != States.EATING && state[((id+1) % numOfPhilosopher)] != States.EATING && state[id] == States.HANGRY ){
			state[id] = States.EATING;
			notifyAll();
		}


	}

	public synchronized void testTalking(int piTID){

	}

}

// EOF
