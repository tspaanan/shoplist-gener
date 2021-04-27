# Vaatimusmäärittely

## Sovelluksen tarkoitus

Sovellus luo siihen tallennettujen reseptien pohjalta satunnaisen ruokalistan koko viikoksi. Tämän jälkeeen sovellus luo kauppalistan niistä ainesosista, joita kuivakaapista tai jääkaapista ei löydy. Sovelluksen satunnaisuutta on mahdollista räätälöidä, ja sen voi pyytää hakemaan internetistä yhden uuden yllätysreseptin, jolla viikon ruokalistaa monipuolistetaan.

## Käyttäjät

Sovelluksella on vain yksi käyttäjärooli. _Normaali käyttäjä_ voi muokata kaikkia sovelluksen osia monipuolisten asetusvaihtoehtojen avulla. **[tehty: viikko 5]** *[tarkennus: toteutettu tietokannan alustaminen ja nimen valinta]*

## Toiminnallisuus

- käyttäjä voi napin painalluksella luoda valmiin ruokalistan koko viikoksi, ja siihen kuuluvan kauppalistan
	- ruokalista ja kauppalista on myös muotoiltu tulostusvalmiiksi, tai ne voidaan lähettää pikaviestimellä kännykkään
	- käyttäjä voi myös arpoa tai valita halutuille päiville uudet ruokalajit **[tehty: viikko 5]** *[tarkennus: halutun reseptin valinta toteutettu viikolla 5]*

- käyttäjä voi tarkastella ruokalistaan kuuluvien ruokalajien reseptejä

- käyttäjä voi lisätä tai poistaa reseptejä tietokannasta, tai etsiä hakukriteerien mukaisia reseptejä (esimerkiksi antamalla kourallinen ainesosia löydetään reseptejä, joihin nämä ainesosat voidaan käyttää)

- käyttäjä voi muokata tietokannassa olevia reseptejä **[tehty: viikko 5]**

- käyttäjä voi lisätä resepteihin tageja, joita voidaan käyttää resepteihin liittyvissä hauissa sekä ruokalistan luomisprosessin muokkaamisessa

- käyttäjä voi päivittää sovelluksen tuntemia kuivakaapin ja jääkaapin sisältöjä

- käyttäjä voi muokata sovelluksen satunnaisuutta esimerkiksi rajaamalla tiettyjä reseptejä vain tietyille päiville (esimerkiksi tagilla 'pubiruoka' merkityt reseptit vain perjantain päivälliselle tai keittoja aina maanantain lounaalle) tai rajaamalla saman ruokalajin esiintymistä esimerkiksi vain yhteen kertaan kolmen viikon välein, tai rajaamalla esimerkiksi kalaruokien määrää vain kahteen kertaan viikossa

- käyttäjä voi hakea esimerkiksi plainoldrecipe-sivustolta yhden satunnaisen uuden reseptin, joka lisätään sovelluksen resepteihin ja seuraavan viikon ruokalistaan

## Jatkokehitysideoita

- käyttäjä voi hakea uusia reseptejä internetistä ja lisätä niitä sovelluksen käyttämään tietokantaan

- sovellus ymmärtäisi esimerkiksi sen, että jos ruokalajiin vaaditaan vain puoli purkillista ranskankermaa, täytyy seuraavana päivänä olla ruokalistassa sellainen ruokalaji, johon sen toisen puolikkaan voi käyttää
