# Lignes directrices pour contribuer

*Les pull requests, signalements de bug et toutes autres formes de contribution sont les bienvenues et hautement recommendées!*

> **Ce guide a pour but de donner des normes pour l'ensemble du projet afin de le rendre plus simple à lire et contribuer**

## 📖 Code de conduite
Merci de lire notre [Code de conduite](https://github.com/Margouta/PluginOpenMC/blob/main/CODE_OF_CONDUCT.md) pour assurer un moment convivial à tous les contributeurs

## 📥 Ouvrir une Issue
Avant de créer une issue, soyez sûr d'avoir la version la plus récente du plugin

## 🪲 Signalement de bugs et autres problèmes
La meilleure façon de contribuer sans coder est de partager les bugs
Si vous en rencontrez un, nous apprécierons un rapport bien écrit ❤️

Avant d'envoyer un ticket soyez sûr de:
- **Ne pas avoir créé un doublon !**
- **Utiliser des réactions**, si vous rencontrez le même problème qu'un ticket existant, utilisez une réaction 👍 au lieu d'écrire un commentaire (sauf si votre commentaire ajoute des détails)
- **Remplire completement le template**, le template a été écrit pour simplifier le travail des contributeurs, merci de leurs faciliter la vie

## 🔁 Envoyer une Pull Request
Avant de forker le repo et créer une pull request, assurez vous que les modifications que vous souhaitez apporter ne sont pas déjà en cours de développement. Dans ce cas, voyez avec le premier auteur pour collaborer !

*Note: Toutes les contributions auront la license GPL 3.0*

- **Plus petit, mieux c'est**. Envoyer **une seule** pull request par bugfix ou fonctionnalité, - **Ne pas** changer du code qui n'est pas lié à votre changement, C'est mieux de faire plein de petites PR plutot qu'une grande, Les grandes pull requests mettent du temps à être approuvées et peuvent être rejettées pour cette raison.
- **Soyez coordonnés**, pour éviter de travailler sur la même chose en parallèle coordonnez vous (en publique) sur qui écrit quoi
- **Suivez les conventions** de code existantes, suivre les conventions permet d'avoir un code plus facile à lire et à corriger
- **[Corriger tous les conflits](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/addressing-merge-conflicts/resolving-a-merge-conflict-on-github)**
- Quand vous écrivez des commentaires, écrivez des phrases construites, sans argot

Il est préférable qu'une PR sois merge par quelqu'un d'autre que son auteur
Avant de merge je m'assure que le code se compile et s'éxécute sans problèmes, ni erreur

## 📝 Ecrire un message de commit
S'il vous plaît, [écrivez de bons messages de commits](https://cbea.ms/git-commit/)
1. Limitez le sujet à 50 charactères
2. Utilsez l'imperatif (example: "Corrige un bug avec les pommes")
3. Ajoutez un tag si possible ([Docs], [Bug], [Features]...)
4. Ecrivez des descriptions complètes
5. Ne dépassez pas les 72 charactères en largeur

aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa (72 chars)

```
[BUG] Corrige le bug avec les pommes

Ecrivez une description complète, séparez les paragraphes par une
ligne blanche, expliquez le problème, avec du contexte si nécessaire,
focusez-vous sur comment vous l'avez résolu, l'objectif est d'écrire
un patch que les reviewers et le futur vous pourrez lire et expliquer

Notez les issues qui sont résolues via ce patch
Résous: #123
Voir aussi: #456, #789
```

## ✅ Examiner le code
- **Examinez le code, pas l'auteur**, Donnez des critiques constructives

## 💅 Style de code
Les conventions, d'après [cette page](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html) sur le site d'Oracle, sont les suivates :  
Les noms doivent être en anglais, peu importe la chose nommée.  
| Chose nommée | Règle | Exemple |
|--------------|-------|---------|
| Package | Toujours en miniscules et tous les mots sont collés sans caractère entre (pas d'`_`). Le nom doit être simple et sans caractères spéciaux (uniquement les caractères ASCII, donc pas d'accents). Le nom du package doit au possible rester un seul mot. | `fr.communaywen.commands` |
| Classes, Interfaces, Enums, Records, Annotations | Toujours en PascalCase. Utilisez uniquement des noms. Évitez les acronymes ou abréviations. | `class SpawnManager;` |
| Methodes | Son nom commence par un verbe. En `camelCase` | `getInstance()` `clone()` |
| Variables | Toujours en `camelCase`, évitez les abréviations et les noms raccourcis comme `nbr`. Pas d'`_` ou de `$` au début. L'utililité de la variable devrait être compréhensible en regardant le nom. | `int numberOfPlayers` `String playerName` |
| Constantes et membres d'enums | Les constantes dont la déclaration commence par `static final` sont nommées en UPPER_SNAKE_CASE, avec comme pour les variables, un nom qui décrit clairement leur utilité. | `static final int MIN_WIDTH = 4;` |
