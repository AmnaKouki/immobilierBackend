# ImmobilierBackend
This is the backend part of the project **Immobilier**.

## Requirements

For building and running the application you need:
- Java 21
- Maven 3.2.1

## DataBase  Configuration
**MongoDB** is used in this project.
You need to create a database with the name `immobilier`.

The structure of the data base should be like this: 
```
immobilier
    |___annonces
    |___users
    |___roles
```
## Create admin acount

To create an account as administrator, you need to do this :

-  `POST` request to `/api/auth/signup` with this body :
``` json
{
    "username" : "username",
    "password" : "123456",
    "roles": [
        "admin"
    ]
}
```
*Password must contain at least 6  characters.*  

## Annonce
### get all annonces:
-`GET` request to `/api/annonces`

###  add new annonce:
- `POST` request to `/api/annonces/add` with the body:
``` json
{
    "type": "Vente",
    "categorieImmo": "Maison",
    "titre": "Maison  à vendre en centre ville",
    "description": "Description de  la maison.",
    "surface": 100,
    "prix": 100,
    "nbPieces": 2,
    "caracteristiques": 
        [
            "Parking",
            "Climatisation",
            "Meublé"
        ],
    "photos": [],
    "region": "Tunisie",
    "adresse": "adresse",
    "contact": {
        "email": "demo@demo.com",
        "telephone": "000000000"
    }
}
```
Note: Photos are added to the annonce seperately. To add a picture to an existing annonce, you have to do this :

- `POST` request to ``/api/annonces/uploadImage/{{annonce_id}}`` with the request param  *`image`* and the file as value.

