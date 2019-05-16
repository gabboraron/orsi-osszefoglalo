# Összefoglaló

## Kliens-szerver kapcsolat
> [kitlei.web.elte.hu/segedanyagok](http://kitlei.web.elte.hu/segedanyagok/felev/2018-2019-tavasz/osztott/osztott-feladatok.html#kliens-szerver)
>
>[orsi-gyak1](https://github.com/gabboraron/orsi-gyak1)

mintafájlok: [gy1/ElsoSzerver.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/ElsoSzerver.java) | [gy1/ElsoKliens.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/ElsoKliens.java) | [gy1/Szerver2.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/Szerver2.java)

### Szerver:
- Első lépésben elindítjuk egy `PORT`-on a `ServerSocket` segítségével: `ServerSocket ss = new ServerSocket(PORT);`
- Várunk a kliens megjelenésére: `Socket s = ss.accept();` _**FIGYELEM!** Ez akár évekig is hajlandó várni, amíg a kliens meg nem jelenik, vagy a programot ki nem kapcsoljuk!_ 
- A **szerverre bejövő** információkat `Scanner` segítségével kapjuk el: `Scanner sc = new Scanner(s.getInputStream());`
- A **szerveren kimenő**ket meg `PrintWriter`el küldjük ki: `PrintWriter pw = new PrintWriter(s.getOutputStream());`

teljes kód:
````Java
import java.io.*;
import java.util.*;
import java.net.*;

public class ElsoSzerver {
	public static void main(String[] args) throws Exception {
		int PORT = 12345;
		try (
			ServerSocket ss = new ServerSocket(PORT);
			Socket s = ss.accept();
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
			String name = sc.nextLine();

			pw.println("Hello, " + name);
			pw.flush();
		}
	}
}
````

### Kliens:
Kliensként ilyenkor felcsatlakozhatunk **PuTTY**-al is akár: _Hostname: `localhost`; Port: `12345`; Connection type: `Raw`; Close window on exit: `Never`_ beállításokkal.

**De írhatunk sajátot is:**
- Először adjuk meg hova szeretnénk kapcsolódni: `String MACHINE = "localhost";` itt a `"localhost"`ot akár lecserélhetjük egy IPra is: `"127.0.0.1"`
- Adjuk meg a `PORT`ot: `int PORT = 12345;`
- Kapcsolódjunk: `Socket s = new Socket(MACHINE, PORT);`
- A **kliensre bejövő** adatfolyamra várjunk egy `Scanner` segíségével: `Scanner sc = new Scanner(s.getInputStream());` 
- A **kliensnél kiemenő** adatokat a szokásos `PrintWriter` segítségével oldhatjuk meg: `PrintWriter pw = new PrintWriter(s.getOutputStream());` De vigyázzunk a használatával, ugyanis, minden kiíratás után `flush`ölni kell amit írtunk. Ez küldi el a szervernek, ha egymás után több dolgot kiíratunk a `PrintWriter`re és utánna `flush`ölünk akkor egybe fog lemenni az egész, nem soronként! Azaz így: `pw.println("AzEnNevem"); pw.flush();`

teljes kód: 
````Java
import java.io.*;
import java.util.*;
import java.net.*;

public class ElsoKliens {
	public static void main(String[] args) throws Exception {
		String MACHINE = "localhost";
		int PORT = 12345;
		try (
			Socket s = new Socket(MACHINE, PORT);
			Scanner sc = new Scanner(s.getInputStream());
			PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
			pw.println("AzEnNevem");
			pw.flush();

			String valasz = sc.nextLine();

			System.out.println(valasz);
		}
	}
}
````
**`Scanner`nél hasznosak lehetnek:**
> `sc.hasNextLine()` - várja következő sort vagy a halott servert
>
> `sc.hasNext()`     - átugorja az összes whitespacet
>
> `sc.hasNextInt()`  - átugorja a whitespaceket és számot vár válaszként

----
### Feladat: `kliens küld -> szerver feldolgoz -> kliens visszakapja a feldolgozott formát`
eredeti: [gyak2](https://github.com/gabboraron/orsi-gyak2)

fájlok: [gy2/Szerver3.java - mintafájl-0ra lép ki](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy2/Szerver3.java) | [gy2/server.java - saját megoldás-karakteres üzenetre lép ki](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy2/server.java)

> A kliens küldjön át sorban egész számokat a szervernek. A számokat a kliens egy fájlból olvassa be. A szerver mindegyik számra meghív egy függvényt, ami egész számot készít (mondjuk `n ↦ 2*n+1`), majd az eredményt visszaküldi a kliensnek. A kliens a visszakapott eredményeket egy fájlba írja ki sorban. Ha a `0` szám következne a kliensoldalon, akkor a kliens kilép.
>
> * A kliens most küldje át az összes adatot a szervernek, és csak utána fogadja a visszaérkező számokat; hasonlóan, a szerver fogadja az összes számot, és csak utána küldje el őket átalakítva a kliensnek.
> 
> * A szerver várakozzon a kliens kilépése után új kliensre, és ez ismétlődjön a végtelenségig.

Az érdekes rész a **sorban egymás után végtelen sok klienst kiszolgáló szerver**:
````Java
while (true){
	try (
		ServerSocket ss = new ServerSocket(PORT);
		Socket s = ss.accept();
		Scanner sc = new Scanner(s.getInputStream());
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		) {
		while(sc.hasNextInt()){
			Integer nr = sc.nextInt(); 	//beérkezik az adat
			tmp.add(toInt(nr));		//feldolgozás
		}	
		answer(tmp, pw);	//válasz
	}
}
````
---
### Feladat: `FTP server`
eredeti: [gyak3](https://github.com/gabboraron/orsi-gyak3)

fájl: [gy3/Szerver4.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy3/Szerver4.java)
> A kliens átküld egy fájlnevet a szervernek. A szerver küldje vissza a fájl tartalmát soronként, ha a fájl létezik, különben pedig egy szöveges hibaüzenetet.
Lényeges rész:
````Java
static void simpleFtp(Scanner sc, PrintWriter pw) {
	String filename = sc.nextLine();

	try (Scanner scFile = new Scanner(new File(filename))) {
		while (scFile.hasNextLine()) {
			String line = scFile.nextLine();
			pw.println(line);
		}
	} catch (IOException e) {
		pw.println("Error: " + e);
	} finally {
		pw.flush();
	}
}
````
---
### Feladat: egyszerre több kliens ugyanazon a porton vagy másikon!
eredeti: [gyak4](https://github.com/gabboraron/orsi-gyak4)

fájl: [gy4/Szerver8.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy4/Szerver8.java)
Felmerülhet, hogy mi van agyszerre **több klienst szeretnénk kezelni egy szerveren egy `PORT`on**. Megoldásként duplikálhatunk mindent, azaz:
- Létrehozzuk a `PORT`ot: `int PORT = 12345;` és hallgatózunk rajta: `ServerSocket ss = new ServerSocket(PORT);`
- Figyelünk egy `Scanner` ésegy `Printwriter`rel, mint eddig: 
	````Java
	Socket s1 = ss.accept();
	Scanner sc1 = new Scanner(s1.getInputStream());
	PrintWriter pw1 = new PrintWriter(s1.getOutputStream());
	````
- Ezt duplikáljuk:
	````Java
	Socket s2 = ss.accept();
	Scanner sc2 = new Scanner(s2.getInputStream());
	PrintWriter pw2 = new PrintWriter(s2.getOutputStream());
	````
- Ezután viszont szabadon kezelhetjük a kettőt, külön, vagy akár egymásnak is átirányíhatjuk
	````Java
	String name1 = sc1.nextLine();
	String name2 = sc2.nextLine();
	````
Ugyanakkor **kezelhetjük őket több porton** is:
- ehhez csak azt kell megváltoztatni, hogy a második `ServerSocket` egy másik porton hallgatózzon:
	````Java
	ServerSocket ss2 = new ServerSocket(54321);
	Socket s2 = ss2.accept();
	Scanner sc2 = new Scanner(s2.getInputStream());
	PrintWriter pw2 = new PrintWriter(s2.getOutputStream());
	````
Teljes kód:
````Java
public static void main(String[] args) throws Exception {
	int PORT = 12345;

	try (
		ServerSocket ss = new ServerSocket(PORT);
		//ServerSocket ss2 = new ServerSocket(54321);

		Socket s1 = ss.accept();
		Scanner sc1 = new Scanner(s1.getInputStream());
		PrintWriter pw1 = new PrintWriter(s1.getOutputStream());

		//Socket s2 = ss2.accept();
		Socket s2 = ss.accept(); //ezt kommentelni kell ha két porton szeretnénk!
		Scanner sc2 = new Scanner(s2.getInputStream());
		PrintWriter pw2 = new PrintWriter(s2.getOutputStream());
		) {
		String name1 = sc1.nextLine();
		String name2 = sc2.nextLine();

		while (true) {
			if (!sc1.hasNextLine())   break;
			sendMsg(name1, sc1, pw2);

			if (!sc2.hasNextLine())   break;
			sendMsg(name2, sc2, pw1);
		}
	}
}
````
[Ezt oldja meg az alábbi feladat is:](https://github.com/gabboraron/orsi-gyak4) 
> A szerverhez kapcsolódjon két kliens egymás után (ugyanazon a porton) úgy, hogy a szerver mindkét kapcsolatot egyszerre tartja nyitva. A kliensek először egy-egy sorban a saját nevüket küldik át, majd felváltva írhatnak be egy-egy sornyi szöveget. A beírt üzeneteket küldje át a szerver a másik kliensnek ilyen alakban: `<másik kliens neve>: <másik kliens üzenete>`. Ha valamelyik kliens bontja a kapcsolatot, akkor a szerver zárja be a másik klienssel a kapcsolatot, és lépjen ki.

## Párhuzamosság
> [kitlei.web.elte.hu/segedanyagok](http://kitlei.web.elte.hu/segedanyagok/felev/2018-2019-tavasz/osztott/osztott-feladatok.html#p%C3%A1rhuzamoss%C3%A1g)
>
>[orsi-gyak5](https://github.com/gabboraron/orsi-gyak5)

mintafájlok: [gy5/Parhuzamossag.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy5/Parhuzamossag.java)

### Server - szálak
- létrehozunk szálat: `Thread t1 = new Thread()`
- elindítjuk: `t1.start();`
- megvárhatjuk, hogy befejezze a futását: `t1.join();`
egyben: 
````Java
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
````
- ebben az esetben 3 szálunk volt: `t1`, `t2`, `main`. Ha nem írjuk ki a `t1.join();`t akkor a `main` nem várja be annak a futánsak a végét és tovább lép. Adott esetben, ha mindez egy `try(){}` blokkban történik akkor a `join()` elhagyásával az erőforrásokat is elengedhetjük, és így például a `t1` szál is megszakad! Jó plda erre a [gy7/MultiThreadChatServer.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy7/MultiThreadChatServer.java) amiben ha elhagynánk a `join()`okat akkor pont ez történne!
- ha valamit szeretnénk elérni mindkét szálról, úgy hogy annak az értéke bizotsan ne módosuljon amíg az egyik szál használja a másik szál által akkor szinkronizálnunk kell: `synchronized (szinkroizálandó){}`

egyben:
````Java
int[] counter = { 0 };

Thread t1 = new Thread(() -> {
	for (int i = 0; i < 100000; ++i) {
		synchronized (counter) {
			++counter[0];
		}
	}
});
````

### Kliens - szálak
Természetesen mindezt a kliensnél is megtehetjük, ha pl [az egyik szálon írunk, másikon olvasunk](https://github.com/gabboraron/orsi-beadando/blob/master/Client.java).

példa: [kecskés feladat](https://github.com/gabboraron/orsi-gyak6#kecsk%C3%A9k) : [gy6/MyGoats.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy6/MyGoats.java)

---
### Feladat: párhuzamosított chatserver
> Készíts olyan chat alkalmazást, amelyben a két kliensnek nem kell egymásra várnia soronként, hanem bármikor beszélhetnek egymáshoz, és ez azonnal kiíródik a másik kliensnél.

részlet: 
````Java
try (
	ServerSocket ss = new ServerSocket(12345);
	) {
	while (true) {
		ClientData client = new ClientData(ss);
		synchronized (otherClients) {
			otherClients.add(client);
		}

		new Thread(() -> {
			while (client.sc.hasNextLine()) {
				String line = client.sc.nextLine();

				synchronized (otherClients) {
					for (ClientData other : otherClients) {
						other.pw.println(line);
						other.pw.flush();
					}
				}
			}

			synchronized (otherClients) {
				otherClients.remove(client);
				try {
					client.close();
				} catch (Exception e) {
							// won't happen
				}
			}
		}).start();
	}
}
````
egész kód: [gy7/MultiThreadChatServerV3.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy7/MultiThreadChatServerV3.java)

## Távoli metódushívás
> [kitlei.web.elte.hu/segedanyagok](http://kitlei.web.elte.hu/segedanyagok/felev/2018-2019-tavasz/osztott/osztott-feladatok.html#t%C3%A1voli-met%C3%B3dush%C3%ADv%C3%A1s)
>
>[orsi-gyak8](https://github.com/gabboraron/orsi-gyak8)

### Server - Deploy
mintafájl: [gy8/RMIDeploy.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy8/RMIDeploy.java)

Szeretnénk eléréni, hogy a kliens közvetlenül a serveren tudjon függvénykeet, metódusokat, eljárásokat hívni, paraméterezni. A **Deploy**ban létrehozzuk a serverünk pár plédányát:
- Lefoglaljuk a `PORT`ot, meg egyebet: `Registry registry = LocateRegistry.createRegistry(PORT);`
- Létrehozzuk ezen a szerverünk egy példányát: ` registry.rebind("rmiAppend", new RemoteAppendTxtServer());`
```Java
public static void main(String args[])
        throws Exception
    {
        final int PORT = 12345;

        Registry registry = LocateRegistry.createRegistry(PORT);
        // Registry registry = LocateRegistry.getRegistry();
        registry.rebind("rmiAppend", new RemoteAppendTxtServer());
     }
````

### Server - server rész
Ezután mintha egy osztály lenne felkonfiguráljuk, létrehozunk változókat, konstruktorokat:
mintafájl: [gy8/RemoteAppendTxtServer.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy8/RemoteAppendTxtServer.java)
````Java
public class RemoteAppendTxtServer
    extends java.rmi.server.UnicastRemoteObject
    implements RemoteAppendTxtInterface
{
    String appendTxt;

    public RemoteAppendTxtServer() throws RemoteException
    {
        this("default");
    }
    
    public String appendTxt(String str) throws RemoteException
    {
        return str + appendTxt;
    }
}  
````

### Server - interface
Ezután már csak egy `interface`re van szükségünk, hogy tudjunk kapcsolódni a klienstől:
mintafájl: [gy8/RemoteAppendTxtInterface.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy8/RemoteAppendTxtInterface.java)
````Java
import java.rmi.*;

public interface RemoteAppendTxtInterface extends Remote
{
    String appendTxt(String str) throws RemoteException;
    // MyData appendTxt(MyData str) throws RemoteException;
} 
````
### Kliens
A kliensnél létrehozzuk az `interface`nek megfelelő `proxy`nkat: 
````Java
RemoteAppendTxtInterface rmiServer = (RemoteAppendTxtInterface)(registry.lookup(srvName));
````
Ahol mostantól nyugodtan hívhatjuk az `interface`n megengedett eljárásokat:
````Java
String reply = rmiServer.appendTxt(text);
````
---

### példa feladat:
[Lottós feladat](https://github.com/gabboraron/orsi-gyak9) : [Lottós feladat fájljai](https://github.com/gabboraron/orsi-osszefoglalo/tree/master/gy9)

## Adatbázis kezelés
> [kitlei.web.elte.hu/segedanyagok](http://kitlei.web.elte.hu/segedanyagok/felev/2018-2019-tavasz/osztott/osztott-feladatok.html#adatb%C3%A1ziskezel%C3%A9s)
>
> [gy10](https://github.com/gabboraron/orsi-gyak10)
**A táblákon csak a szokásos SQL parancsok használhatóak!**
