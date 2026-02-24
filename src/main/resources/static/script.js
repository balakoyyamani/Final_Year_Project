async function showTable(stage) {

    const endpoint = "http://localhost:8080/calibrated";

    try {
        const response = await fetch(endpoint);
        const data = await response.json();

        let html = `
            <table>
                <tr>
                    <th>Time</th>
                    <th>Temperature</th>
                    <th>Humidity</th>
                    <th>Wind Speed</th>
                    <th>Pressure</th>
                    <th>Rainfall</th>
                </tr>
        `;

        data.forEach(row => {
            html += `
                <tr>
                    <td>${row.timestamp}</td>
                    <td>${row.temperature}</td>
                    <td>${row.humidity}</td>
                    <td>${row.windSpeed}</td>
                    <td>${row.pressure}</td>
                    <td>${row.rainfall}</td>
                </tr>
            `;
        });

        html += "</table>";

        document.getElementById("tableContainer").innerHTML = html;

    } catch (error) {
        document.getElementById("tableContainer").innerHTML =
            "<p style='color:red'>Failed to fetch data</p>";
    }
}