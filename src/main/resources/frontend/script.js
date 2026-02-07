function show(id) {
    let panels = document.getElementsByClassName("panel");
    for (let p of panels) {
        p.style.display = "none";
    }
    document.getElementById(id).style.display = "block";
}

show("raw");
