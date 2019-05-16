# Összefoglaló

## Kliens-szerver kapcsolat
> [kitlei.web.elte.hu/segedanyagok](http://kitlei.web.elte.hu/segedanyagok/felev/2018-2019-tavasz/osztott/osztott-feladatok.html#kliens-szerver)
>
>[orsi-gyak1](https://github.com/gabboraron/orsi-gyak1)

mintafájlok: [ElsoSzerver.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/ElsoSzerver.java) | [ElsoKliens.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/ElsoKliens.java) | [Szerver2.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/Szerver2.java)

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

fájlok: [mintafájl-0ra lép ki](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy2/Szerver3.java) | [saját megoldás-karakteres üzenetre lép ki](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy2/server.java)

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
