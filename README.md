# SHOPLIST-GENER

## Dokumentaatio

[Linkki harjoitustyön vaatimusmäärittelyyn](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/vaatimusmaarittely.md)

[Linkki harjoitustyön työaikakirjanpitoon](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/ty%C3%B6aikakirjanpito.md)

[Linkki harjoitustyön arkkitehtuurikuvaukseen](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/arkkitehtuuri.md)

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
java -jar target/shoplistgener-1.0-SNAPSHOT.jar
```
huom! jar-paketin voi ajaa ainoastaan, mikäli cwd (current working directory) on tämän kloonatun projektin juuressa!

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
