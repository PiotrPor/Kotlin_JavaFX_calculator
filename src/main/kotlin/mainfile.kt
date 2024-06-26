import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
//import javafx.scene.layout.StackPane
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import javafx.event.EventHandler
import javafx.event.ActionEvent
import kotlin.math.sqrt
import javafx.scene.text.Font

//potrzebne stale
const val OdstGorKlaw: Double = 13.0 //odstep od gornej krawedzi okna
const val OdstLewKlaw: Double = 13.0 //odstep od lewej krawedzi okna
const val OdstKlawTekst: Double = 26.0 //odstep pionowo miedzy polem tekstowym a klawiszami
const val SzerKlaw: Double = 100.0 //50 //szerokosc klawisza
const val WysKlaw: Double = 100.0 //50 //wysokosc klawisza
const val OdstPionKlaw: Double = 26.0 //odstep miedzy klawiszami w osi pionowej
const val OdstPozKlaw: Double = 26.0 //odstep miedzy klawiszami w osi poziomej
const val SzerTekst = 4*SzerKlaw + 3*OdstPozKlaw
const val WysTekst: Double = 70.0
const val staly_pionowy_odstep = OdstGorKlaw+WysTekst+OdstKlawTekst
//
const val SzerokoscOkna = SzerTekst + 2*OdstLewKlaw
const val WysokoscOkna = staly_pionowy_odstep+6*WysKlaw+5*OdstPionKlaw+OdstGorKlaw

class OknoKalkulatora : Application(), EventHandler<ActionEvent> {
    var wyswietlacz = TextField()
    var klawisze_cyfr : Array<Button> = arrayOf(Button("0")) //do redefinicji
    var klawisze_dzialan : Array<Button> = arrayOf(Button("+")) //do redefinicji
    var klawisz_kropka : Button = Button(".")
    var klawisz_rowna_sie : Button = Button("=")
    var klawisz_ujemnych : Button = Button("-/+")
    var klawisz_pierwiastka : Button = Button("sqrt(x)")
    var klawisz_res_liczba : Button = Button("reset liczby")
    var klawisz_res_total : Button = Button("reset kalkulatora")
    //
    var wybrana_operacja : TypDzialania = TypDzialania.Nic
    var A : Double = 0.0 //liczba po prawej stronie operatora arytmetycznego
    var B : Double = 0.0 //liczba po lewej stronie operatora arytmetycznego
    //
    var wpisano_pierwsza_cyfre : Boolean = false
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

    fun napisz_komunikat(wpis : String)
    {
        wyswietlacz.text = wpis
    }

    override fun start(stage: Stage) {
        val powierzchnia = Pane()

                klawisze_cyfr = arrayOf(Button("0"),
            Button("1"), Button("2"), Button("3"),
            Button("4"), Button("5"), Button("6"),
            Button("7"), Button("8"), Button("9"),
        )
        wyswietlacz.relocate(OdstLewKlaw,OdstGorKlaw)
        wyswietlacz.setPrefSize(SzerTekst,WysTekst)
        powierzchnia.getChildren().add(wyswietlacz)
        wyswietlacz.setFont(Font(0.5*WysTekst))
        wyswietlacz.text = "0"
        for(a in 0..2)
        {
            for(b in 0..2)
            {
                klawisze_cyfr[3*a+b+1].relocate(
                    OdstLewKlaw+b*(SzerKlaw+OdstPozKlaw),
                    staly_pionowy_odstep+a*(WysKlaw+OdstPionKlaw)
                )
                powierzchnia.getChildren().add(klawisze_cyfr[3*a+b+1])
                klawisze_cyfr[3*a+b+1].setFont(Font(0.5*WysKlaw))
            }
        }
        klawisze_cyfr[0].relocate(
            OdstLewKlaw+SzerKlaw+OdstPozKlaw,
            staly_pionowy_odstep+3*(WysKlaw+OdstPionKlaw),
        )
        powierzchnia.getChildren().add(klawisze_cyfr[0])
        klawisze_cyfr[0].setFont(Font(0.5*WysKlaw))
        //
        klawisze_dzialan = arrayOf(Button("+"), Button("-"), Button("*"), Button("/"))
        for(a in 0..3)
        {
            klawisze_dzialan[a].relocate(
                OdstLewKlaw+3*(SzerKlaw+OdstPozKlaw),
                staly_pionowy_odstep+a*(WysKlaw+OdstPionKlaw),
            )
            powierzchnia.getChildren().add(klawisze_dzialan[a])
            klawisze_dzialan[a].setFont(Font(0.4*WysKlaw))
        }
        klawisz_kropka.relocate(
            OdstLewKlaw+2*(SzerKlaw+OdstPozKlaw),
            staly_pionowy_odstep+3*(WysKlaw+OdstPionKlaw),
        )
        klawisz_kropka.setFont(Font(0.5*WysKlaw))
        powierzchnia.getChildren().add(klawisz_kropka)
        klawisz_rowna_sie.relocate(
            OdstLewKlaw+3*(SzerKlaw+OdstPozKlaw),
            staly_pionowy_odstep+4*(WysKlaw+OdstPionKlaw),
        )
        klawisz_rowna_sie.setFont(Font(0.4*WysKlaw))
        powierzchnia.getChildren().add(klawisz_rowna_sie)
        klawisz_ujemnych.relocate(
            OdstLewKlaw,
            staly_pionowy_odstep+3*(WysKlaw+OdstPionKlaw),
        )
        klawisz_ujemnych.setFont(Font(0.3*WysKlaw))
        powierzchnia.getChildren().add(klawisz_ujemnych)
        klawisz_pierwiastka.relocate(
            OdstLewKlaw+2*(SzerKlaw+OdstPozKlaw),
            staly_pionowy_odstep+4*(WysKlaw+OdstPionKlaw),
        )
        klawisz_pierwiastka.setFont(Font(0.2*WysKlaw))
        powierzchnia.getChildren().add(klawisz_pierwiastka)
        klawisz_res_liczba.relocate(
            OdstLewKlaw,
            staly_pionowy_odstep+4*(WysKlaw+OdstPionKlaw),
        )
        klawisz_res_liczba.setFont(Font(0.2*WysKlaw))
        powierzchnia.getChildren().add(klawisz_res_liczba)
        klawisz_res_total.relocate(
            OdstLewKlaw,
            staly_pionowy_odstep+5*(WysKlaw+OdstPionKlaw),
        )
        klawisz_res_total.setFont(Font(0.2*WysKlaw))
        powierzchnia.getChildren().add(klawisz_res_total)
        //
        for(a in 0..9)
        {
            klawisze_cyfr[a].setOnAction(this)
            klawisze_cyfr[a].setPrefSize(SzerKlaw,WysKlaw)
        }
        for(a in 0..3)
        {
            klawisze_dzialan[a].setOnAction(this)
            klawisze_dzialan[a].setPrefSize(SzerKlaw,WysKlaw)
        }
        klawisz_rowna_sie.setOnAction(this)
        klawisz_rowna_sie.setPrefSize(SzerKlaw,WysKlaw)
        klawisz_kropka.setOnAction(this)
        klawisz_kropka.setPrefSize(SzerKlaw,WysKlaw)
        klawisz_ujemnych.setOnAction(this)
        klawisz_ujemnych.setPrefSize(SzerKlaw,WysKlaw)
        klawisz_pierwiastka.setOnAction(this)
        klawisz_pierwiastka.setPrefSize(SzerKlaw,WysKlaw)
        klawisz_res_liczba.setOnAction(this)
        klawisz_res_liczba.setPrefSize(2*SzerKlaw,WysKlaw)
        klawisz_res_total.setOnAction(this)
        klawisz_res_total.setPrefSize(2*SzerKlaw,WysKlaw)

        val scena = Scene(powierzchnia, SzerokoscOkna, WysokoscOkna)
        stage.title = "Okno"
        stage.scene = scena
        stage.show()
    }

