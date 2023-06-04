const client = require('node-fetch');

(async () => {
    const resp = await client('http://127.0.0.1:3001/data', {
        method: 'GET'
    });

    const data = await resp.json();
    console.log(data);
})();