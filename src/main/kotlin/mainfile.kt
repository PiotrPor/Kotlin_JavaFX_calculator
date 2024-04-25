import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
//import javafx.scene.layout.StackPane
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.event.EventHandler
import kotlin.math.sqrt

//potrzebne stale
const val OdstGorKlaw: Double = 10.0 //odstep od gornej krawedzi okna
const val OdstLewKlaw: Double = 10.0 //odstep od lewej krawedzi okna
const val SzerTekst: Double = 360.0
const val WysTekst: Double = 40.0
const val OdstKlawTekst: Double = 20.0 //odstep pionowo miedzy polem tekstowym a klawiszami
const val SzerKlaw: Double = 75.0 //50 //szerokosc klawisza
const val WysKlaw: Double = 75.0 //50 //wysokosc klawisza
const val OdstPionKlaw: Double = 20.0 //odstep miedzy klawiszami w osi pionowej
const val OdstPozKlaw: Double = 20.0 //odstep miedzy klawiszami w osi poziomej
const val staly_pionowy_odstep = OdstGorKlaw+WysTekst+OdstKlawTekst
//
const val SzerokoscOkna : Double = 800.0
const val WysokoscOkna : Double = 700.0

class OknoKalkulatora : Application() {
    var wyswietlacz = TextField()
    var klawisze_cyfr : Array<Button> = arrayOf(Button("0")) //do redefinicji
    var klawisze_dzialan : Array<Button> = arrayOf(Button("+")) //do redefinicji
    var klawisz_kropka : Button = Button(".")
    var klawisz_rowna_sie : Button = Button("=")
    var klawisz_ujemnych : Button = Button("-/+")
    var klawisz_pierwiastka : Button = Button("sqrt(x)")
    //
    var wybrana_operacja : TypDzialania = TypDzialania.Nic
    var A : Double = 0.0 //liczba po prawej stronie operatora arytmetycznego
    var B : Double = 0.0 //liczba po lewej stronie operatora arytmetycznego
    //
    var wpisano_pierwsza_cyfre : Boolean = false
    var wpisano_kropke : Boolean = false
    var wpisano_pierwsza_liczbe : Boolean = false
    var dzielenie_przez_0 : Boolean = false

    fun dopisz_cyfre(cyfra : Int)
    {
        var tekst = wyswietlacz.text
        if(!wpisano_pierwsza_cyfre)
        {
            tekst = cyfra.toString()
            wpisano_pierwsza_cyfre = true
        }
        else
        {
            tekst = tekst.plus(cyfra.toString())
        }
        wyswietlacz.text = tekst
    }

    fun dopisz_kropke()
    {
        if(!wpisano_kropke)
        {
            wyswietlacz.text = wyswietlacz.text.plus(".")
            wpisano_kropke = true
        }
    }

    fun napisz_komunikat(wpis : String)
    {
        wyswietlacz.text = wpis
    }

