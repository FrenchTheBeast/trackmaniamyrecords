# 🏎️ Trackmania Search Records

**Trackmania Search Records** — A lightweight tool to search any player's records on any map using the official Trackmania & Nadeo APIs.

---

## 🔧 Architecture

- ⚙️ **Java Backend** — Private repository containing the Java-based backend (**soon to be available**), hosted on [Render](https://render.com).
- ☁️ **Cloudflare Worker** — Acts as a proxy between the frontend and Nadeo's API to handle CORS and token logic.
- 📦 **Trackmania APIs** — Used to fetch player information and leaderboard records.
- 🗂️ **Database** — Using NocoDB and Postgres to stock player's record / Mapgroup to ensure that we don't overload Trackmania's API 

---

## 🚀 Live Website

🌐 [Access the site](https://frenchthebeast.github.io/trackmaniamyrecords)

---

## 📂 Project Structure

```
📁 public/
 ┣ 📄 script.js          ← (animation + API requests)
 ┗ 📁 records/
    ┗ 📄 error.html      ← If API is down / Player does not exist
```

---

>  We are not affiliated with or endorsed by Nadeo or Ubisoft. All relevant trademarks belong to their respective owners.
