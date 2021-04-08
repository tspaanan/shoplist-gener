# SHOPLIST-GENER

## Dokumentaatio

[Linkki harjoitustyön vaatimusmäärittelyyn](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/vaatimusmaarittely.md)

[Linkki harjoitustyön työaikakirjanpitoon](https://github.com/tspaanan/shoplist-gener/blob/main/dokumentaatio/ty%C3%B6aikakirjanpito.md)

## Ohjelman suoritus

```mvn compile exec:java -Dexec.mainClass=defaultPackage.App```

## Ohjelman testaaminen
Automaattiset testit:
```
mvn test
```
Jacoco-raportin luominen:
```
mvn test jacoco:report
```