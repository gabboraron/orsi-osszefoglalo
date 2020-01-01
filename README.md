# Összefoglaló
**Tartalom:**
- [Elméleti rész főbb fogalmai](https://github.com/gabboraron/orsi-osszefoglalo#elméleti-összefoglaló)
  - [Kapcsolódó programok](https://github.com/gabboraron/orsi-osszefoglalo#kapcsolódó-programok)
  - [Irodalom](https://github.com/gabboraron/orsi-osszefoglalo#irodalom)
- [Gyakorlati összefoglaló](https://github.com/gabboraron/orsi-osszefoglalo#gyakorlati-összefoglaló)
- [gyakorlati rész anyaga hordozható formában](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/portable.rar)
----
# Elméleti rész főbb fogalmai
**Főbb irányvonalak:**
- Grid
- Cloud
- infomrációs rendszerek

**Virtualizáció**
> A `hardware -> Interface -> Hardware/software system` átalakítja `program -> Interface A -> Implementation of mimicking A on B -> Interface B -> Hardware/software system B` alakká.

***Szerkezete***
- Application
  - Library functions
  - Operating system, sys calls
- Hardware -> general instructions,Prvileged instructions

***Process VM és VM monitor***

*JAVA*

> minden bytcode egy "mini program", a runtimeban van futtatva:
- Application
- Runtime sys
  - Operating system
- Hardware

*VMware, VirtualBox*
- Applications 
  - Operating system
- Virtual machine monitor
- Hardware

> **Előnyei a *JAVA* féle megoldáshoz képest:**
>
> A teljes `Application` és `OS` réteget egy teljesen új `gép`re viheti át. **Ekkor a `VM Monitor` egy operációs rendszer!** Azaz nagyon egszerű rendszerekkel rendelkezik, a minimális driverekkel és hasonlókkal, de egy teljes OS tulajdonságaival ugyankkor a minimális tulajdonságokkal amik a feladat végrehajtásához szükségesek.
> 
> Tehát közvelten a hardwaerhez van útja a az `App` és `OS` rétegnek, hiszen a `VM`közbvetlen a hardwaret szólítja meg.


**USER level és kernel level szálak**

[stackowerflow](https://stackoverflow.com/questions/15983872/difference-between-user-level-and-kernel-supported-threads)
> a lényege, hogy az egyiket a processzor szintjén hozzuk létre, a másikat pedig a programnak kiosztott erőforrásokban hozzuk létre

**Interaktív program**
> Olyan program ami felhasználó interakciói nélkül nem műödne, pl MS-Excel, [bővebben](https://www.computerhope.com/jargon/i/inteprog.htm)

**mobil IP**
> Az `IP`t kiosztó `home server` újrahosztol
> 
> `anycast` - továbbítja a hozzá kapcsolódó routereknek a csomagot 
>
> `changeroute` - az olvasási pontot átteszi máshova

## Kapcsolódó programok

**[Owncloud](https://owncloud.org/)**
- személyes felhő, megy rpi-n is

**[Amazon EC2](https://aws.amazon.com/ec2/)**
- számítási kapacitást visz át, elosztott rendszerekbe, ahol bucketeekben dolgoznak a felhasználók

**[FrostWire](https://www.frostwire.com/)**
- Cloud downloader, Bit Torrent client, media player, preview and play while download
- connect to different torrent search engine

**[Bit Torrent](https://www.bittorrent.com/)**
- megkeresi a bit torent index fájlt a fájlszerveren (Pirate Bay)
- tovább meg a trackerhez a `.torent`fájlban talált pointer alapján
- a tracker megmondja, hogy kinek lehet meg az adott rész, nem pontosan, csak lehetőségként, úgy, hogy az aki keresi annak meglegyen az ami a másiknál hiányzik
-  [magnet link](https://www.vuze.com/about-torrents/magnet-links)ek egy P2P rendszerre mutató pointerek

**[VirtualBox](https://www.virtualbox.org/)**
- VM teszteléshez

**[planetlab](https://www.planet-lab.org/)**
- egy elosztott szerverkutatóhálózatok között

## Irodalom
- [egész féléves előadás anyaga egyben](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/ea/ilovepdf_merged(1).pdf)
- [securityval kapcsolatos fájlok, előadások](https://github.com/gabboraron/orsi-osszefoglalo/commit/0513d361a8295e16a9c19f189d2a75ddee1c8a0c)
- [Maarten van Steen könyv](https://www.distributed-systems.net/index.php/books/distributed-systems-3rd-edition-2017/ds3-sneak-preview/)

--------
# Gyakorlati összefoglaló
## Kliens-szerver kapcsolat
> [kitlei.web.elte.hu/segedanyagok](http://kitlei.web.elte.hu/segedanyagok/felev/2018-2019-tavasz/osztott/osztott-feladatok.html#kliens-szerver)
>
> [orsi-gyak1](https://github.com/gabboraron/orsi-gyak1)

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

----

## Osztott ZH 2019.05.20.
> Elégségesért az alapfeladatot kell teljesen megoldani.
> - Az adatok (osztály neve, port stb.) a megadott alakban szerepeljenek a programban.
> - A megoldáshoz célszerű osztályokat, segédfüggvényeket, adatszerkezeteket stb. létrehozni.
> - Használhatók az alábbi segédanyagok:
>   - A JDK dokumentációja itt érhető el.
>   - A gyakorlatok anyagai (és az adatbáziskezeléshez szükséges jar fájlok) itt érhetők el.
> - A megoldást teljesen önállóan kell elkészíteni. Együttműködés/másolás esetén minden érintett fél elégtelent kap.
> A további feladatok eggyel növelik a megszerzett jegyet, és tetszőleges sorrendben adhatók hozzá a programhoz.
> Beadás: az elkészült megoldás fájljait `zip`-pel kell tömöríteni a feltöltéshez. IDE-ben készült megoldásnál a teljes projekt becsomagolható.
### Alapfeladat
**Kidolgozva:** [Server](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/vizsga%2019-05-20/Server.java) | [kliens](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/vizsga%2019-05-20/AuctionClient.java) 
> !! Az alapfeladatban se a szervernek, se a kliensnek nem kell párhuzamosan (több szálon) működnie.
> Készíts olyan szervert az Auction osztályba, amely a `2121` porton indul el, és egy aukciós házat szimulál.
> A szerver egy klienst vár, majd a kliens lecsatlakozása után csatlakozhat újabb, és újabb kliens. A kliensek szövegesen kommunikálnak a szerverrel; az első sorban elküldik a nevüket, majd a következő sorokban az alábbi három lehetőségből választhatnak.
> - `put <item_name>`: az adott nevű tárgyat meghirdeti (tegyük fel, hogy még nincs azonos nevű tárgy), kezdetben `0` áron
> - `list`: lekérdezi a meghirdetett tárgyakat. A szerver először elküld egy számot (hány meghirdetett tárgy van), majd soronként egyet-egyet a `név ár nyertes` formában
> - `bid <item_name>`: a felhasználó licitál a tárgyra, eggyel megnövelve az értékét, és az aktuális nyertest a felhasználóra állítva
> Ehhez a szerverhez készíts egy klienst is az `AuctionClient` programba.
### +1
**Kidolgozva:** [Server2](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/vizsga%2019-05-20/Server2.java) | [Kliens](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/vizsga%2019-05-20/AuctionClient2.java)
> Módositsd a szervert, hogy egyszerre tetszőlegesen sok klienst engedjen csatlakozni, és mind párhuzamosan használhassák az előző feladatban megvalósított parancsokat.
> Az aukciók legyenek limitált idejűek: a meghirdetéstől számítva 30 másodpercig tartanak. Az idő lejárta után befejeződnek: többet nem lehet rájuk licitálni, és nem jelennek meg a listában. Amikor az aukció lejár, a szerver üzenetet küld a győztes kliensnek a nyereményéről.
### +1
> A szerver exportáljon egy RMI objektumot, amely a `BidHistory` interfészen keresztül, a `bid_history` néven érhető el.
> Az interfésznek legyen egy olyan metódusa, ami visszaadja az összes eddigi licit adatait (ki, mire, mennyiért) egy időben növekvő listában.
> Az interfész másik metódusát meghívva lehessen átváltani nyitva/zárvatartás között. Amíg az aukciós ház zárva tart, a licitálás szünetel, és a szerver egy ezt jelző üzenetet küld vissza a klienseknek.
### +1
> A szerver tárolja el adatbázisban, ki hány tárgyat hirdetett meg eddig összesen. Ez az érték maradjon meg a szerver újraindításakor is, ne kezdődjön előről nulláról. Mindenki maximum három tárgyat hirdethet meg - az afölötti kéréseket a szerver figyelmen kívül hagyja.

## Osztott ZH - régebbi
### Alapfeladat
**Kidolgozva:** [Server](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/vizsga-regi/Szerver.java) | [Kliens](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/vizsga-regi/Iro.java)  
> Készíts cikkek írására egy szerver-kliens alkalmazást. 
> Készíts egy `ujsag.Cikk` típust, ami egy cikket tartalmaz, egy címet. valamint szavak listáját. 
> Készíts egy `ujsag.Server` típust. Ami egy futtatható' Java osztály. A main függvény indítson egy szervert, az első paranccsori paraméterként megadott porton. es várja egymás utáni kliensek csatlakozásat. A klienstöl csatlakozás után először egy sort várjon. ami a cikk címét tartalmazza. majd ezután szavak sorozatát A szerver nem küld válaszokat a kliensnek 
> A szerver tárolja el a kapott cikkeket egy adatszerkezetbe. Amennyiben egy cikk korábban már létezett. azt írja felül. 
> Keszits egy `ujsag.iro` nevü futtatható Java osztályt. A program kérjen be a felhasználótól a standard bemeneten egy címet. majd szavak listáját a standard bemenet zárásáig. és azokat folyamatosan küldje el a paranccsori paraméterként megkapott szerveren. (az első parancssori paraméter a host. a második a port) 

## Osztott ZH - régebbi2
### Alapfeladat
**Kidolgozva:** [Server](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/vizsga-regi2/Szerver.java) 
> Készíts sportmérkőzések lebonyolítására egy szerver-kliens alkalmazást. 
> - Készíts egy `sport.Pont` típust, ami azt reprezentálja, ha egy csapat pontot szerzett. Tartalmazzon két adattagot: hányadik csapat (1, vagy 2) szerezte a pontot. és a játék hányadik másodpercében. 
> - Készíts egy `sport.Merkozes` típust, ami egy mérkőzést reprezentál: tartalmaz két csapatnevet (stringek), valamint a pontok listáját. 
> - Készíts egy `sport.Szerver` típust, ami egy futtatható Java osztály. A main függvény indítson egy szervert az első paranccsori paraméterként megadott porton, es várja egymás utáni kliensek csatlakozásat. A klienstől csatlakozás után először két sort várjon. ami a két csapat nevét tartalmazza, majd ezután pontok sorozatát. A kliens a pontoknál csak azt küldi el. melyik csapat szerezte a pontot (szám formájában), az idő automatikusan számolódik az alapján, a kliens hány rnásodperce csatlakozott. A szerver nem küld válaszokat a kliensnek. A szerver jelenítse meg a standard kimeneten az aktuális mérkőzés információit minden változás után. 
> - Készíts egy `sport.Feljegyzo` nevű futtatható Java osztályt. A program kérjen be a felhasználótól a standard bemeneten két csapatnevet, majd sorok listáját a jatek_vege. üzenetig. A sorok vagy 1-est. vagy 2-est, vagy a latek_vege“ üzenetet tartalmazhatják. 
