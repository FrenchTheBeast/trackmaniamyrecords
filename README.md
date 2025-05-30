<p align="center">
 <img width="300px" src="https://cdn2.steamgriddb.com/logo/369a226ba5264d149663022a90786d1f.png" align="center" alt="GitHub Readme Stats" />
 <h3 align="center"> A lightweight tool to search any player's records on any map using the official Trackmania & Nadeo APIs.</h3>
 <div align="center">
  <p align="center">
    <a href="https://frenchthebeast.github.io/trackmaniamyrecords/"><strong>Visit the website »</strong></a>
</p>
 </div>
<br>

[![FWH - License](https://img.shields.io/pypi/l/tmrl?color=blue)]([https://github.com/trackmania-rl/tmrl/blob/master/LICENSE](https://github.com/FrenchTheBeast/trackmaniamyrecords/blob/main/LICENSE))
![version](https://img.shields.io/badge/version-0.1.1-red)
![uptime](https://img.shields.io/badge/Website%20Uptime-100%25-brightgreen)

---

## 🔧 Architecture

- ⚙️ **Java Backend** — Private repository containing the Java-based backend, hosted on [Render](https://render.com).
- ☁️ **Cloudflare Worker** — Acts as a proxy between the frontend and Nadeo's API to handle CORS and token logic.
- 📦 **Trackmania APIs** — Used to fetch player information and leaderboard records.
- 🗂️ **Database** — Using NocoDB and Postgres to stock player's record / Mapgroup to ensure that we don't overload Trackmania's API 

---
## 📂 Project Structure
```
📁 Cloudfare Workers/
 ┗ 📄 worker.js                ← Proxy between front-end and backend
📁 public/                     ← Web-page of the project
 ┣ 📄 script.js                ← (animation + API requests)
 ┗ 📁 records/
    ┗ 📄 error.html            
📁 backend/
  ┣ 📁 lib/                    ← json package for HTTPRequest 
  ┣ 📁 src/
     ┣  ☕️ AuthUtils.java                  ← Class to access Nadeo's Api, Trackmania's Api
     ┣  ☕️ Group.java                      ← Class containing the GroupId object (DB purposes)
     ┣  ☕️ Main.java                       ← Main class to run the server
     ┣  ☕️ Player.java                     ← Class containing the object Player 
     ┣  ☕️ Record.java                     ← Class containing the object Record
     ┣  ☕️ TokenController.java            ← Class containing handling nadeoTokens
     ┣  ☕️ TrackmaniaController.java       ← Class handling AccountID & Record
     ┗  ☕️ TrackmaniaService.java          ← Class using Nadeo's Token for our purposes.
  ┗  📄 Dockerfile                

```
## 📚 Ressources

> [!WARNING]\
> **IMPORTANT:** Please take note that Nadeo's API are subject to change, refer to the ressource below.

[OpenPlanet](https://webservices.openplanet.dev/oauth/auth)  — Documentation for Trackmania's API <br>
[ManiaExchange](https://api2.mania.exchange/)  — Documentation and API for ManiaExchange <br>
[Trackmania API](https://api.trackmania.com/)  — Official Ubisoft site to obtain an OAuth2 access token <br>
[NocoDB](https://github.com/nocodb/nocodb)     — Open-source database to save records / player times / etc. <br>
[PostgreSQL](https://www.postgresql.org/)      — Open-source tool to manage databases <br>
[Railway](https://railway.com/) — Cloud platform to host our database <br>
[Render](https://render.com/)                  — Cloud platform to host our backend<br>
[Cloudflare Workers](https://workers.cloudflare.com/) — Proxy between our backend and frontend<br>
[JackTerok / TMWorldsRecord](https://tmworldrecords.net/) — Inspired by Jack Terok project


## 🔑 Environment Variables 

```DATABASE_URL``` — API endpoint for our database <br>
```DATABASE_USER_TOKEN``` — Access token for our API <br>
```TRACKMANIA_CLIENT_ID``` — Trackmania Client ID (no need to make it a variable, since it's public anyway) <br>
```TRACKMANIA_CLIENT_SECRET``` — Trackmania Client Secret obtained from api.trackmania.com <br>
```UBI_EMAIL``` — Ubisoft email of the second account used for token requests <br>
```UBI_PASSWORD``` — Ubisoft password of the second account used for token requests <br>

##

> [!NOTE]\
>  During project release, one of the endpoints to retrieve Trackmania player's record broke. The only workaround — if you don't have a more robust database — is to ask the user for Ubisoft OAuth
>  authentication and use this [endpoint](https://webservices.openplanet.dev/core/records/account-records-v2) :
>  ```http
>  GET https://prod.trackmania.core.nadeo.online/v2/accounts/{accountId}/mapRecords?gameMode={gameMode}
>  ```


---
