export default {
    async fetch(request) {
        const url = new URL(request.url);
        const pseudo = url.searchParams.get("pseudo");

        if (!pseudo) {
            return new Response(JSON.stringify({ error: "Missing pseudo" }), {
                status: 400,
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                }
            });
        }

        try {
            // Call our backend API on Render 
            const recordsRes = await fetch(`https://mytrackmaniarecords.onrender.com/api/records?pseudo=${encodeURIComponent(pseudo)}`);
            const raw = await recordsRes.text();

            let data;
            try {
                data = JSON.parse(raw);
            } catch {
                return new Response(JSON.stringify({
                    error: "Invalid JSON from backend",
                    body: raw
                }), {
                    status: 500,
                    headers: {
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin": "*"
                    }
                });
            }

            // Handle errors
            if (data.error || !Array.isArray(data)) {
                return new Response(JSON.stringify({
                    error: "Account not found or no records",
                    details: data
                }), {
                    status: 404,
                    headers: {
                        "Content-Type": "application/json",
                        "Access-Control-Allow-Origin": "*"
                    }
                });
            }

            return new Response(JSON.stringify(data), {
                status: 200,
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                }
            });

        } catch (e) {
            return new Response(JSON.stringify({ error: "Unexpected error", details: e.message }), {
                status: 500,
                headers: {
                    "Content-Type": "application/json",
                    "Access-Control-Allow-Origin": "*"
                }
            });
        }
    }
}
