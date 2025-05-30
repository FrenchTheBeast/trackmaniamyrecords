<p align="center">
 <img width="300px" src="https://cdn2.steamgriddb.com/logo/369a226ba5264d149663022a90786d1f.png" align="center" alt="GitHub Readme Stats" />
 <h3 align="center"> Setup Guide to run the Backend</h3>
 <div align="center">
  <p align="center">
    <a href="https://frenchthebeast.github.io/trackmaniamyrecords/"><strong>Visit the website »</strong></a>
</p>
 </div>
<br>

[![FWH - License](https://img.shields.io/pypi/l/tmrl?color=blue)]([https://github.com/trackmania-rl/tmrl/blob/master/LICENSE](https://github.com/FrenchTheBeast/trackmaniamyrecords/blob/main/LICENSE))
![uptime](https://img.shields.io/badge/Website%20Uptime-100%25-brightgreen)

---

## 🔧 Setup guide

> [!WARNING]\
> Please take note that Nadeo's API are subject to change, refer to the documentation.
<br>

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
