# Arkkitehtuurikuvaus

## Rakenne

Sovellus seuraa kurssilla käytetyn referenssisovelluksen kolmitasoista kerrosarkkitehtuuria:

<img src="https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/Pakkauskaavio.jpg">

Pakkauksessa *shoplistgener.ui* sijaitsee kaikki käyttöliittymään liittyvä koodi. Sovelluksen graafinen käyttöliittymä on toteutettu JavaFX-kirjastolla. Pakkauksessa *shoplistgener.domain* löytyy kaikki sovelluslogiikan toiminnallisuus. Pakkauksessa *shoplistgener.dao* on sovelluksen käyttämien tietojen pysyväistallenukseen tarvittava koodi.

## Käyttöliittymä

Sovelluksen käyttöliittymä tarjoaa neljä näkymää:

* ruokalistan ja kauppalistan generoimiseen tarkoitettu näkymä
* tietokannassa olevien reseptien tarkasteluun tarkoitettu näkymä
* uusien reseptien luomiseen tai tietokannassa olevien reseptien muokkaamiseen tarkoitettu näkymä
* keittiöstä jo valmiina löytyvien ainesosien hallintaan tarkoitettu näkymä

Käyttöliittymässä vasemmalla sijaitsevat neljä nappia vaihtavat näiden näkymien välillä. Jokainen vaihtuva näkymä on toteutettu HBox-oliona, jossa erilaiset kontrollielementit on aseteltu rinnakkain (ja osin allekkain).

Käyttöliittymä on eriytetty sovelluslogiikasta.

## Sovelluslogiikka

Rakenteessa yläpuolella mainittu kolmitasoisuus on nähtävissä myös seuraavassa luokkakaaviossa:

<img src="https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/Luokkakaavio.jpg">

ShoplistgenerService-luokka välittää dataa käyttäjälle näkyvästä graafisesta käyttöliittymästä vastaavan UIJavaFX-luokan ja tietojen pysyväistallennuksesta vastaavan ShoplistgenerDAO-rajapinnan toteuttavan luokan välillä.

Sovelluksen ytimen muodostaa reseptin käsite. Resepti kuvaa kunkin ruokalajin nimen, valmistusohjeiden ja valmistuksessa tarvittavien ainesosien kokonaisuutta. Sovelluksessa käsitellään ensisijaisesti reseptejä tai niiden osia. ShoplistgenerService-luokka tarjoaa metodit reseptien ja niiden osien luomiseen, muokkaamiseen ja poistamiseen. Näitä metodeja ovat mm. seuraavat:

* void addRecipe(List\<String> recipeParts, List\<String> ingredientParts)
* void modifyRecipe(List\<String> recipeParts, List\<String> ingredientParts)
* List\<String> fetchRecipeList(String name)
* void removeRecipe(String name)

Reseptejä voidaan käsitellä myös laajempina kokonaisuuksina, erityisesti useamman päivän ruokalajien muodostamina menuina, ja niiden valmistamisessa tarvittavia ainesosia listaavina kauppalistoina:

* String fetchCourses(int numberOfCourses)
* String fetchShoppingList()

Reseptien ohella toisen keskeisen kokonaisuuden muodostavat ainesosat, joita tarvitaan reseptien kuvaamien ruokalajien valmistamiseen. Ainesosille ShoplistgenerService-luokka tarjoaa mm. seuraavia metodeja:

* String fetchKitchenIngredients()
* String subtractKitchenIngredients()
* void removeIngredientFromKitchen(String name)
* void upgradeIngredientQuantityInKitchen(String name, Integer quantity)

Lisäksi Ingredient-luokka käsittelee ainesosia mm. seuraavien staattisten metodien kautta:

* Ingredient combineIngredients(Ingredient first, Ingredient second)
* List\<Ingredient> sortIngredients(List\<Ingredient> ingredients)
* List\<Ingredient> subtractIngredients(List\<Ingredient> original, List\<Ingredient> subtract)

## Pysyväistallennus levylle

shoplistgener.dao -paketti sisältää *Data Access Object* -periaatteella suunnitellun toiminnallisuuden ohjelman hyödyntämien tietojen pysyväistallennukseen levylle. Sovelluksessa on tällä hetkellä valmiina yksi ShoplistgenerDAO-rajapinnan toteuttava luokka, ShoplistgenerDAOsqlite, joka hyödyntää SQLite-nimistä tietokannan hallintajärjestelmää datan tallennukseen.

