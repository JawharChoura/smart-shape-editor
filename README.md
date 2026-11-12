# JavaFX Shape Drawer - Mini Projet Universitaire

Application JavaFX complete pour dessiner des formes geometriques avec implementation des design patterns:

- Factory Pattern
- Command Pattern
- Strategy Pattern
- Observer Pattern
- DAO Pattern

## Architecture des packages

- `app` : point d'entree JavaFX
- `controller` : logique d'interface
- `model` : etat applicatif
- `shapes` : abstraction et implementations des formes
- `factory` : creation des formes
- `command` : commandes + undo
- `strategy` : strategies de journalisation
- `observer` : notification automatique des actions
- `dao` : sauvegarde/chargement SQLite
- `utils` : utilitaires techniques

## Prerequis

- Java 17+
- Maven 3.8+

## Lancer avec Maven

```bash
mvn clean javafx:run
```

## Fonctionnalites

- Palette de formes: Rectangle, Cercle, Ligne
- Dessin via souris sur Canvas
- Selection d'une forme par clic
- Suppression de la forme selectionnee
- Undo de la derniere action
- Sauvegarde dans SQLite (table `shapes`)
- Chargement depuis SQLite
- Journalisation des actions:
  - Console
  - Fichier (`logs/actions.log`)
  - Base de donnees (table `logs`)

## Base de donnees

Le fichier SQLite est cree automatiquement au premier lancement:

- `shapes.db`

Tables:

- `shapes(id, type, x, y, width, height)`
- `logs(id, timestamp, message)`
