# Käyttöohje

Sovelluksen viimeisin julkaisu jar-tiedostona voidaan ladata [täältä](https://github.com/tspaanan/shoplist-gener/releases/tag/viikko6).

## Konfigurointi

Ohjelma konfiguroi itse itsensä ja pyytää tarvittaessa käyttäjältä halutun tietokantatiedoston nimen. Ti
etokantatiedoston nimi tallennetaan config.properties -tiedostoon.

**(debug-vaihe)** Sovellus tiedustelee käyttäjältä, täytetäänkö tyhjä tietokanta testidatalla vai jätetäänkö se tyhjäksi käyttäjän syöttämiä reseptejä odottamaan.

## Käynnistäminen

Ohjelman voi ajaa komentoriviltä seuraavalla komennolla:
```
java -jar shoplistgener-2.0-SNAPSHOT.jar
```

## Käyttäminen

Ohjelma avautuu oletusnäkymään. Vasemmalla olevat kolme nappia vaihtavat kolmen eri tilan välillä. Eri tilat näytetään ikkunan oikealla puolella.

### Viikkomenun ja kauppalistan luominen ja tarkastelu

'Print Week's Menu and shopping list' arpoo automaattisesti seitsemän ruokalajin menun ja tulostaa näytölle sen vaatimien ainesosien pohjalta kauppalistan. Menun arpomisen jälkeen käyttäjä voi vaihtaa haluamansa ruokalajin satunnaiseen uuteen tai itse valitsemaansa ruokalajiin. Kauppalista päivittyy jokaisen valinnan jälkeen automaattisesti. Käyttäjä voi myös tarkastella valitun ruokalajin reseptiä.

### Reseptien tarkastelu

'View recipes' tuo näkyviin kaikki tietokantaan tallennetut reseptit. Käyttäjä voi joko valita haluamansa reseptin listasta tai kirjoittaa sen nimen tekstikenttään. Molemmille hakutavoille löytyy oma nappinsa ruudun keskeltä. Kun käyttäjä on tarkastellut reseptiä, ruudun oikealta laidalta löytyvät toiminnot uuden reseptin lisäämiseen, tarkasteltavan reseptin muokkaamiseen ja tarkasteltavan reseptin poistamiseen.

### Reseptien muokkaaminen ja lisääminen

'Add new recipe' tuo näkyviin reseptien muokkaamiseen tarkoitetut kentät. Ylhäällä ovat kentät reseptin nimen (vasemmalla) ja reseptin valmistusohjeiden (oikealla) syöttämiseen. Alapuolella olevat kentät lisäävät reseptiin halutun ainesosan: vasemmalta oikealle ainesosan nimi, ainesosaa tarvittava määrä, ja ainesosaan liittyvä mittayksikkö (valikko). Ainesosa lisätään painamalla 'Add new ingredient' -nappia. Erehdyksessä lisätyt ainesosat voidaan poistaa valitsemalla ne listasta ja painamalla 'Remove selected ingredient' -nappia.

Kun resepti on muokattu haluttuun muotoon, se voidaan tallentaa tietokantaan joko painamalla 'Add new recipe' -nappia (mikäli resepti on aivan uusi) tai 'Modify recipe' -nappia (mikäli muokataan olemassaolevaa reseptiä).
