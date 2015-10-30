/*
 * Licensed to Neo Technology under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Neo Technology licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stulsoft.yscdcatalogue;

import java.io.File;
import java.io.IOException;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.tooling.GlobalGraphOperations;

public class EmbeddedNeo4j {
	private static final String DB_PATH = "d:/work/target/neo4j-hello-db";

	public String greeting;

	// START SNIPPET: vars
	GraphDatabaseService graphDb;
	Node firstNode;
	Node secondNode;
	Relationship relationship;
	// END SNIPPET: vars

	// START SNIPPET: createReltype
	private static enum RelTypes implements RelationshipType {
		KNOWS
	}
	// END SNIPPET: createReltype

	public static void main(final String[] args) throws IOException {
		EmbeddedNeo4j hello = new EmbeddedNeo4j();
		hello.createDb();
		hello.removeData();
		hello.shutDown();
	}

	void createDb() throws IOException {
		FileUtils.deleteRecursively(new File(DB_PATH));

		// START SNIPPET: startDb
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
		registerShutdownHook(graphDb);
		// END SNIPPET: startDb

		// START SNIPPET: transaction
		try (Transaction tx = graphDb.beginTx()) {
			// Database operations go here
			// END SNIPPET: transaction
			// START SNIPPET: addData
			firstNode = graphDb.createNode();
			firstNode.setProperty("message", "Hello, ");
			secondNode = graphDb.createNode();
			secondNode.setProperty("message", "World!");

			relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
			relationship.setProperty("message", "brave Neo4j ");

			// END SNIPPET: addData

			// START SNIPPET: readData
			System.out.print(firstNode.getProperty("message"));
			System.out.print(relationship.getProperty("message"));
			System.out.print(secondNode.getProperty("message"));
			// END SNIPPET: readData

			greeting = ((String) firstNode.getProperty("message")) + ((String) relationship.getProperty("message")) + ((String) secondNode.getProperty("message"));

			// START SNIPPET: transaction

			// YS
			System.out.println();
			System.out.println("YS start");
			Label firstNodeLabel = DynamicLabel.label("the first node"); 
			firstNode.addLabel(firstNodeLabel);
			secondNode = graphDb.createNode();
			secondNode.setProperty("message", "It is from Yuriy!");
			relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
			relationship.setProperty("message", "link to Yuriy");

			firstNode.getRelationships().forEach(r -> System.out.println(r.getProperty("message") + " " + r.getEndNode().getProperty("message")));
			
			System.out.println();
			GlobalGraphOperations ggops = GlobalGraphOperations.at(graphDb);
			ggops.getAllNodes().forEach(n-> System.out.println(n.getProperty("message")));
			
			System.out.println("\nlabels");
			graphDb.findNodes(firstNodeLabel).forEachRemaining(n -> System.out.println(n.getProperty("message")));

			System.out.println("\nRelationships");
			ggops.getAllRelationships().forEach(r -> System.out.println("Relationship: " + r.getProperty("message")));
			System.out.println("YS end");
			// YS
			tx.success();
		}
		// END SNIPPET: transaction
	}

	void removeData() {
		try (Transaction tx = graphDb.beginTx()) {
			// START SNIPPET: removingData
			// let's remove the data
			// firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();
			// firstNode.delete();
			// secondNode.delete();

			firstNode.getRelationships().forEach(r -> {
				r.getEndNode().delete();
				r.delete();
			});
			firstNode.delete();
			// END SNIPPET: removingData

			tx.success();
		}
	}

	void shutDown() {
		System.out.println();
		System.out.println("Shutting down database ...");
		// START SNIPPET: shutdownServer
		graphDb.shutdown();
		// END SNIPPET: shutdownServer
	}

	// START SNIPPET: shutdownHook
	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		});
	}
	// END SNIPPET: shutdownHook
}