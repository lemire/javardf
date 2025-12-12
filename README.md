# JavaRDF — exécuteur SPARQL

Ce petit projet Java (Maven) lit une requête SPARQL depuis un fichier et un jeu de données RDF au format RDF/XML depuis un fichier XML, exécute la requête et affiche le résultat dans la console.

Prérequis
- Java 11 ou supérieur
- Maven

## Construction

```bash
mvn package
```

Le build produit un JAR exécutable avec dépendances :
`target/javardf-0.1.0-SNAPSHOT-jar-with-dependencies.jar`

## Utilisation

```bash
java -jar target/javardf-0.1.0-SNAPSHOT-jar-with-dependencies.jar examples/query.sparql examples/data.rdf
```

Paramètres :
- premier argument : chemin vers la requête SPARQL (fichier texte)
- second argument : chemin vers le fichier RDF au format RDF/XML

Comportement
- Requêtes `SELECT` : affichage tabulaire des résultats
- Requêtes `ASK` : affichage `true` ou `false`
- Requêtes `CONSTRUCT` / `DESCRIBE` : affichage du graphe en `Turtle`

Exemples
Un exemple de requête et de fichier RDF sont fournis dans `examples/`.

Description des fichiers d'exemple
- `examples/query.sparql` : requête SPARQL simple qui sélectionne toutes les triplets du graphe, limitée à 10 résultats.
	Contenu :
	```sparql
	PREFIX ex: <http://example.org/>
	SELECT ?s ?p ?o WHERE { ?s ?p ?o } LIMIT 10
	```
- `examples/data.rdf` : un petit jeu de données au format RDF/XML contenant une ressource `http://example.org/subject1` avec la propriété `http://example.org/prop` et la valeur littérale `Valeur`.
	Contenu :
	```xml
	<?xml version="1.0"?>
	<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
					 xmlns:ex="http://example.org/">
		<rdf:Description rdf:about="http://example.org/subject1">
			<ex:prop>Valeur</ex:prop>
		</rdf:Description>
	</rdf:RDF>
	```

Résultat attendu
Lorsque vous exécutez l'application avec ces fichiers d'exemple, la sortie console pour la requête `SELECT` ressemble à un tableau formaté. Exemple observé :

```
------------------------------------
| s           | p       | o        |
====================================
| ex:subject1 | ex:prop | "Valeur" |
------------------------------------
```

Ce format provient de l'utilisation de `ResultSetFormatter` d'Apache Jena ; d'autres formats (CSV, TSV, JSON) peuvent être proposés selon les besoins.

L
