let currentStage = "calibrated";

async function loadData(stage) {

    currentStage = stage;

    const response = await fetch(`http://localhost:8080/${stage}`);
    const data = await response.json();

    renderTable(data);
    updateStatus(data);
}

function renderTable(data) {

    let html = `
        <table>
        <tr>
            <th>Time</th>
            <th>Temp</th>
            <th>Humidity</th>
            <th>Wind</th>
            <th>Pressure</th>
            <th>Rainfall</th>
            <th>Anomaly Score</th>
        </tr>
    `;

    data.forEach(row => {

        let color = "";

        if (row.score > 0.7) {
            color = "style='background:#ff7675;color:white;'";
        }

        html += `
            <tr ${color}>
                <td>${row.timestamp}</td>
                <td>${row.temperature}</td>
                <td>${row.humidity}</td>
                <td>${row.windSpeed}</td>
                <td>${row.pressure}</td>
                <td>${row.rainfall}</td>
                <td>${row.score ? row.score.toFixed(3) : "-"}</td>
            </tr>
        `;
    });

    html += "</table>";

    document.getElementById("tableContainer").innerHTML = html;
}

function updateStatus(data) {

    let maxScore = 0;

    data.forEach(row => {
        if (row.score && row.score > maxScore)
            maxScore = row.score;
    });

    const statusBox = document.getElementById("statusBox");
    const meterFill = document.getElementById("meterFill");

    let percent = Math.min(maxScore * 100, 100);
    meterFill.style.width = percent + "%";

    if (maxScore > 0.7) {
        statusBox.innerText = "STATUS: 🔥 HIGH RISK";
        statusBox.style.background = "red";
        meterFill.style.background = "red";
    }
    else if (maxScore > 0.5) {
        statusBox.innerText = "STATUS: ⚠ MODERATE";
        statusBox.style.background = "orange";
        meterFill.style.background = "orange";
    }
    else {
        statusBox.innerText = "STATUS: NORMAL";
        statusBox.style.background = "green";
        meterFill.style.background = "green";
    }
}

setInterval(() => {
    loadData(currentStage);
}, 5000);