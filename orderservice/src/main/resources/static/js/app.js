function showMessage(text, isError = false) {
    const msgBox = document.getElementById('messageBox');
    msgBox.style.display = 'block';
    msgBox.textContent = text;
    msgBox.className = isError ? 'error' : 'success';
    setTimeout(() => msgBox.style.display = 'none', 5000); // Hide after 5 seconds
}

async function placeOrder() {
    const name = document.getElementById('customerName').value;
    const food = document.getElementById('foodItem').value;
    if (!name || !food) return showMessage("Please fill in both fields!", true);

    try {
        const response = await fetch('/api/orders', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ customerName: name, foodItem: food })
        });
        showMessage(await response.text());
        document.getElementById('customerName').value = '';
        document.getElementById('foodItem').value = '';
        fetchOrders(); // Refresh dashboard instantly
    } catch (e) { showMessage("Failed to place order.", true); }
}

async function pickupOrder() {
    const orderId = document.getElementById('pickupOrderId').value;
    if (!orderId) return showMessage("Please enter an Order ID!", true);

    try {
        const response = await fetch(`/api/orders/${orderId}/pickup`, { method: 'PUT' });
        const resultText = await response.text();
        showMessage(resultText, !response.ok);
        if (response.ok) {
            document.getElementById('pickupOrderId').value = '';
            fetchOrders(); // Refresh dashboard instantly to remove the order
        }
    } catch (e) { showMessage("Failed to communicate with server.", true); }
}

async function fetchOrders() {
    try {
        const response = await fetch('/api/orders');
        const orders = await response.json();
        const dashboardList = document.getElementById('dashboardList');

        dashboardList.innerHTML = ''; // Clear current list

        if (orders.length === 0) {
            dashboardList.innerHTML = '<div class="empty-state">No active orders right now. Kitchen is resting!</div>';
            return;
        }

        orders.forEach(order => {
            const isReady = order.status === 'READY';
            const cardHtml = `
                <div class="order-card ${isReady ? 'ready' : ''}">
                    <div class="order-info">
                        <strong>#${order.id} - ${order.customerName}</strong>
                        <span>${order.foodItem}</span>
                    </div>
                    <span class="badge ${order.status.toLowerCase()}">${order.status}</span>
                </div>
            `;
            dashboardList.innerHTML += cardHtml;
        });
    } catch (error) {
        console.error("Failed to fetch orders for dashboard", error);
    }
}

// Initialize application
window.onload = fetchOrders;

// Connect to SSE endpoint
const eventSource = new EventSource('/api/orders/stream');

eventSource.onmessage = function(event) {
    console.log("Real-time update received from Kafka pipeline:", event.data);
    fetchOrders();
    showMessage("🔔 " + event.data);
};

eventSource.onerror = function() {
    console.log("Lost connection to live stream. Reconnecting...");
};
