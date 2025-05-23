// Animation du titre
const titleText = "Trackmania\nSearch Records";
const titleElement = document.getElementById("title");

function animateTitle(text, element) {
    element.innerHTML = '';
    let delay = 0;
    text.split('').forEach((char, i) => {
        const span = document.createElement('span');
        span.textContent = char;
        span.style.animationDelay = `${delay}s`;
        span.classList.add('letter');
        element.appendChild(span);
        delay += char === ' ' ? 0.01 : 0.05;
        if (char === '\n') element.appendChild(document.createElement('br'));
    });
}

animateTitle(titleText, titleElement);

// Interaction avec le backend
const input = document.querySelector('.input');
const button = document.querySelector('.btn');

button.addEventListener('click', async () => {
    const pseudo = input.value.trim();
    if (!pseudo) return alert("Please enter a username!");

    try {
        const res = await fetch(`https://trackmania.reyzosfx.workers.dev/?pseudo=${encodeURIComponent(pseudo)}`);
        const data = await res.json();

        if (data.error) {
            window.location.href = "records/error.html";
            return;
        }

        console.log("Records reçus :", data);

        let html = "<h2>Résultats pour " + pseudo + ":</h2><ul>";
        data.forEach(record => {
            html += `<li><strong>${record.mapName}</strong> - ${record.time}s</li>`;
        });
        html += "</ul>";

        let results = document.getElementById("results");
        if (!results) {
            results = document.createElement("div");
            results.id = "results";
            document.body.appendChild(results);
        }
        results.innerHTML = html;

    } catch (e) {
        console.error("Erreur :", e);
        window.location.href = "records/error.html";
    }
});
