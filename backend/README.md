# 🔧 Setup guide

> [!WARNING]\
> Please take note that Nadeo's API are subject to change, refer to the documentation.

* Clone this repo :  ```git git clone https://github.com/FrenchTheBeast/trackmaniamyrecords/ ```
* Use IntelliJ/Vscode/Eclipse and create a project with all thoses classes. **This will create a /out/ folder**
* Use any cloud environment and run the server.
* Create a second Ubisoft account that will handle all API calls.
* If you don't want to use an external database to call/store data : You can remove methods calling `DATABASE_URL` and `DATABASE_USER_TOKEN`


## 🔑 Environment Variables 

> [!IMPORTANT]\
> **IMPORTANT:** Setup thoses environment variables into your cloud environment.


```DATABASE_URL``` — API endpoint for your database <br>
```DATABASE_USER_TOKEN``` — Access token for your API <br>
```TRACKMANIA_CLIENT_ID``` — Trackmania Client ID (no need to make it a variable, since it's public anyway) <br>
```TRACKMANIA_CLIENT_SECRET``` — Trackmania Client Secret obtained from [api.trackmania.com](https://api.trackmania.com/) <br>
```UBI_EMAIL``` — Ubisoft email of the second account used for token requests <br>
```UBI_PASSWORD``` — Ubisoft password of the second account used for token requests <br>


---
