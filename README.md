# depot-projet-Java


Ce projet Java a pour but de créer une application de clavardage. 
Pour résumer le cahier des charges, cette application doit permettre l'échange de messages entre plusieurs instances sur des machines différentes. Elle doit également prendre en charge la gestion de pseudo et d'utilisateur connecté à un instant t. De plus, elle doit être muni d'une interface graphique et d'une base de données collectant ainsi les différents messages échangés.

Afin de compiler et d'exécuter le programme, il faut exécuter la commande suivante :
```sh
mvn compile && mvn -X exec:java -Dexec.mainclass="userInterface.LaunchGUI"
```

Ce programme ne requiert pas de fichiers externes ni de manipulations manuelles des dépendances.
