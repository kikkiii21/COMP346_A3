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
	public enum State {THINKING, HUNGRY, EATING}
	public State[] states;
	public int nbChopsticks;
	public Boolean somebodyTalking;
	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		states = new State[piNumberOfPhilosophers];
		nbChopsticks = piNumberOfPhilosophers;
		somebodyTalking = false;
		for(int i=0; i<piNumberOfPhilosophers; i++){
			states[i] = State.THINKING;
		}
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
		int index = piTID-1;
		states[index] = State.HUNGRY;
		testChopsticks(index);
		while(states[index]!=State.EATING) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopsticks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		int index = piTID-1;
		states[index] = State.THINKING;
		testChopsticks((index+nbChopsticks-1)%nbChopsticks);
		testChopsticks((index+1)%nbChopsticks);
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		while(somebodyTalking){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Current philosopher thread talking
		somebodyTalking = true;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		somebodyTalking = false;
		notifyAll();
	}
	public synchronized void testChopsticks(int index)
	{
		if(states[((index+nbChopsticks-1)%nbChopsticks)]!=State.EATING && states[((index+1)%nbChopsticks)]!=State.EATING &&
			states[index]==State.HUNGRY){
			states[index] = State.EATING;
			notifyAll();
		}
	}
}

// EOF
