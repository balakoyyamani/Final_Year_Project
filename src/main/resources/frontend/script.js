function toggle(id) {
    let box = document.getElementById(id);
    box.style.display = box.style.display === "block" ? "none" : "block";
}

function show(id) {
    let panels = document.getElementsByClassName("panel");
    for (let p of panels) {
        p.style.display = "none";
    }
    document.getElementById(id).style.display = "block";
}

show("temp");
