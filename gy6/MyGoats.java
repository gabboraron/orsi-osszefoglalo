
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class MyGoats {
	public static void main(String[] args) throws Exception {
		int len = 5;

		// v1
		int[] goatPos = {
			len / 2,   		// goat 1
			len / 2 + 1		// goat 2
		};

		// v2
		// int commonGoatPos = ...;

		Thread goat1 = startGoat(goatPos, len, 0, +1);
		Thread goat2 = startGoat(goatPos, len, 1, -1);
		// Thread goat2 = startGoat(goatPos, len, 1, -3);

		goat1.join();
		goat2.join();

		System.out.println("result: " + Arrays.toString(goatPos));
	}

	static Thread startGoat(int[] goatPos, int len, int idx, int hitStrength) {
		Thread thread = new Thread(() -> {
			while (!isDone(goatPos, len)) {
				waitBeforeHit();
				if (isDone(goatPos, len))  break;
				hit(goatPos, idx, hitStrength);
			}

			System.out.println("finished: " + idx);
		});

		thread.start();

		return thread;
	}

	static boolean isDone(int[] goatPos, int len) {
		synchronized (MyGoats.class) {
			return isInvalid(goatPos[0], len) || isInvalid(goatPos[1], len);
		}
	}

	static boolean isInvalid(int goatPos, int len) {
		return 0 > goatPos || goatPos > len;
	}

	static void hit(int[] goatPos, int idx, int hitStrength) {
		int otherIdx = 1 - idx;

		synchronized (MyGoats.class) {
			goatPos[idx]      += hitStrength;
			goatPos[otherIdx] += hitStrength;
		}

		System.out.printf("goat %d hit: %s%n", idx, Arrays.toString(goatPos));
	}

	static void waitBeforeHit() {
		// new Random();
		int waitMsec = ThreadLocalRandom.current().nextInt(1500) + 500;

		try {
			// a "wait" más!
			Thread.sleep(waitMsec);
		} catch (Exception e) {
			// won't happen
		}
	}

}