SQLite on relaatiomallisella SQL-kielellä operoitava tietokannan hallintajärjestelmä, jonka käyttämä SQL-skeema on koottu tiedostoon [schema.sql](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/schema.sql).

Sovelluksen tarvitsemat konfiguraatiot tallennetaan erilliseen konfiguraatiotiedostoon: *config.properties*. Tällä hetkellä konfiguraatiotiedostossa tallennetaan tietoa ohjelman käyttämän tietokantatiedoston nimestä.

Konfiguraatiotiedoston formaatti on seuraava:
```
databaseName=<tietokannan nimi>
```

Sovellus tarkistaa automaattisesti käynnistyksen yhteydessä tietokantatiedoston olemassaolon, ja tarvittaessa ohjeistaa käyttäjää valitsemaan tietokantatiedostolle haluamansa nimen.

## Toiminnallisuus

Monimutkaisin yksittäinen toiminnallisuus toteutuu, kun käyttäjä painaa ruokalistan ja kauppalistan generoimiseen tarkoitetussa näkymässä nappia, jossa on teksti "Print Week's Menu and shopping list":

<img src="https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/Sekvenssikaavio.png">

Sekvenssikaaviossa kuvataan ensiksi ohjelman käynnistäessä luotujen UIContructChangingView, ShoplistgenerDAO ja ShoplistgenerService -olioiden luontia. Nämä oliot vastaavat vaihtuvien näkymien luomisesta, tietojen tallentamisesta ja sovelluslogiikan toteuttamisesta.

Napin painaminen kutsuu sovelluslogiikan fetchCourses() -metodia, joka välittää pyynnön edelleen DAO-olion fetchMenu() -metodille parametrilla 7 (viikossa on seitsemän päivää). DAO-olio palauttaa parametrin ilmaiseman määrän reseptejä listassa, jotka sovelluslogiikka purkaa käyttäjälle näytettävään merkkijonomuotoon ja lähettää edelleen käyttöliittymälle.

Seuraavat kaksi käyttöliittymän pyyntöä koskevat kahden eri ostoslistan luomista. Ensimmäisenä sovelluslogiikalta pyydetään äsken välitettyjen reseptien kaikkia ainesosia metodilla fetchShoppingList(). Sovelluslogiikka hyödyntää listan järjestämisessä Ingredient-luokan staattista metodia sortIngredients(), joka saa parametrikseen kaikki viikon reseptien sisältämät ainesosat. Ingredient-luokan palauttama järjestetty lista puretaan käyttöliittymän ymmärtämään muotoon ja palautetaan.

Toisena sovelluslogiikkaa pyydetään poistamaan ostoslistalta kaikki ne ainesosat, jotka sovellus tietää jo olevan keittiössä. Tämä tapahtuu kutsumalla sovelluslogiikan subtractKitchenIngredients() -metodia. Sovelluslogiikka kutsuu DAO-olion fetchKitchenIngredients() -metodia saadakseen ajantasaisen listan keittiön kaappien sisällöstä, ja saa palautuksena kitchenIngredients-nimisen listan ainesosista. Ingredient-luokan staattiselle metodille annetaan parametrina alkuperäinen ostoslista ja tuore keittiön ainesosat sisältävä lista, palautuksena saadaan lista ainesosista, joita keittiöstä ei vielä löydy. Sovelluslogiikka purkaa tämänkin lista käyttöliittymän näytettäväksi ja palauttaa sen.

Viimeisenä käyttöliittymä kutsuu UIContructChangingView-oliota asettamaan tiedot ruokalistan ja kauppalista generoimiseen tarkoitettuun näkymään.

Kaikki ohjelman toiminnallisuus seuraa tätä mallia: käyttöliittymä kutsuu sovelluslogiikkaa toteuttamaan käyttäjän antaman ohjeistuksen, sovelluslogiikka kutsuu DAO-oliota tarvittavien tietojen lukemiseen ja muuttuneiden tietojen tallentamiseen levylle, ja lopuksi purkaa palautetulosteen sellaiseen muotoon, jonka käyttöliittymä voi esittää takaisin käyttäjälle.