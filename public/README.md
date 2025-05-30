# Quick installation tips 
* Don't forget to update the workers link below in the **script.js**

```js
try {
        const res = await fetch(`https://trackmania.reyzosfx.workers.dev/?pseudo=${encodeURIComponent(pseudo)}`);
        const data = await res.json();
```

* Don't forget to update the workers link below in the **/records/results.html**
  
```js
if (!pseudo) {
      document.getElementById("results").innerHTML = "❌ No username given.";
    } else {
      fetch(`https://trackmania.reyzosfx.workers.dev/?pseudo=${encodeURIComponent(pseudo)}`)
        .then(res => res.json())
        .then(data => {
          if (data.error || !Array.isArray(data)) {
            document.getElementById("results").innerHTML = "❌ No record found.";
            return;
          }
```
---
