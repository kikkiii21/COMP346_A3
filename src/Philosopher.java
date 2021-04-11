//import common.BaseThread;

import java.util.Arrays;

/**
 * Class Philosopher.
 * Outlines main subroutines of our virtual philosopher.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 */
public class Philosopher extends BaseThread
{
	/**
	 * Max time an action can take (in milliseconds)
	 */
	public static final long TIME_TO_WASTE = 1000;

	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			// indicates what the philosopher is currently doing and corresponds to its state status
			System.out.println("Philosopher #"+getTID()+" has started eating");
			Thread.yield();
			sleep((long)(Math.random() * TIME_TO_WASTE));
			Thread.yield();
			// message done eating
			System.out.println("Philosopher #"+ getTID()+" has finished eating");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - yield
	 * - Then sleep() for a random interval.
	 * - yield
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		// indicates what the philosopher is currently doing and corresponds to its state status
		try{
		System.out.println("Philosopher #"+getTID()+" has started thinking");
		Thread.yield();
		sleep((long)(Math.random() * TIME_TO_WASTE));
		Thread.yield();
		System.out.println("Philosopher #"+getTID()+" has finished thinking");
		}catch(InterruptedException e){
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}

	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - yield
	 * - Say something brilliant at random
	 * - yield
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		// indicates what the philosopher is currently doing
		System.out.println("Philosopher #"+getTID()+" has started talking");
		Thread.yield();
		saySomething();
		Thread.yield();
		System.out.println("Philosopher #"+getTID()+" is done talking");


	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			// Synchronizes the pickup() and putDown() methods to ensure that no two adjacent philosophers can
			// enter the eat() method at the same time since there is a lack of chopsticks
			DiningPhilosophers.soMonitor.pickUp(getTID());
			eat();
			DiningPhilosophers.soMonitor.putDown(getTID());

			// no need to synchronize this method as there are no restrictions or conditions on who is allowed to access think()
			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */

			// The decision is randomly made by choosing at random either True or False
			Boolean[] randomValue = {true,false};
			Boolean willTalk = randomValue[(int)(Math.random() * randomValue.length)];

			if(willTalk) {
				//Checks if another philosopher is talking; if not will talk() otherwise will wait
				//synchronizes the requestTalk() and endTalk() methods to ensure that only one philosopher can talk at a time
				DiningPhilosophers.soMonitor.requestTalk();
				talk();
				DiningPhilosophers.soMonitor.endTalk();
			}
			Thread.yield();

		}
	} // run()

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	public void saySomething()
	{
		String[] astrPhrases =
		{
			"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
			"You know, true is false and false is true if you think of it",
			"2 + 2 = 5 for extremely large values of 2...",
			"If thee cannot speak, thee must be silent",
			"My number is " + getTID() + "",
			"ARTHUUUUUUUUUUUUUR"
		};

		System.out.println
		(
			"Philosopher " + getTID() + " says: " +
			astrPhrases[(int)(Math.random() * astrPhrases.length)]
		);
	}
}

// EOF
