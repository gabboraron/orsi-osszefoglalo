
public class Parhuzamossag
{
	public static void main(String[] args) throws Exception {
		MyClass myClass = new MyClass();

		Thread t1 = new Thread(() -> {
			myClass.myFun1();
			myClass.myFun2();
		});
		Thread t2 = new Thread(() -> {
			myClass.myFun1();
			myClass.myFun2();
		});

		t1.start();		// versenyhelyzet (race condition)
		t2.start();
		t1.join();
		t2.join();

		System.out.println(myClass.myData1);
		System.out.println(myClass.myData2);
	}

	public static void main4(String[] args) throws Exception {
		new Thread(() -> {
			for (int i = 0; i < 3000; ++i) {
				synchronized (System.out) {
					// System.out.println("Hello");
					for (char c : "Hello".toCharArray()) {
						System.out.print(c);
					}
					System.out.println();
				}
			}
		}).start();

		new Thread(() -> {
			for (int i = 0; i < 3000; ++i) {
				synchronized (System.out) {
					// System.out.println("vilag");
					for (char c : "vilag".toCharArray()) {
						System.out.print(c);
					}
					System.out.println();
				}
			}
		}).start();
	}

	public static void main3a(String[] args) throws Exception {
		Object lock = new Object();
		int[] counter = { 0 };
		String txt = "abc";

		Thread t1 = new Thread(() -> {
			for (int i = 0; i < 100000; ++i) {
				// monitor; lock; kölcsönös kizárás
				synchronized (counter) {
					++counter[0];
				}
			}
		});

		Thread t2 = new Thread(() -> {
			for (int i = 0; i < 100000; ++i) {
				synchronized (counter) {
				// synchronized (txt) {
				// synchronized (lock) {
				// synchronized (Parhuzamossag.class) {
				// synchronized (System.out) {
					++counter[0];
				}
			}
		});

		// t1.run();
		// t2.run();
		t1.start();		// versenyhelyzet (race condition)
		t2.start();
		t1.join();
		t2.join();

		System.out.println(counter[0]);
	}

	// public static void main(String[] args) throws Exception {
	//	// final int counter = 0;
	//	int counter = 0;		// nem tud (effectively) final lenni

	//	Thread t1 = new Thread(() -> {
	//		for (int i = 0; i < 100000; ++i) {
	//			++counter;
	//		}
	//	});

	//	Thread t2 = new Thread(() -> {
	//		for (int i = 0; i < 100000; ++i) {
	//			++counter;
	//		}

	//	});
	//	t1.start();
	//	t2.start();
	//	t1.join();
	//	t2.join();

	//	System.out.println(counter);
	// }

	public static void main2(String[] args) throws Exception {
		// névtelen függvény
		Runnable r = () -> {
			for (int i = 0; i < 3000; ++i) {
				System.out.println("Hello vilag " + i);
			}
		};

		// NEM párhuzamos
		// r.run();
		// r.run();
		// r.run();
		// r.run();

		new Thread(r);

		// new Thread(r).run();
		// new Thread(r).run();
		// new Thread(r).run();
		// new Thread(r).run();

		// new Thread(r).start();
		// new Thread(r).start();
		// new Thread(r).start();
		// new Thread(r).start();

		// new Thread(() -> {
		//	for (int i = 0; i < 3000; ++i) {
		//		System.out.println("Hello vilag " + i);
		//	}
		// }).start();

		// new Thread(szovegesSzal("a", 3000)).start();
		// new Thread(szovegesSzal("b", 3000)).start();
		// new Thread(szovegesSzal("c", 3000)).start();
		Thread t = new Thread(szovegesSzal("d", 3000));
		t.start();
		Thread te = new Thread(szovegesSzal("e", 3000));
		te.start();

		// // tilos használni:
		// // t.stop();
		// // System.exit(123);

		// Thread t1 = new MyThread();
		// Thread t2 = new Thread(new MyRunnable());

		// t1.start();
		// t2.start();

		// t1.join();
		// t2.join();
		t.join();
		te.join();

		System.out.println("All done.");

	}

	static Runnable szovegesSzal(String id, int lepesszam) {
		return () -> {
			for (int i = 0; i < lepesszam; ++i) {
				System.out.println(id + " " + i);
			}
		};
	}
}


class MyThread extends Thread {
	public void run() {
		System.out.println("MyThread");
	}
}

class MyRunnable implements Runnable {
	public void run() {
		System.out.println("MyRunnable");
	}
}

class MyClass {
	int myData1;
	int myData2;

	public void myFun1() {
		for (int i = 0; i < 100000; ++i) {
			++myData1;
		}
	}

	public synchronized void myFun2() {
		// synchronized (this) {
			for (int i = 0; i < 100000; ++i) {
				++myData2;
			}
		// }
	}

	public void myFun3() {
		for (int i = 0; i < 100000; ++i) {
			synchronized (this) {
				++myData2;
			}
		}
	}
}

