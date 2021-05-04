# Arkkitehtuurikuvaus

## Rakenne

Sovellus seuraa kurssilla käytetyn referenssisovelluksen kolmitasoista kerrosarkkitehtuuria:

<img src="https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/Pakkauskaavio.jpg">

Pakkauksessa *shoplistgener.ui* sijaitsee kaikki käyttöliittymään liittyvä koodi. Sovelluksen graafinen käyttöliittymä on toteutettu JavaFX-kirjastolla. Pakkauksessa *shoplistgener.domain* löytyy kaikki sovelluslogiikan toiminnallisuus. Pakkauksessa *shoplistgener.dao* on sovelluksen käyttämien tietojen pysyväistallenukseen tarvittava koodi.

## Sovelluslogiikka

Rakenteessa yläpuolella mainittu kolmitasoisuus on nähtävissä myös seuraavassa luokkakaaviossa:

<img src="https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/Luokkakaavio.jpg">

ShoplistgenerService-luokka välittää dataa käyttäjälle näkyvästä graafisesta käyttöliittymästä vastaavan UIJavaFX-luokan ja tietojen pysyväistallennuksesta vastaavan ShoplistgenerDAO-rajapinnan toteuttavan luokan välillä. Sovelluksessa on tällä hetkellä valmiina yksi ShoplistgenerDAO-rajapinnan toteuttava luokka, ShoplistgenerDAOsqlite, joka hyödyntää SQLite-nimistä tietokannan hallintajärjestelmää datan tallennukseen.

Sovelluksen ytimen muodostaa reseptin käsite. Resepti kuvaa kunkin ruokalajin nimen, valmistusohjeiden ja valmistuksessa tarvittavien ainesosien kokonaisuutta. Sovelluksessa käsitellään ensisijaisesti reseptejä tai niiden osia. ShoplistgenerService-luokka tarjoaa metodit reseptien ja niiden osien luomiseen, muokkaamiseen ja poistamiseen:

* void addRecipe(List<String> recipeParts, List<String> ingredientParts)
* void modifyRecipe(List<String> recipeParts, List<String> ingredientParts)
* List<String> fetchRecipe(String name)
* void removeRecipe(String name)

Reseptejä voidaan käsitellä myös laajempina kokonaisuuksina, erityisesti useamman päivän ruokalajien muodostamina menuina, ja niiden valmistamisessa tarvittavia ainesosia listaavina kauppalistoina:

* String fetchCourses(int numberOfCourses)
* String fetchShoppingList()

## Toiminnallisuus

### Sekvenssikaavio
<img src="https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/Sekvenssikaavio.png">

