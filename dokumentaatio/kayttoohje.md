# Käyttöohje

Sovelluksen viimeisin julkaisu jar-tiedostona voidaan ladata [täältä](https://github.com/tspaanan/shoplist-gener/releases/tag/lopullinenpalautus).

## Konfigurointi

Ohjelma konfiguroi itse itsensä ja pyytää tarvittaessa käyttäjältä halutun tietokantatiedoston nimen. Tietokantatiedoston nimi tallennetaan config.properties -tiedostoon.

Sovellus tiedustelee käyttäjältä, täytetäänkö tyhjä tietokanta testidatalla vai jätetäänkö se tyhjäksi käyttäjän syöttämiä reseptejä odottamaan.

## Käynnistäminen

Ohjelman voi ajaa komentoriviltä seuraavalla komennolla:
```
java -jar shoplistgener-3.0-SNAPSHOT.jar
```

## Käyttäminen

Ohjelma avautuu tyhjään näkymään. Vasemmalla olevat neljä nappia vaihtavat neljän eri näyttötilan välillä. Eri tilat näytetään ikkunan oikealla puolella.

### Viikkomenun ja kauppalistan luominen ja tarkastelu

'View Menu' avaa ruokalistan ja kauppalista generoimiseen tarkoitetun näkymän. 'Print Week's Menu and shopping list' arpoo automaattisesti seitsemän ruokalajin menun ja tulostaa näytölle sen vaatimien ainesosien pohjalta kaksi eri kauppalistaa. Ylempi kauppalista sisältää kaikki ruokalajien ainesosat järjestyksessä. Alemmassa kauppalistassa siitä on poistettu kaikki keittiöstä jo löytyvät ainesosat. Menun arpomisen jälkeen käyttäjä voi vaihtaa haluamansa ruokalajin satunnaiseen uuteen tai itse valitsemaansa ruokalajiin. Kauppalistat päivittyvät jokaisen valinnan jälkeen automaattisesti. Käyttäjä voi myös tarkastella valitun ruokalajin reseptiä.

### Reseptien tarkastelu

'View recipes' tuo näkyviin kaikki tietokantaan tallennetut reseptit. Käyttäjä voi joko valita haluamansa reseptin listasta tai kirjoittaa sen nimen tekstikenttään. Molemmille hakutavoille löytyy oma nappinsa ruudun keskeltä. Kun käyttäjä on tarkastellut reseptiä, ruudun oikealta laidalta löytyvät toiminnot uuden reseptin lisäämiseen, tarkasteltavan reseptin muokkaamiseen ja tarkasteltavan reseptin poistamiseen.

### Reseptien muokkaaminen ja lisääminen

'Add new recipe' tuo näkyviin reseptien muokkaamiseen tarkoitetut kentät. Ylhäällä ovat kentät reseptin nimen (vasemmalla) ja reseptin valmistusohjeiden (oikealla) syöttämiseen. Alapuolella olevat kentät lisäävät reseptiin halutun ainesosan: vasemmalta oikealle ainesosan nimi, ainesosaa tarvittava määrä, ja ainesosaan liittyvä mittayksikkö (valikko). Ainesosa lisätään painamalla 'Add new ingredient' -nappia. Erehdyksessä lisätyt ainesosat voidaan poistaa valitsemalla ne listasta ja painamalla 'Remove selected ingredient' -nappia.

Kun resepti on muokattu haluttuun muotoon, se voidaan tallentaa tietokantaan joko painamalla 'Add new recipe' -nappia (mikäli resepti on aivan uusi) tai 'Modify recipe' -nappia (mikäli muokataan olemassaolevaa reseptiä).

### Keittiön kaappien sisältämien ainesosien hallinta

'View ingredients in kitchen' avaa näkymän keittiöstä löytyvien ainesosien hallintaan. Vasemmalla on listattu kaikki tietokannasta löytyvät ainesosat, joita voidaan lisätä keittiöön haluttu ainesosa valitsemalla ja painamalla 'Add selected ingredient' -nappia. Oikealla on listattu kaikki keittiöstä löytyvät ainesosat ja niiden määrät. Valitsemalla oikeanpuoleisesta listasta ainesosat, voidaan se joko poistaa kokonaan 'Remove selected ingredient' -napilla, tai päivittää sen yksikkömäärää 'Update ingredient quantity' -napilla.