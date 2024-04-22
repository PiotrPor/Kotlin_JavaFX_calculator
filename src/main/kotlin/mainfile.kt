import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.Pane
//import javafx.scene.layout.StackPane
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage

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
    //var klawisze_cyfr: Array<Button>

    override fun start(stage: Stage) {
        //val korzen = StackPane()
        val powierzchnia = Pane()

        var wyswietlacz = TextField()
        var klawisze_cyfr = arrayOf(Button("0"),
            Button("1"), Button("2"), Button("3"),
            Button("4"), Button("5"), Button("6"),
            Button("7"), Button("8"), Button("9"),
        )
        //var a : Int
        //var b : Int
        //var nr : Int
        wyswietlacz.resizeRelocate(OdstLewKlaw,OdstGorKlaw,SzerTekst,WysTekst)
        powierzchnia.getChildren().add(wyswietlacz)
        for(a in 0..2)
        {
            for(b in 0..2)
            {
                //nr = 3*a+b+1
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

        val scena = Scene(powierzchnia, SzerokoscOkna, WysokoscOkna)
        stage.title = "Okno"
        stage.scene = scena
        stage.show()
    }
}

fun main() {
    Application.launch(OknoKalkulatora::class.java)
}