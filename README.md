# 🏎️ Trackmania Search Records

**Trackmania Search Records** — A lightweight tool to search any player's records on any map using the official Trackmania & Nadeo APIs.

---

## 🔧 Architecture

- ⚙️ **Java Backend** — Private repository containing the Java-based backend (**soon to be available**), hosted on [Render](https://render.com).
- ☁️ **Cloudflare Worker** — Acts as a proxy between the frontend and Nadeo's API to handle CORS and token logic.
- 📦 **Trackmania APIs** — Used to fetch player information and leaderboard records.
- 🗂️ **Database** — Using NocoDB and Postgres to stock player's record / Mapgroup to ensure that we don't overload Trackmania's API 

---
## 📂 Project Structure
```
📁 public/                     ← Web-page of the website 
 ┣ 📄 script.js                ← (animation + API requests)
 ┗ 📁 records/
    ┗ 📄 error.html            
📁 backend/
  ┣ 📁 lib/                    ← json package for HTTPRequest 
  ┣ 📁 src/
     ┣  ☕️ AuthUtils.java                  ← Class to access Nadeo's Api, Trackmania's Api
     ┣  ☕️ Group.java                      ← Class containing the object Group (DB purposes)
     ┣  ☕️ Main.java                       ← Main class to run the server
     ┣  ☕️ Player.java                     ← Class containing the object Player 
     ┣  ☕️ Record.java                     ← Class containing the object Record
     ┣  ☕️ TokenController.java            ← Class containing handling nadeoTokens
     ┣  ☕️ TrackmaniaController.java       ← Class containing all handling of AccountID & Records
     ┗  ☕️ TrackmaniaService.java          ← Class using Nadeo's Token for our purposes.
  ┣ 📄 Dockerfile                
  ┗ 📄 Trackmania.iml 

```

---