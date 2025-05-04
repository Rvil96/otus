async function fetchClients() {
    try {
        const response = await fetch('http://localhost:8080/clients');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const clients = await response.json();
        renderClients(clients);
    } catch (error) {
        console.error('Error fetching clients:', error);
    }
}

function renderClients(clients) {
    const tableBody = document.getElementById('clientTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';

    clients.forEach(client => {
        const row = tableBody.insertRow();

        const idCell = row.insertCell();
        idCell.textContent = client.id;

        const nameCell = row.insertCell();
        nameCell.textContent = client.name;

        const phonesCell = row.insertCell();
        phonesCell.textContent = client.phones.map(phone => phone.number).join(', ');

        const addressCell = row.insertCell();
        addressCell.textContent = client.address.street;
    });
}

async function createClient(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const street = document.getElementById('street').value;
    const phone1 = document.getElementById('phone1').value;
    const phone2 = document.getElementById('phone2').value;

    const newClient = {
        name: name,
        address: { street: street },
        phones: [
            { number: phone1 },
            { number: phone2 }
        ]
    };

    try {
        const response = await fetch('http://localhost:8080/client', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newClient)
        });

        if (!response.ok) {
            throw new Error('Failed to create client');
        }

        fetchClients();
        document.getElementById('createClientForm').reset();
    } catch (error) {
        console.error('Error creating client:', error);
    }
}

window.onload = fetchClients;

document.getElementById('createClientForm').addEventListener('submit', createClient);