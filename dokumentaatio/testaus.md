# Testausdokumentti

Sovelluksen testaus on toteutettu automatisoitujen yksikkötestien ja integraatiotestien avulla JUnit-kirjaston avulla. Lisäksi sovellusta on testattu manuaalisesti kaikkien sen järjestelmien toiminnan varmistamiseksi.

## Automatisoitu yksikkö- ja integraatiotestaus

Automatisoitujen testausten rivikattavuus on 82% ja haarautumakattavuus 77%:

<img src="https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/testauskattavuus.png">

Testin ulkopuolelle jäivät erityisesti sellaiset haaraumat, joita esiintyi yksinkertaisia ja suoraviivaisia toimenpiteitä suorittavissa metodeissa.

Erityisen tarkasti testattiin sellaisia metodeja, joiden puutteellinen toiminta johtaisi sovelluksen näyttämään käyttäjälle harhaanjohtavaa informaatiota. Esimerkiksi Ingredient-luokan tarjoamat staattisten metodien combineIngredients(Ingredient, Ingredient), sortIngredients(List\<Ingredient>) ja subtractIngredients(List\<Ingredient>, List\<Ingredient>) toiminta varmistettiin tarkoin. Näitä metodeja testattiin sekä suoraan yksikkötesteillä että laajempien toiminnallisuuksien osana integraatiotesteillä.

## Manuaalinen järjestelmätestaus

Sovelluksen kaikki määrittelydokumentissa kuvattu toiminnallisuus sekä oikeiden että virheellisten syötteiden osalta on varmistettu manuaalisella järjestelmätestauksella.

Sovellusta on testattu lukuisissa Linux-työpöytäympäristöissä sekä Windows 10 -käyttöjärjestelmässä.

## Laatuongelmat

Sovellus käyttää tietojen pysyväistallennukseen SQLite-ohjelmistoa Javan JDBC-ajurin kautta. Tämän vuoksi sovellus antaa geneerisen tietokannan lukittumisesta kertovan virheilmoituksen, mikäli sitä yritetään ajaa kansiossa, joka on symbolinen linkki.