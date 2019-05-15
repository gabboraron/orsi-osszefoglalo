# Összefoglaló

## Kliens-szerver kapcsolat
> [kitlei.web.elte.hu/segedanyagok](http://kitlei.web.elte.hu/segedanyagok/felev/2018-2019-tavasz/osztott/osztott-feladatok.html#kliens-szerver)
>
>[orsi-gyak1](https://github.com/gabboraron/orsi-gyak1)

mintafájlok: [ElsoSzerver.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/ElsoSzerver.java) [ElsoKliens.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/ElsoKliens.java) [Szerver2.java](https://github.com/gabboraron/orsi-osszefoglalo/blob/master/gy1/Szerver2.java)

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
`sc.hasNextLine()` - várja következő sort vagy a halott servert

`sc.hasNext()`     - átugorja az össes whitespacet

`sc.hasNextInt()`  - átugorja a whitespaceket és számot vár válaszként