    fun policz() : Double {
        var wynik: Double = 0.0
        if (B == 0.0 && wybrana_operacja == TypDzialania.Dzielenie) {
            dzielenie_przez_0 = true
        } else {
            if (wybrana_operacja == TypDzialania.Dodawanie) {
                wynik = A + B
            }
            if (wybrana_operacja == TypDzialania.Odejmowanie) {
                wynik = A - B
            }
            if (wybrana_operacja == TypDzialania.Mnozenie) {
                wynik = A * B
            }
            if (wybrana_operacja == TypDzialania.Dzielenie) {
                wynik = A / B
            }
        }
        return wynik
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
                A = wynik
                wyswietlacz.text = wynik.toString()
            }
        }
    }

    override fun handle(wydarzenie : ActionEvent)
    {
        val zrodlo = wydarzenie.getSource()

        for(a in 0..9)
        {
            if(klawisze_cyfr[a] == zrodlo)
            {
                dopisz_cyfre(a)
                break
            }
        }

        var numer_wybranej_operacji = -1
        for(a in 0..3)
        {
            if(klawisze_dzialan[a] == zrodlo)
            {
                numer_wybranej_operacji = a
                break
            }
        }
        if(numer_wybranej_operacji != -1)
        {
            //wybrana_operacja = TypDzialania.entries.get(numer_wybranej_operacji)
            if(!wpisano_pierwsza_liczbe)
            {
                wybrana_operacja = TypDzialania.entries.get(numer_wybranej_operacji)
                wpisano_pierwsza_liczbe = true
                wpisano_pierwsza_cyfre = false
                A = wyswietlacz.text.toDouble()
            }
            else
            {
                nacisniecie_rowna_sie()
                wybrana_operacja = TypDzialania.entries.get(numer_wybranej_operacji)
                wpisano_pierwsza_cyfre = false
            }
        }

        if(klawisz_rowna_sie == zrodlo) {nacisniecie_rowna_sie()}

        if(klawisz_kropka == zrodlo)
        {
            if(!wyswietlacz.text.contains('.'))
            {
                wyswietlacz.text = wyswietlacz.text.plus(".")
            }
        }

        if(klawisz_ujemnych == zrodlo)
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

        if(klawisz_pierwiastka == zrodlo)
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

        if(klawisz_res_liczba == zrodlo || klawisz_res_total == zrodlo)
        {
            wyswietlacz.text = "0.0"
            wpisano_pierwsza_cyfre = false
            if(klawisz_res_total == zrodlo)
            {
                wpisano_pierwsza_liczbe = false
                dzielenie_przez_0 = false
                A = 0.0
                B = 0.0
                wybrana_operacja = TypDzialania.Nic
            }
        }
    }
}

fun main() {
    Application.launch(OknoKalkulatora::class.java)
}