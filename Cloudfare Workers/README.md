# 🔧 Cloudflare Setup Guide

1. Clone this repo :  `git git clone https://github.com/FrenchTheBeast/trackmaniamyrecords`
2. Change this endpoint to your backend cloud environment or to your server :
```js
const recordsRes = await fetch(`https://mytrackmaniarecords.onrender.com/api/records?pseudo=${encodeURIComponent(pseudo)}`)
```
3. Build a workers app with [Cloudflare](https://workers.cloudflare.com/). 
4. Deploy your workers 

> [!NOTE]\
> If you are not familiar with Cloudflare, you can also integrate this script into the front-end /public/* and directly call your backend with
> your main website.

---
