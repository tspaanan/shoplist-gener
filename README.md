# SHOPLIST-GENER

Sovelluksella voi luoda siihen tallennettujen reseptien pohjalta satunnaisen ruokalistan koko viikoksi. Tämän jälkeen sovellus luo kauppalistan ruokalajien ainesosista. Sovelluksen tuntemia reseptejä voi muokata tai poistaa, ja uusia reseptejä voi lisätä.

## Dokumentaatio

[Linkki harjoitustyön vaatimusmäärittelyyn](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/vaatimusmaarittely.md)

[Linkki harjoitustyön työaikakirjanpitoon](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/ty%C3%B6aikakirjanpito.md)

[Linkki harjoitustyön arkkitehtuurikuvaukseen](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/arkkitehtuuri.md)

[Linkki harjoitustyön käyttöohjeeseen](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/kayttoohje.md)

## Releaset

[viikko5](https://github.com/tspaanan/shoplist-gener/releases/tag/viikko5)
[viikko6](https://github.com/tspaanan/shoplist-gener/releases/tag/viikko6)

## Ohjelman suoritus

```
mvn compile exec:java -Dexec.mainClass=shoplistgener.App
```
**Vaihtoehtoisesti:**

jar-paketin generointi:
```
mvn package
```
jar-paketin ajaminen:
```
java -jar target/shoplistgener-2.0-SNAPSHOT.jar
```

## Ohjelman testaaminen
Automaattiset testit:
```
mvn test
```
Jacoco-raportin luominen:
```
mvn test jacoco:report
```
Checkstyle-raportin luominen:
```
mvn jxr:jxr checkstyle:checkstyle
```

## Ohjelman dokumentaatio
JavaDoc-dokumentaation luonti:
```
mvn javadoc:javadoc
```
