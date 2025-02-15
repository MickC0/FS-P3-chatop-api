# Chatop API

## Description
Chatop API est le backend d'une application de gestion locative. Il permet la gestion des utilisateurs, des locations et des messages entre utilisateurs.

## Technologies utilisées
![Static Badge](https://img.shields.io/badge/Java-21-orange)
![Static Badge](https://img.shields.io/badge/Maven-3.9.9-purple)
![Static Badge](https://img.shields.io/badge/Spring_Boot-3.4.2-green)
![Static Badge](https://img.shields.io/badge/MySQL-8.5-darkblue)

- **Langage** : Java 21
- **Framework** : Spring Boot 3.4.2
- **Base de données** : MySQL 8.5
- **Authentification** : JWT (JSON Web Token)
- **Documentation API** : Swagger

## Installation et configuration

### Prérequis
- Java 21
- Maven
- MySQL 8.5
- Postman (optionnel, pour tester l'API)

### Installation
```sh
# Cloner le projet
git clone <repository_url>
cd chatop-api

# Configurer la base de données
# Créer une base de données MySQL : chatop_db
# Exécuter le script SQL fourni
mysql -u root -p chatop_db < script.sql

# Modifier application.properties avec les informations de connexion à la base de données

# Lancer l'application
mvn spring-boot:run
```

## Endpoints disponibles
L'API suit les spécifications fournies dans la collection Postman.

- **Authentification** :
    - `POST /api/auth/register`
    - `POST /api/auth/login`
    - `GET /api/auth/me`
- **Locations** :
    - `GET /api/rentals`
    - `GET /api/rentals/{id}`
    - `POST /api/rentals`
    - `PUT /api/rentals/{id}`
- **Messages** :
    - `POST /api/messages`

## Sécurisation
Toutes les routes nécessitent une authentification (sauf l'inscription et la connexion). L'authentification se fait via JWT. Les tokens sont à inclure dans l'en-tête `Authorization` des requêtes.

## Gestion des images
Lors de la création d'une location, une image est requise. Elle est stockée sur le serveur et son URL est sauvegardée en base de données.

## Documentation API
Swagger est disponible à l'adresse suivante après lancement du projet :
```
http://localhost:3001/swagger-ui.html
```

## Tests
L'API peut être testée avec Postman en important la collection fournie (`rental.postman_collection.json`).

## Contribution
Les contributions sont les bienvenues. Forkez le projet et proposez une PR.

## Licence
Ce projet est sous licence MIT.

