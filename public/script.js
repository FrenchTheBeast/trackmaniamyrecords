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

// Backend interaction -> Calling trackmania.reyzosfx.workers.dev which handle all the API request -> Workers call our Java Backend 
const input = document.querySelector('.input');
const button = document.querySelector('.btn');

button.addEventListener('click', async () => {
    const pseudo = input.value.trim();
    if (!pseudo) return alert("Please enter a username!");

    try {
        const res = await fetch(`https://trackmania.reyzosfx.workers.dev/?pseudo=${encodeURIComponent(pseudo)}`);
        const data = await res.json();

        if (data.error) {
            window.location.href = `records/error.html?pseudo=${encodeURIComponent(pseudo)}`;
            return;
        }

        console.log("Records reçus :", data);

        window.location.href = `record/results.html?pseudo=${encodeURIComponent(pseudo)}`;

    } catch (e) {
        console.error("Erreur :", e);
        window.location.href = "records/error.html";
    }
});
