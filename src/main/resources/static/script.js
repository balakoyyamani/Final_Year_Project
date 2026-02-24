let currentStage = "calibrated";
let chart;
let alertHistory = [];
let demoMode = false;
let fullData = [];
let currentIndex = 0;
let animationInterval;

/* ---------- LOAD DATA ---------- */

async function loadData(stage) {

    if (demoMode) return;

    currentStage = stage;

    const response = await fetch(`http://localhost:8080/${stage}`);
    fullData = await response.json();

    renderTable(fullData);
    updateStatus(fullData);

    startGraphAnimation();
}

/* ---------- GRAPH ANIMATION ---------- */

function startGraphAnimation() {

    if (animationInterval)
        clearInterval(animationInterval);

    currentIndex = 0;

    animationInterval = setInterval(() => {

        if (fullData.length === 0) return;

        const singleData = [ fullData[currentIndex] ];

        updateChart(singleData);

        currentIndex++;

        if (currentIndex >= fullData.length)
            currentIndex = 0;

    }, 2000);
}

/* ---------- BAR CHART ---------- */

function updateChart(data) {

    const labels = data.map(d => d.timestamp);
    const temp = data.map(d => d.temperature);
    const hum = data.map(d => d.humidity);
    const pres = data.map(d => d.pressure);

    if (chart) chart.destroy();

    const ctx = document.getElementById("barChart");

    chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: 'Temperature',
                    data: temp,
                    backgroundColor: 'rgba(255,0,0,0.7)'
                },
                {
                    label: 'Humidity',
                    data: hum,
                    backgroundColor: 'rgba(0,0,255,0.7)'
                },
                {
                    label: 'Pressure',
                    data: pres,
                    backgroundColor: 'rgba(0,255,0,0.7)'
                }
            ]
        },
        options: {
            animation: { duration: 1000 },
            responsive: true
        }
    });
}

/* ---------- TABLE ---------- */

function renderTable(data) {

    let html = `<table>
        <tr>
            <th>Time</th>
            <th>Temp</th>
            <th>Humidity</th>
            <th>Pressure</th>
            <th>Score</th>
        </tr>`;

    data.forEach(row => {

        let style = "";

        if (row.score && row.score > 0.7)
            style = "style='background:red;color:white;'";

        html += `
            <tr ${style}>
                <td>${row.timestamp}</td>
                <td>${row.temperature}</td>
                <td>${row.humidity}</td>
                <td>${row.pressure}</td>
                <td>${row.score ? row.score.toFixed(3) : "-"}</td>
            </tr>`;
    });

    html += "</table>";

    document.getElementById("tableContainer").innerHTML = html;
}

/* ---------- STATUS ---------- */

function updateStatus(data) {

    let maxScore = 0;
    let anomalyCount = 0;

    data.forEach(row => {
        if (row.score && row.score > maxScore)
            maxScore = row.score;

        if (row.score && row.score > 0.7)
            anomalyCount++;
    });

    document.getElementById("anomalyCount").innerHTML =
        `<i class="fa-solid fa-triangle-exclamation"></i> Anomalies: ${anomalyCount}`;

    const statusBox = document.getElementById("statusBox");
    const meterFill = document.getElementById("meterFill");

    meterFill.style.width = (maxScore * 100) + "%";

    if (maxScore > 0.7) {

        statusBox.innerHTML =
            `<i class="fa-solid fa-fire"></i> STATUS: HIGH RISK`;

        statusBox.style.background = "red";
        meterFill.style.background = "red";

        const time = new Date().toLocaleTimeString();
        alertHistory.push("🔥 Fire Risk at " + time);
        updateHistory();

    } else {

        statusBox.innerHTML =
            `<i class="fa-solid fa-shield"></i> STATUS: NORMAL`;

        statusBox.style.background = "green";
        meterFill.style.background = "green";
    }
}

/* ---------- ALERT HISTORY ---------- */

function updateHistory() {

    let html = "<ul>";

    alertHistory.slice(-5).forEach(alert => {
        html += `<li>${alert}</li>`;
    });

    html += "</ul>";

    document.getElementById("alertHistory").innerHTML = html;
}

/* ---------- EXPORT FUNCTIONS ---------- */

async function exportCalibratedCSV() {

    const response = await fetch(`http://localhost:8080/calibrated`);
    const data = await response.json();

    let csv = "Time,Temperature,Humidity,Pressure,WindSpeed,Rainfall\n";

    data.forEach(row => {
        csv += `${row.timestamp},${row.temperature},${row.humidity},${row.pressure},${row.windSpeed},${row.rainfall}\n`;
    });

    downloadCSV(csv, "calibrated_data.csv");
}

async function exportAnomaliesCSV() {

    const response = await fetch(`http://localhost:8080/${currentStage}`);
    const data = await response.json();

    const anomalies = data.filter(row => row.score && row.score > 0.7);

    if (anomalies.length === 0) {
        alert("No anomalies detected.");
        return;
    }

    let csv = "Time,Temperature,Humidity,Pressure,Score\n";

    anomalies.forEach(row => {
        csv += `${row.timestamp},${row.temperature},${row.humidity},${row.pressure},${row.score}\n`;
    });

    downloadCSV(csv, "anomaly_report.csv");
}

function downloadCSV(content, filename) {

    const blob = new Blob([content], { type: "text/csv" });
    const link = document.createElement("a");

    link.href = URL.createObjectURL(blob);
    link.download = filename;
    link.click();
}

/* ---------- DEMO MODE ---------- */

function toggleDemo() {

    demoMode = !demoMode;

    if (demoMode) {

        alert("Demo Mode Activated");
        clearInterval(animationInterval);

        animationInterval = setInterval(() => {

            const fake = [{
                timestamp: new Date().toISOString(),
                temperature: 40 + Math.random() * 15,
                humidity: 20 + Math.random() * 60,
                pressure: 980 + Math.random() * 30,
                score: Math.random()
            }];

            renderTable(fake);
            updateStatus(fake);
            updateChart(fake);

        }, 2000);

    } else {

        loadData(currentStage);
    }
}

loadData("calibrated");