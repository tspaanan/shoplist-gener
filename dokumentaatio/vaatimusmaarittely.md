# Vaatimusmäärittely

## Sovelluksen tarkoitus

Sovellus luo siihen tallennettujen reseptien pohjalta satunnaisen ruokalistan koko viikoksi. Tämän jälkeen sovellus luo kauppalistan niistä ainesosista, joita keittiöstä ei jo löydy.

## Käyttäjät

Sovelluksella on vain yksi käyttäjärooli. _Normaali käyttäjä_ voi valita tallennukseen käytettävän tietokantatiedoston nimen.

## Toiminnallisuus

- käyttäjä voi napin painalluksella luoda valmiin ruokalistan koko viikoksi, ja siihen kuuluvan kauppalistan
	- kauppalista tarjoillaan kahtena versiona: reseptien perusteella muodostettu kauppalista, ja reseptien ja keittiöstä jo löytyvien ainesosien pohjalta muodostettu kauppalista
	- käyttäjä voi myös arpoa tai valita halutuille päiville uudet ruokalajit: kauppalista päivittyvät automaattisesti
	- ruokalista arpoo seitsemän ruokalajia, koska logiikka on seuraava: edellisen päivän päivällisen tähteet on tarkoitus syödä seuraavan päivän lounaaksi :)

- käyttäjä voi tarkastella ruokalistaan kuuluvien ruokalajien reseptejä

- käyttäjä voi lisätä tai poistaa reseptejä tietokannasta, tai etsiä reseptejä nimellä

- käyttäjä voi muokata tietokannassa olevia reseptejä

- käyttäjä voi päivittää keittiöstä jo valmiiksi löytyvien ainesosien yksikkömääriä tai poistaa niitä

## Jatkokehitysideoita

- kauppalistat on mahdollista tulostaa automaattisesti tai lähettää esim. Telegramilla kännykkään

- käyttäjä voi hakea uusia reseptejä internetistä ja lisätä niitä sovelluksen käyttämään tietokantaan

- ruokalistan luomista voi konfiguroida tarkemmin

- sovellus ymmärtäisi esimerkiksi sen, että jos ruokalajiin vaaditaan vain puoli purkillista ranskankermaa, täytyy seuraavana päivänä olla ruokalistassa sellainen ruokalaji, johon sen toisen puolikkaan voi käyttää