    override fun start(stage: Stage) {
        //val korzen = StackPane()
        val powierzchnia = Pane()

        //var wyswietlacz = TextField()
        klawisze_cyfr = arrayOf(Button("0"),
            Button("1"), Button("2"), Button("3"),
            Button("4"), Button("5"), Button("6"),
            Button("7"), Button("8"), Button("9"),
        )
        wyswietlacz.resizeRelocate(OdstLewKlaw,OdstGorKlaw,SzerTekst,WysTekst)
        powierzchnia.getChildren().add(wyswietlacz)
        wyswietlacz.text = "0"
        for(a in 0..2)
        {
            for(b in 0..2)
            {
                //val przycisk = Button(nr.toString())
                klawisze_cyfr[3*a+b+1].resizeRelocate(
                    OdstLewKlaw+b*(SzerKlaw+OdstPozKlaw),
                    staly_pionowy_odstep+a*(WysKlaw+OdstPionKlaw),
                    SzerKlaw,
                    WysKlaw
                )
                powierzchnia.getChildren().add(klawisze_cyfr[3*a+b+1])
            }
        }
        klawisze_cyfr[0].resizeRelocate(
            OdstLewKlaw+SzerKlaw+OdstPozKlaw,
            staly_pionowy_odstep+3*(WysKlaw+OdstPionKlaw),
            SzerKlaw,
            WysKlaw
        )
        powierzchnia.getChildren().add(klawisze_cyfr[0])
        //
        klawisze_dzialan = arrayOf(Button("+"), Button("-"), Button("*"), Button("/"))
        for(a in 0..3)
        {
            klawisze_dzialan[a].resizeRelocate(
                OdstLewKlaw+3*(SzerKlaw+OdstPozKlaw),
                staly_pionowy_odstep+a*(WysKlaw+OdstPionKlaw),
                SzerKlaw,
                WysKlaw
            )
            powierzchnia.getChildren().add(klawisze_dzialan[a])
        }
        klawisz_kropka.resizeRelocate(
            OdstLewKlaw+2*(SzerKlaw+OdstPozKlaw),
            staly_pionowy_odstep+3*(WysKlaw+OdstPionKlaw),
            SzerKlaw,
            WysKlaw
        )
        powierzchnia.getChildren().add(klawisz_kropka)
        klawisz_rowna_sie.resizeRelocate(
            OdstLewKlaw+3*(SzerKlaw+OdstPozKlaw),
            staly_pionowy_odstep+4*(WysKlaw+OdstPionKlaw),
            SzerKlaw,
            WysKlaw
        )
        powierzchnia.getChildren().add(klawisz_rowna_sie)
        klawisz_ujemnych.resizeRelocate(
            OdstLewKlaw,
            staly_pionowy_odstep+3*(WysKlaw+OdstPionKlaw),
            SzerKlaw,
            WysKlaw
        )
        powierzchnia.getChildren().add(klawisz_ujemnych)
        klawisz_pierwiastka.resizeRelocate(
            OdstLewKlaw+2*(SzerKlaw+OdstPozKlaw),
            staly_pionowy_odstep+4*(WysKlaw+OdstPionKlaw),
            SzerKlaw,
            WysKlaw
        )
        powierzchnia.getChildren().add(klawisz_pierwiastka)
        //
        for(a in 0..9)
        {
            klawisze_cyfr[a].onAction = EventHandler{dopisz_cyfre(a)}
        }
        klawisze_dzialan[0].onAction = EventHandler{ ustaw_typ_dzialania(TypDzialania.Dodawanie) }
        klawisze_dzialan[1].onAction = EventHandler{ ustaw_typ_dzialania(TypDzialania.Odejmowanie) }
        klawisze_dzialan[2].onAction = EventHandler{ ustaw_typ_dzialania(TypDzialania.Mnozenie) }
        klawisze_dzialan[3].onAction = EventHandler{ ustaw_typ_dzialania(TypDzialania.Dzielenie) }
        klawisz_rowna_sie.onAction = EventHandler{nacisniecie_rowna_sie()}
        klawisz_kropka.onAction = EventHandler{ dopisz_kropke() }
        klawisz_ujemnych.onAction = EventHandler{dopisz_minus()}
        klawisz_pierwiastka.onAction = EventHandler{ pierwiastkowanie() }


        val scena = Scene(powierzchnia, SzerokoscOkna, WysokoscOkna)
        stage.title = "Okno"
        stage.scene = scena
        stage.show()
    }

    fun policz() : Double
    {
        var wynik: Double = 0.0
        if(B == 0.0 && wybrana_operacja == TypDzialania.Dzielenie)
        {
            dzielenie_przez_0 = true
        }
        else
        {
            if(wybrana_operacja == TypDzialania.Dodawanie) {wynik=A+B}
            if(wybrana_operacja == TypDzialania.Odejmowanie) {wynik=A-B}
            if(wybrana_operacja == TypDzialania.Mnozenie) {wynik=A*B}
            if(wybrana_operacja == TypDzialania.Dzielenie) {wynik=A/B}
        }
        return wynik
    }

    fun ustaw_typ_dzialania(wybrane : TypDzialania)
    {
        wybrana_operacja = wybrane
        if(!wpisano_pierwsza_liczbe)
        {
            wpisano_pierwsza_liczbe = true //rozpisac bardziej?
            wpisano_pierwsza_cyfre = false
            wpisano_kropke = false //napewno tu?
            A = wyswietlacz.text.toDouble()
        }
        else
        {
            nacisniecie_rowna_sie()
        }

    }

    fun dopisz_minus()
    {
        var tekstowe = wyswietlacz.text
        if(tekstowe.get(0).equals('-'))
        {
            tekstowe = tekstowe.drop(1)
        }
        else
        {
            tekstowe = "-"+tekstowe
        }
        wyswietlacz.text = tekstowe
    }

    fun nacisniecie_rowna_sie()
    {
        B = wyswietlacz.getText().toDouble()
        if(wybrana_operacja != TypDzialania.Nic)
        {
            val wynik : Double = policz()
            if(dzielenie_przez_0)
            {
                napisz_komunikat("dzielenie przez zero!")
            }
            else
            {
                B = wynik
                wyswietlacz.text = wynik.toString()
            }
        }
    }

    fun pierwiastkowanie()
    {
        var liczba : Double = wyswietlacz.text.toDouble()
        if(liczba >= 0)
        {
            liczba = sqrt(liczba)
            wyswietlacz.text = liczba.toString()
        }
        else
        {
            napisz_komunikat("Pierwiastek z ujemnej jest urojony")
        }
    }
}

fun main() {
    Application.launch(OknoKalkulatora::class.java)
}