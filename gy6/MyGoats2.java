
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MyGoats2 {
	public static void main(String[] args) throws Exception {
		new GoatGame(10).start();
	}
}

class GoatGame extends Thread {
	int boardLength;
	Goat[] goats = new Goat[2];
	Optional<Integer> champion = Optional.empty();

	public GoatGame(int boardLength) {
		this.boardLength = boardLength;
	}

	public void run() {
		try {
			goats[0] = new Goat(this, boardLength / 2, +1);
			goats[1] = new Goat(this, boardLength / 2 + 1, -1);

			goats[0].start();
			goats[1].start();

			goats[0].join();
			goats[1].join();

			System.out.println("Champion: " + champion.get());
		} catch (Exception e) {
			// won't happen
		}
	}
}

class Goat extends Thread {
	GoatGame game;
	// Goat adatai
	int pos;
	int hitStrength;

	public Goat(GoatGame game, int initPos, int hitStrength) {
		this.game = game;
		this.pos = initPos;
		this.hitStrength = hitStrength;
	}

	public void run() {
		// Goat működése
		while (!isDone()) {
			waitBeforeHit();
			if (isDone())  break;
			hit();
		}

		System.out.println("finished: " + hitStrength);
	}

	boolean isDone() {
		synchronized (MyGoats.class) {
			return isInvalid() || getOtherGoat().isInvalid();
		}
	}

	boolean isInvalid() {
		return 0 > pos || pos > game.boardLength;
	}

	void hit() {
		synchronized (MyGoats.class) {
			pos += hitStrength;
			getOtherGoat().pos += hitStrength;
		}

		System.out.printf("goat %d hit: %d %d%n", hitStrength, pos, getOtherGoat().pos);
	}

	Goat getOtherGoat() {
		return game.goats[game.goats[0] == this ? 1 : 0];

	}

	void waitBeforeHit() {
		int waitMsec = ThreadLocalRandom.current().nextInt(1500) + 500;
		try {
			// a "wait" más!
			// Thread.sleep(waitMsec);
			TimeUnit.MILLISECONDS.sleep(waitMsec);
		} catch (Exception e) {
			// won't happen
		}
	}
}
