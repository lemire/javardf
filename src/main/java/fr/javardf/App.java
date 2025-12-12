package fr.javardf;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Application principale en français.
 * Usage: java -jar target/javardf-0.1.0-SNAPSHOT-jar-with-dependencies.jar <requete.sparql> <data.rdf>
 */
public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar javardf.jar <requete.sparql> <fichier.rdf>");
            System.exit(1);
        }

        Path queryPath = Paths.get(args[0]);
        Path rdfPath = Paths.get(args[1]);

        if (!Files.exists(queryPath)) {
            System.err.println("Fichier de requête introuvable: " + queryPath);
            System.exit(2);
        }
        if (!Files.exists(rdfPath)) {
            System.err.println("Fichier RDF introuvable: " + rdfPath);
            System.exit(3);
        }

        try {
            String sparql = new String(Files.readAllBytes(queryPath), java.nio.charset.StandardCharsets.UTF_8);

            Model model = ModelFactory.createDefaultModel();
            try (InputStream in = Files.newInputStream(rdfPath)) {
                model.read(in, null); // Jena détecte RDF/XML automatiquement
            }

            Query query = QueryFactory.create(sparql);
            try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                if (query.isSelectType()) {
                    ResultSet rs = qexec.execSelect();
                    // Utiliser ResultSetFormatter pour supporter les différents formats (texte, CSV, JSON...)
                    org.apache.jena.query.ResultSetFormatter.out(System.out, rs, query);
                } else if (query.isAskType()) {
                    boolean b = qexec.execAsk();
                    System.out.println(b);
                } else if (query.isConstructType()) {
                    Model m2 = qexec.execConstruct();
                    m2.write(System.out, "TTL");
                } else if (query.isDescribeType()) {
                    Model m2 = qexec.execDescribe();
                    m2.write(System.out, "TTL");
                } else {
                    System.err.println("Type de requête non supporté.");
                }
            }

        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(4);
        }
    }
}
